package com.azimbabu.javapersistence.ch13.filtering.envers;

import com.azimbabu.javapersistence.ch13.Constants;
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

  public Bid(BigDecimal amount, Item item) {
    this.amount = amount;
    this.item = item;
  }

  public Long getId() {
    return id;
  }

  public @NotNull BigDecimal getAmount() {
    return amount;
  }

  public Item getItem() {
    return item;
  }
}
