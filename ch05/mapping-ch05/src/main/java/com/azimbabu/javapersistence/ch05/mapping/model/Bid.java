package com.azimbabu.javapersistence.ch05.mapping.model;

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

  @NotNull
  private BigDecimal amount;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)  // NOT NULL
  @JoinColumn(name = "ITEM_ID")
  private Item item;

  public Bid() {
  }

  public Bid(Item item) {
    this.item = item;
    item.bids.add(this); // Bidirectional
  }

  public Bid(Item item, BigDecimal amount) {
    this(item);
    this.amount = amount;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Long getId() {
    return id;
  }

  public Item getItem() {
    return item;
  }
}
