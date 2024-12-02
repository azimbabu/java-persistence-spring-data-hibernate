package com.azimbabu.javapersistence.ch13.filtering.cascade;

import com.azimbabu.javapersistence.ch13.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

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

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(@NotNull BigDecimal amount) {
    this.amount = amount;
  }

  public Item getItem() {
    return item;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || !(o instanceof Bid bid)) {
      return false;
    }
    return Objects.equals(id, bid.id) && Objects.equals(amount, bid.amount)
        && Objects.equals(item, bid.item);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, amount, item);
  }
}
