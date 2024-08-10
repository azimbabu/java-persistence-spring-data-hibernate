package com.azimbabu.javapersistence.ch11.transactions.concurrency;

import com.azimbabu.javapersistence.ch11.transactions.Constants;
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

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Item item;

  @NotNull
  private BigDecimal amount;

  public Bid() {
  }

  public Bid(Item item, BigDecimal amount) {
    this.item = item;
    this.amount = amount;
  }

  public Bid(Item item, BigDecimal amount, Bid lastBid) {
    if (lastBid != null && amount.compareTo(lastBid.getAmount()) < 1) {
      throw new InvalidBidException("Bid amount " + amount + " too low, last bid was " + lastBid.getAmount());
    }
    this.item = item;
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
}
