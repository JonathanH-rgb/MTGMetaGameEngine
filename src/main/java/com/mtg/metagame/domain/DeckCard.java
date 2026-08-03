package com.mtg.metagame.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "deck_cards")
public class DeckCard {

    @EmbeddedId
    private DeckCardId id;

    @Column(nullable = false)
    private Short quantity;

    protected DeckCard() { 
    }

    public DeckCard(DeckCardId id, Short quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public DeckCardId getId() {
        return id;
    }

    public Short getQuantity() {
        return quantity;
    }

    public void setQuantity(Short quantity) {
        this.quantity = quantity;
    }
}
