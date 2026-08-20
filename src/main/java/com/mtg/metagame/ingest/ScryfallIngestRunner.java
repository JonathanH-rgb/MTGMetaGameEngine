package com.mtg.metagame.ingest;

import com.mtg.metagame.domain.Card;
import com.mtg.metagame.repository.CardIngestRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "scryfall.ingest-on-startup", havingValue = "true")
public class ScryfallIngestRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ScryfallIngestRunner.class);
    private static final int BATCH_SIZE = 500;

    private final ScryfallBulkClient client;
    private final CardIngestRepository repository;

    ScryfallIngestRunner(ScryfallBulkClient client, CardIngestRepository repository) {
        this.client = client;
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        log.info("Starting Scryfall card ingestion...");
        long start = System.nanoTime();
        List<Card> buffer = new ArrayList<>(BATCH_SIZE);
        long[] upserted = {0};
        long[] skipped = {0};

        client.streamCards(source -> {
            CardMapper.toCard(source).ifPresentOrElse(buffer::add, () -> skipped[0]++);
            if (buffer.size() >= BATCH_SIZE) {
                upserted[0] += repository.upsertBatch(buffer);
                buffer.clear();
            }
        });

        if (!buffer.isEmpty()) {
            upserted[0] += repository.upsertBatch(buffer);
            buffer.clear();
        }

        double seconds = (System.nanoTime() - start) / 1_000_000_000.0;
        log.info("Ingestion complete: {} upserted, {} skipped (no oracle_id) in {}s",
                upserted[0], skipped[0], String.format("%.1f", seconds));
    }
}
