package com.azimbabu.javapersistence.ch03.metadataxmljpa;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;

public class Item {

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
}
