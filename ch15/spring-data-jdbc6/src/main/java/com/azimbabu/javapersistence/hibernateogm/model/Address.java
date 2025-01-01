package com.azimbabu.javapersistence.hibernateogm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("ADDRESSES")
public class Address {
  @Id
  private Long id;

  private String street;

  private String city;

  public Address(String street, String city) {
    this.street = street;
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

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }
}
