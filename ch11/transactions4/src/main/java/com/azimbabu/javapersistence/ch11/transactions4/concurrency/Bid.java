package com.azimbabu.javapersistence.ch11.transactions4.concurrency;

import com.azimbabu.javapersistence.ch11.transactions4.Constants;
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

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Item item;

  public Bid() {
  }

  public Bid(Item item, BigDecimal amount) {
    this.amount = amount;
    this.item = item;
  }

  public Bid(Item item, BigDecimal amount, Bid lastBid) {
    if (lastBid != null && amount.compareTo(lastBid.getAmount()) < 1) {
      throw new InvalidBidException("Bid amount " + amount + " too low, last bid was " + lastBid.getAmount());
    }
    this.amount = amount;
    this.item = item;
  }

  public Long getId() {
    return id;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Item getItem() {
    return item;
  }
}
