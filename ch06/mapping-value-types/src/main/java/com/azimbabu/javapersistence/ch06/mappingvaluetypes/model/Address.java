package com.azimbabu.javapersistence.ch06.mappingvaluetypes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class Address {

  @NotNull  // Ignored for DDL generation!
  @Column(nullable = false) // Used for DDL generation!
  private String street;

  @NotNull  // Ignored for DDL generation!
  @Column(nullable = false, length = 5) // Used for DDL generation! Override VARCHAR(255)
  private String zipcode;

  @NotNull  // Ignored for DDL generation!
  @Column(nullable = false) // Used for DDL generation!
  private String city;

  public Address() {
  }

  public Address(String street, String zipcode, String city) {
    this.street = street;
    this.zipcode = zipcode;
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getZipcode() {
    return zipcode;
  }

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }
}
