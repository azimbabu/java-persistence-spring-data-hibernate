package com.azimbabu.javapersistence.ch06.mappingvaluetypes4.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Bid {

  @Id
  @GeneratedValue(generator = "ID_GENERATOR")
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)  // NOT NULL
  @JoinColumn(name = "ITEM_ID") // Actually the default name
  private Item item;
  @NotNull
  private BigDecimal amount;

  public Bid() {
  }

  public Bid(Item item) {
    this.item = item;
  }

  public Bid(Item item, BigDecimal amount) {
    this(item);
    this.amount = amount;
  }

  public Long getId() {
    return id;
  }

  public Item getItem() {
    return item;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}
