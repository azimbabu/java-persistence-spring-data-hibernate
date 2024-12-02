package com.azimbabu.javapersistence.ch13.filtering.cascade;

import com.azimbabu.javapersistence.ch13.Constants;
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
  @GeneratedValue(generator = Constants.ID_GENERATOR)
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

  public void setOwner(@NotNull String owner) {
    this.owner = owner;
  }
}
