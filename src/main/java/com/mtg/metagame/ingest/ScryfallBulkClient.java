package com.mtg.metagame.ingest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ScryfallBulkClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final ScryfallProperties properties;

    ScryfallBulkClient(ScryfallProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("User-Agent", properties.getUserAgent())
                .defaultHeader("Accept", "application/json")
                .build();
    }

    public URI resolveDownloadUri() {
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
    }

    public void streamCards(Consumer<ScryfallCard> handler) {
        URI downloadUri = resolveDownloadUri();
        restClient.get()
                .uri(downloadUri)
                .exchange((request, response) -> {
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
