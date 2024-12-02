package com.azimbabu.javapersistence.ch13.filtering.cascade;

import com.azimbabu.javapersistence.ch13.Constants;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @NotNull
  private String username;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinColumn(name = "USER_ID", nullable = false)
  private final Set<BillingDetails> billingDetails = new HashSet<>();

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

  public void setUsername(@NotNull String username) {
    this.username = username;
  }

  public Set<BillingDetails> getBillingDetails() {
    return Collections.unmodifiableSet(billingDetails);
  }

  public void addBillingDetails(BillingDetails billingDetails) {
    this.billingDetails.add(billingDetails);
  }
}
