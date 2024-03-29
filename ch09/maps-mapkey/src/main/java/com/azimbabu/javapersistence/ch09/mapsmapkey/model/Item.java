package com.azimbabu.javapersistence.ch09.mapsmapkey.model;

import com.azimbabu.javapersistence.ch09.mapsmapkey.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MapKey;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Item {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @NotNull
  private String name;

  @MapKey(name = "id")
  @OneToMany(mappedBy = "item")
  private final Map<Long, Bid> bids = new HashMap<>();

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

  public Map<Long, Bid> getBids() {
    return Collections.unmodifiableMap(bids);
  }

  public void addBid(Bid bid) {
    bids.put(bid.getId(), bid);
  }
}
