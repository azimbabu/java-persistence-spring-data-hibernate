package com.azimbabu.javapersistence.hibernateogm.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@NoArgsConstructor
public class Item {

  @Id
  @GeneratedValue(generator = "ID_GENERATOR")
  @GenericGenerator(name = "ID_GENERATOR", strategy = "uuid2")
  @Getter
  private String id;

  @Getter
  @Setter
  private String name;

  @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
  private Set<Bid> bids = new HashSet<>();

  public Item(String name) {
    this.name = name;
  }

  public Set<Bid> getBids() {
    return Collections.unmodifiableSet(bids);
  }

  public void addBid(Bid bid) {
    bids.add(bid);
  }
}
