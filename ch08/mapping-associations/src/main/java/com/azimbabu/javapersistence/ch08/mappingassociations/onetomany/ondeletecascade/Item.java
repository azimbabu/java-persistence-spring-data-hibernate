package com.azimbabu.javapersistence.ch08.mappingassociations.onetomany.ondeletecascade;

import com.azimbabu.javapersistence.ch08.mappingassociations.Constants;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Item {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  private String name;

  @OneToMany(mappedBy = "item",  // Required for bidirectional association
      cascade = CascadeType.PERSIST)
  @org.hibernate.annotations.OnDelete(  // Hibernate quirk: Schema options usually on the 'mappedBy' side
      action = OnDeleteAction.CASCADE
  )
  private final Set<Bid> bids = new HashSet<>();

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

  public Set<Bid> getBids() {
    return Collections.unmodifiableSet(bids);
  }

  public void addBid(Bid bid) {
    bids.add(bid);
  }
}
