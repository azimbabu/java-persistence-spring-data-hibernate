package com.azimbabu.javapersistence.ch08.mappingassociations.onetomany.orphanremoval;

import com.azimbabu.javapersistence.ch08.mappingassociations.Constants;
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

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User bidder;

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

  public void setItem(
      Item item) {
    this.item = item;
  }

  public User getBidder() {
    return bidder;
  }

  public void setBidder(
      User bidder) {
    this.bidder = bidder;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}
