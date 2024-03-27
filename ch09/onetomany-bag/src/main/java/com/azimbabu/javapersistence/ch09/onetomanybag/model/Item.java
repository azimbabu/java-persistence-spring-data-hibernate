package com.azimbabu.javapersistence.ch09.onetomanybag.model;

import com.azimbabu.javapersistence.ch09.onetomanybag.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Entity
public class Item {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  private String name;

  @OneToMany(mappedBy = "item")
  private final Collection<Bid> bids = new ArrayList<>();

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

  public Collection<Bid> getBids() {
    return Collections.unmodifiableCollection(bids);
  }

  public void addBid(Bid bid) {
    bids.add(bid);
  }
}
