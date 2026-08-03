package com.mtg.metagame.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "cards")
public class Card {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "oracle_id", nullable = false, unique = true)
  private UUID oracleId;

  @Column(nullable = false)
  private String name;

  @Column(name = "mana_value", nullable = false)
  private Short manaValue = 0;

  @Column(name = "type_line")
  private String typeLine;

  @JdbcTypeCode(SqlTypes.ARRAY)
  @Column(name = "color_identity", nullable = false, columnDefinition = "text[]")
  private List<String> colorIdentity = new ArrayList<>();

  protected Card() {
  }

  public Card(UUID oracleId, String name, Short manaValue, String typeLine, List<String> colorIdentity) {
    this.oracleId = oracleId;
    this.name = name;
    this.manaValue = manaValue;
    this.typeLine = typeLine;
    this.colorIdentity = colorIdentity;
  }

  public Integer getId() {
    return id;
  }

  public UUID getOracleId() {
    return oracleId;
  }

  public void setOracleId(UUID oracleId) {
    this.oracleId = oracleId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Short getManaValue() {
    return manaValue;
  }

  public void setManaValue(Short manaValue) {
    this.manaValue = manaValue;
  }

  public String getTypeLine() {
    return typeLine;
  }

  public void setTypeLine(String typeLine) {
    this.typeLine = typeLine;
  }

  public List<String> getColorIdentity() {
    return colorIdentity;
  }

  public void setColorIdentity(List<String> colorIdentity) {
    this.colorIdentity = colorIdentity;
  }
}
