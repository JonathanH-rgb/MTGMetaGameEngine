package com.mtg.metagame.ingest;

import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "scryfall.ingest-on-startup", havingValue = "true")
public class ScryfallIngestRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ScryfallIngestRunner.class);

    private final ScryfallBulkClient client;

    ScryfallIngestRunner(ScryfallBulkClient client) {
        this.client = client;
    }

    @Override
    public void run(String... args) {
        log.info("Resolving Scryfall bulk download URI...");
        long start = System.nanoTime();
        AtomicLong count = new AtomicLong();

        client.streamCards(card -> {
            long n = count.incrementAndGet();
            if (n <= 3 || n % 5000 == 0) {
                log.info("card #{}: {} [{}] cmc={} colors={}",
                        n, card.name(), card.typeLine(), card.cmc(), card.colorIdentity());
            }
        });

        double seconds = (System.nanoTime() - start) / 1_000_000_000.0;
        log.info("Streamed {} cards in {}s", count.get(), String.format("%.1f", seconds));
    }
}
