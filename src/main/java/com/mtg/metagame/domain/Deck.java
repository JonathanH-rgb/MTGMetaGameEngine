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
import java.time.LocalDate;

@Entity
@Table(name = "decks")
public class Deck {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "format_id", nullable = false)
  private Format format;

  private String archetype;

  private String source;

  @Column(name = "source_url")
  private String sourceUrl;

  @Column(name = "played_on")
  private LocalDate playedOn;

  private Short placement;

  protected Deck() { 
  }

  public Deck(String name, Format format) {
    this.name = name;
    this.format = format;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Format getFormat() {
    return format;
  }

  public void setFormat(Format format) {
    this.format = format;
  }

  public String getArchetype() {
    return archetype;
  }

  public void setArchetype(String archetype) {
    this.archetype = archetype;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getSourceUrl() {
    return sourceUrl;
  }

  public void setSourceUrl(String sourceUrl) {
    this.sourceUrl = sourceUrl;
  }

  public LocalDate getPlayedOn() {
    return playedOn;
  }

  public void setPlayedOn(LocalDate playedOn) {
    this.playedOn = playedOn;
  }

  public Short getPlacement() {
    return placement;
  }

  public void setPlacement(Short placement) {
    this.placement = placement;
  }
}
