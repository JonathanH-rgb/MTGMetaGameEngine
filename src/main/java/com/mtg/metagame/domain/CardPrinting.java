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
import java.util.UUID;

@Entity
@Table(name = "card_printings")
public class CardPrinting {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "card_id", nullable = false)
  private Card card;

  @Column(name = "scryfall_id", nullable = false, unique = true)
  private UUID scryfallId;

  @Column(name = "set_code", nullable = false)
  private String setCode;

  @Column(name = "collector_number", nullable = false)
  private String collectorNumber;

  // Guarded by a CHECK in the DB; Check flyway migration file.
  @Column(nullable = false)
  private String rarity;

  protected CardPrinting() {
  }

  public CardPrinting(Card card, UUID scryfallId, String setCode, String collectorNumber, String rarity) {
    this.card = card;
    this.scryfallId = scryfallId;
    this.setCode = setCode;
    this.collectorNumber = collectorNumber;
    this.rarity = rarity;
  }

  public Integer getId() {
    return id;
  }

  public Card getCard() {
    return card;
  }

  public void setCard(Card card) {
    this.card = card;
  }

  public UUID getScryfallId() {
    return scryfallId;
  }

  public void setScryfallId(UUID scryfallId) {
    this.scryfallId = scryfallId;
  }

  public String getSetCode() {
    return setCode;
  }

  public void setSetCode(String setCode) {
    this.setCode = setCode;
  }

  public String getCollectorNumber() {
    return collectorNumber;
  }

  public void setCollectorNumber(String collectorNumber) {
    this.collectorNumber = collectorNumber;
  }

  public String getRarity() {
    return rarity;
  }

  public void setRarity(String rarity) {
    this.rarity = rarity;
  }
}
