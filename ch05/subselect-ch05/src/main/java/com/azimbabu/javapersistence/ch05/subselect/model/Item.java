package com.azimbabu.javapersistence.ch05.subselect.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Item {

  @Transient
  Set<Bid> bids = new HashSet<>();
  @Id
  @GeneratedValue(generator = "ID_GENERATOR")
  private Long id;
  @Version
  private long version;
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

  public Set<Bid> getBids() {
    return Collections.unmodifiableSet(bids);
  }
}
