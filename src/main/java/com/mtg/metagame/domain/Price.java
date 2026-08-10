package com.mtg.metagame.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "prices")
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_printing_id", nullable = false)
    private CardPrinting cardPrinting;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "captured_at", nullable = false)
    private Instant capturedAt;

    protected Price() { 
    }

    public Price(CardPrinting cardPrinting, BigDecimal price, Instant capturedAt) {
        this.cardPrinting = cardPrinting;
        this.price = price;
        this.capturedAt = capturedAt;
    }

    public Long getId() {
        return id;
    }

    public CardPrinting getCardPrinting() {
        return cardPrinting;
    }

    public void setCardPrinting(CardPrinting cardPrinting) {
        this.cardPrinting = cardPrinting;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Instant getCapturedAt() {
        return capturedAt;
    }

    public void setCapturedAt(Instant capturedAt) {
        this.capturedAt = capturedAt;
    }
}
