package com.azimbabu.javapersistence.ch09.onetomanybag.model;

import com.azimbabu.javapersistence.ch09.onetomanybag.Constants;
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

  @NotNull
  private BigDecimal amount;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Item item;

  public Bid() {
  }

  public Bid(BigDecimal amount, Item item) {
    this.amount = amount;
    this.item = item;
  }

  public Long getId() {
    return id;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }
}
