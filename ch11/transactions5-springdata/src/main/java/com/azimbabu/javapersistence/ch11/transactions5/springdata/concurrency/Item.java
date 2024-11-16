package com.azimbabu.javapersistence.ch11.transactions5.springdata.concurrency;

import com.azimbabu.javapersistence.ch11.transactions5.springdata.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Item {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @Version
  private long version;

  @NotNull
  private String name;

  private LocalDateTime createdAt;

  private BigDecimal buyNowPrice;

  public Item() {
  }

  public Item(String name) {
    this.name = name;
  }

  public Item(String name, LocalDateTime createdAt) {
    this.name = name;
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public long getVersion() {
    return version;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public String getName() {
    return name;
  }

  public void setName(@NotNull String name) {
    this.name = name;
  }

  public BigDecimal getBuyNowPrice() {
    return buyNowPrice;
  }

  public void setBuyNowPrice(BigDecimal buyNowPrice) {
    this.buyNowPrice = buyNowPrice;
  }

  @Override
  public String toString() {
    return "Item{" +
        "id=" + id +
        ", version=" + version +
        ", name='" + name + '\'' +
        ", createdAt=" + createdAt +
        ", buyNowPrice=" + buyNowPrice +
        '}';
  }
}
