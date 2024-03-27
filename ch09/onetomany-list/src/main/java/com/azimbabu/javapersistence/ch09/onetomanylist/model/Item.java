package com.azimbabu.javapersistence.ch09.onetomanylist.model;

import com.azimbabu.javapersistence.ch09.onetomanylist.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Item {
  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @NotNull
  private String name;

  @OneToMany
  @JoinColumn(
      name = "ITEM_ID",
      nullable = false
  )
  @OrderColumn(
      name = "BID_POSITION",
      nullable = false
  )
  private List<Bid> bids = new ArrayList<>();

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

  public List<Bid> getBids() {
    return Collections.unmodifiableList(bids);
  }

  public void addBid(Bid bid) {
    bids.add(bid);
  }
}
