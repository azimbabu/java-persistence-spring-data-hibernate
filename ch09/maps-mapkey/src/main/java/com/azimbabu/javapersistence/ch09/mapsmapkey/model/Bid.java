package com.azimbabu.javapersistence.ch09.mapsmapkey.model;

import com.azimbabu.javapersistence.ch09.mapsmapkey.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Bid {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Item item;

  @NotNull
  private BigDecimal amount;

  public Bid() {
  }

  public Bid(Item item, BigDecimal amount) {
    this.item = item;
    this.amount = amount;
  }

  public Long getId() {
    return id;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}
