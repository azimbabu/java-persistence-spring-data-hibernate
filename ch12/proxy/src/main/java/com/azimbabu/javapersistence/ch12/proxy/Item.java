package com.azimbabu.javapersistence.ch12.proxy;

import com.azimbabu.javapersistence.ch12.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class Item {

  private Long id;

  private String name;

  private LocalDateTime auctionEnd;

  private User seller;

  private Set<Category> categories = new HashSet<>();

  private Set<Bid> bids = new HashSet<>();

  public Item() {
  }

  public Item(String name, LocalDateTime auctionEnd, User seller) {
    this.name = name;
    this.auctionEnd = auctionEnd;
    this.seller = seller;
  }

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @NotNull
  public String getName() {
    return name;
  }

  public void setName(@NotNull String name) {
    this.name = name;
  }

  @NotNull
  public LocalDateTime getAuctionEnd() {
    return auctionEnd;
  }

  public void setAuctionEnd(LocalDateTime auctionEnd) {
    this.auctionEnd = auctionEnd;
  }

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  public User getSeller() {
    return seller;
  }

  public void setSeller(User seller) {
    this.seller = seller;
  }

  @ManyToMany(mappedBy = "items")
  public Set<Category> getCategories() {
    return categories;
  }

  // setter is only provided for hibernate
  private void setCategories(Set<Category> categories) {
    this.categories = categories;
  }

  public void addCategory(Category category) {
    categories.add(category);
  }

  @OneToMany(mappedBy = "item")
  @org.hibernate.annotations.LazyCollection(LazyCollectionOption.EXTRA)
  public Set<Bid> getBids() {
    return bids;
  }

  // setter is only provided for hibernate
  private void setBids(Set<Bid> bids) {
    this.bids = bids;
  }

  public void addBid(Bid bid) {
    bids.add(bid);
  }
}
