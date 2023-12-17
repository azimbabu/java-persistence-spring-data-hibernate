package com.azimbabu.javapersistence.ch07.mappinginheritancejoined.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BillingDetails {
  @Id
  @GeneratedValue(generator = "ID_GENERATOR")
  private Long id;

  @NotNull
  private String owner;

  public BillingDetails() {
  }

  public BillingDetails(String owner) {
    this.owner = owner;
  }

  public Long getId() {
    return id;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }
}
