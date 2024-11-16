package com.azimbabu.javapersistence.ch12.eagerjoin;

import com.azimbabu.javapersistence.ch12.Constants;
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
  @ManyToOne(fetch = FetchType.LAZY)
  private Item item;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  private User bidder;

  @NotNull
  private BigDecimal amount;

  public Bid() {
  }

  public Bid(BigDecimal amount) {
    this.amount = amount;
  }

  public Bid(Item item, User bidder, BigDecimal amount) {
    this.item = item;
    this.bidder = bidder;
    this.amount = amount;
  }

  public Long getId() {
    return id;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(
      @NotNull Item item) {
    this.item = item;
  }

  public User getBidder() {
    return bidder;
  }

  public void setBidder(
      @NotNull User bidder) {
    this.bidder = bidder;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(@NotNull BigDecimal amount) {
    this.amount = amount;
  }
}
