package com.mtg.metagame.service;

public class DeckNotFoundException extends RuntimeException {

    public DeckNotFoundException(String deckName) {
        super("Deck not found: " + deckName);
    }
}
