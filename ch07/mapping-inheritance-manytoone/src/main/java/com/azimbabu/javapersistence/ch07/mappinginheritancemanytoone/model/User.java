package com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "USERS")
public class User {

  @Id
  @GeneratedValue(generator = "ID_GENERATOR")
  private Long id;

  @NotNull
  private String username;

  @ManyToOne
  private BillingDetails defaultBilling;

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

  public BillingDetails getDefaultBilling() {
    return defaultBilling;
  }

  public void setDefaultBilling(
      BillingDetails defaultBilling) {
    this.defaultBilling = defaultBilling;
  }
}
