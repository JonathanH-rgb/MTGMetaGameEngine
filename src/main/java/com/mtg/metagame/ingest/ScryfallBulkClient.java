package com.mtg.metagame.ingest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@Component
public class ScryfallBulkClient {

    private static final Logger log = LoggerFactory.getLogger(ScryfallBulkClient.class);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final ScryfallProperties properties;
    private final Retry retry;

    ScryfallBulkClient(ScryfallProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(5));
        requestFactory.setReadTimeout(Duration.ofSeconds(30));

        this.restClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .requestFactory(requestFactory)
                .defaultHeader("User-Agent", properties.getUserAgent())
                .defaultHeader("Accept", "application/json")
                .build();

        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(3)
                .intervalFunction(IntervalFunction.ofExponentialBackoff(Duration.ofMillis(500), 2.0))
                .retryExceptions(
                        IOException.class,
                        ResourceAccessException.class,
                        HttpServerErrorException.class,
                        TransientIngestException.class)
                .build();
        this.retry = Retry.of("scryfall", retryConfig);
        this.retry.getEventPublisher().onRetry(event ->
                log.warn("Scryfall call failed, retry {} in {}ms: {}",
                        event.getNumberOfRetryAttempts(),
                        event.getWaitInterval().toMillis(),
                        String.valueOf(event.getLastThrowable())));
    }

    public URI resolveDownloadUri() {
        return retry.executeSupplier(() -> {
            BulkDataList manifest = restClient.get()
                    .uri("/bulk-data")
                    .retrieve()
                    .body(BulkDataList.class);
            String type = properties.getBulkType();
            return manifest.data().stream()
                    .filter(entry -> type.equals(entry.type()))
                    .map(BulkDataEntry::downloadUri)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No bulk-data entry of type: " + type));
        });
    }

    public void streamCards(Consumer<ScryfallCard> handler) {
        URI downloadUri = resolveDownloadUri();
        retry.executeRunnable(() -> download(downloadUri, handler));
    }

    private void download(URI downloadUri, Consumer<ScryfallCard> handler) {
        restClient.get()
                .uri(downloadUri)
                .exchange((request, response) -> {
                    if (response.getStatusCode().is5xxServerError()) {
                        throw new TransientIngestException("Bulk download failed (server): " + response.getStatusCode());
                    }
                    if (!response.getStatusCode().is2xxSuccessful()) {
                        throw new IllegalStateException("Bulk download failed: " + response.getStatusCode());
                    }
                    try (GZIPInputStream gis = new GZIPInputStream(response.getBody());
                            BufferedReader reader = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (!line.isBlank()) {
                                handler.accept(objectMapper.readValue(line, ScryfallCard.class));
                            }
                        }
                    }
                    return null;
                });
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record BulkDataList(@JsonProperty("data") List<BulkDataEntry> data) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record BulkDataEntry(
            @JsonProperty("type") String type,
            @JsonProperty("jsonl_download_uri") URI downloadUri) {
    }
}
