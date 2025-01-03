package com.azimbabu.javapersistence.querydsl.model;

import com.azimbabu.javapersistence.querydsl.Constants;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  @Getter
  private Long id;

  @Getter
  @Setter
  private String username;

  @Getter
  @Setter
  private String firstName;

  @Getter
  @Setter
  private String lastName;

  @Getter
  @Setter
  private LocalDate registrationDate;

  @Getter
  @Setter
  private String email;

  @Getter
  @Setter
  private int level;

  @Getter
  @Setter
  private boolean active;

  @Embedded
  @Getter
  @Setter
  private Address address;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<Bid> bids = new HashSet<>();

  public User(String username, String firstName, String lastName) {
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public Set<Bid> getBids() {
    return Collections.unmodifiableSet(bids);
  }

  public void addBid(Bid bid) {
    bids.add(bid);
  }
}
