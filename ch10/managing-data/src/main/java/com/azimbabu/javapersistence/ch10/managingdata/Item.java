package com.azimbabu.javapersistence.ch10.managingdata;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Item {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @NotNull
  @Size(
      min = 2,
      max = 255,
      message = "Name is required, maximum 255 characters."
  )
  private String name;

  @Future
  private Date auctionEnd;

  private BigDecimal buyNowPrice;

  public Item() {
  }

  public Item(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getAuctionEnd() {
    return auctionEnd;
  }

  public void setAuctionEnd(Date auctionEnd) {
    this.auctionEnd = auctionEnd;
  }

  public BigDecimal getBuyNowPrice() {
    return buyNowPrice;
  }

  public void setBuyNowPrice(BigDecimal buyNowPrice) {
    this.buyNowPrice = buyNowPrice;
  }
}
