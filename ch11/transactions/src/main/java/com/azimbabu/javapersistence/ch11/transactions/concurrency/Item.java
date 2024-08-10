package com.azimbabu.javapersistence.ch11.transactions.concurrency;

import com.azimbabu.javapersistence.ch11.transactions.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Item {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @Version
  private long version;

  @NotNull
  private String name;

  private BigDecimal buyNowPrice;

  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;

  public Item() {
  }

  public Item(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public long getVersion() {
    return version;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getBuyNowPrice() {
    return buyNowPrice;
  }

  public void setBuyNowPrice(BigDecimal buyNowPrice) {
    this.buyNowPrice = buyNowPrice;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }
}