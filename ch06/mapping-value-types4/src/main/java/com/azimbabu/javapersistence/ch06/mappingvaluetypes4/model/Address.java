package com.azimbabu.javapersistence.ch06.mappingvaluetypes4.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class Address {

  @NotNull
  @Column(nullable = false)
  private String street;

  @NotNull
  @AttributeOverride(
      name = "name",
      column = @Column(name = "CITY", nullable = false)
  )
  private City city;

  public Address() {
  }

  public Address(String street, City city) {
    this.street = street;
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }
}
