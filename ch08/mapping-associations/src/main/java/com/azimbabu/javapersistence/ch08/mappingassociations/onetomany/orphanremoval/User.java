package com.azimbabu.javapersistence.ch08.mappingassociations.onetomany.orphanremoval;

import com.azimbabu.javapersistence.ch08.mappingassociations.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  private String name;

  @OneToMany(mappedBy = "bidder")
  private final Set<Bid> bids = new HashSet<>();

  public User() {
  }

  public User(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Set<Bid> getBids() {
    return Collections.unmodifiableSet(bids);
  }

  public void addBid(Bid bid) {
    bids.add(bid);
  }
}
