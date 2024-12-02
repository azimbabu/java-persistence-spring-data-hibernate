package com.azimbabu.javapersistence.ch13.filtering.cascade;

import com.azimbabu.javapersistence.ch13.Constants;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Item {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @NotNull
  private String name;

  @OneToMany(
      mappedBy = "item",
      cascade = {CascadeType.DETACH, CascadeType.MERGE}
  )
  private final Set<Bid> bids = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SELLER_ID", nullable = false)
  @org.hibernate.annotations.Cascade(
      org.hibernate.annotations.CascadeType.REPLICATE
  )
  private User seller;

  public Item() {
  }

  public Item(String name, User seller) {
    this.name = name;
    this.seller = seller;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(@NotNull String name) {
    this.name = name;
  }

  public Set<Bid> getBids() {
    return Collections.unmodifiableSet(bids);
  }

  public void addBid(Bid bid) {
    bids.add(bid);
  }

  public User getSeller() {
    return seller;
  }

  public void setSeller(User seller) {
    this.seller = seller;
  }
}
