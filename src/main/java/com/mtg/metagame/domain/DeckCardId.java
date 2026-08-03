package com.mtg.metagame.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key for {@link DeckCard}: (deck_id, card_id, board).
 * A composite key must be Serializable and implement equals/hashCode.
 */
@Embeddable
public class DeckCardId implements Serializable {

    @Column(name = "deck_id")
    private Long deckId;

    @Column(name = "card_id")
    private Integer cardId;

    @Column(name = "board")
    private String board;

    protected DeckCardId() { 
    }

    public DeckCardId(Long deckId, Integer cardId, String board) {
        this.deckId = deckId;
        this.cardId = cardId;
        this.board = board;
    }

    public Long getDeckId() {
        return deckId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public String getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeckCardId that)) {
            return false;
        }
        return Objects.equals(deckId, that.deckId)
                && Objects.equals(cardId, that.cardId)
                && Objects.equals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deckId, cardId, board);
    }
}
