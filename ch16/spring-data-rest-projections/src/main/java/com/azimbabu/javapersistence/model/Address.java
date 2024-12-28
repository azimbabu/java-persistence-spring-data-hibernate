package com.azimbabu.javapersistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Address {
  @Id
  @GeneratedValue
  private Long id;

  private String street;

  private String zipCode;

  private String city;

  private String state;

  protected Address() {
  }

  public Address(String street, String zipCode, String city, String state) {
    this.street = street;
    this.zipCode = zipCode;
    this.city = city;
    this.state = state;
  }

  public Long getId() {
    return id;
  }

  public String getStreet() {
    return street;
  }

  public String getZipCode() {
    return zipCode;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  @Override
  public String toString() {
    return String.format("%s, %s %s, %s", street, zipCode, city, street);
  }
}
