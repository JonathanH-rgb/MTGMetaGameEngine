package com.mtg.metagame.ingest;

class TransientIngestException extends RuntimeException {

    TransientIngestException(String message) {
        super(message);
    }
}
