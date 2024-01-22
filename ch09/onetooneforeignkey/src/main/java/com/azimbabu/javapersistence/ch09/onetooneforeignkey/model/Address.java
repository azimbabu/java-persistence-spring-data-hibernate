package com.azimbabu.javapersistence.ch09.onetooneforeignkey.model;

import com.azimbabu.javapersistence.ch09.onetooneforeignkey.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Address {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @NotNull
  private String street;

  @NotNull
  private String zipcode;

  @NotNull
  private String city;

  public Address() {
  }

  public Address(String street, String zipcode, String city) {
    this.street = street;
    this.zipcode = zipcode;
    this.city = city;
  }

  public Long getId() {
    return id;
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
