package com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USERS")
public class User {
  @Id
  @GeneratedValue(generator = "ID_GENERATOR")
  private Long id;

  @NotNull
  private String username;

  @OneToMany(mappedBy = "user")
  private Set<BillingDetails> billingDetails = new HashSet<>();

  public User() {
  }

  public User(String username) {
    this.username = username;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Set<BillingDetails> getBillingDetails() {
    return Collections.unmodifiableSet(billingDetails);
  }

  public void addBillingDetails(BillingDetails billingDetail) {
    billingDetails.add(billingDetail);
  }
}
