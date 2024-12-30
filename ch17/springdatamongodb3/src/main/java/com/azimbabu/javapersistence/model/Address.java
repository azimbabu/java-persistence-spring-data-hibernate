package com.azimbabu.javapersistence.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Address {

  @Id
  @Getter
  private String id;

  @Getter
  private String street;

  @Getter
  private String zipCode;

  @Getter
  private String city;

  @Getter
  private String state;

  protected Address() {
  }

  public Address(String street, String zipCode, String city, String state) {
    this.street = street;
    this.zipCode = zipCode;
    this.city = city;
    this.state = state;
  }

  @Override
  public String toString() {
    return String.format("%s, %s %s, %s", street, zipCode, city, state);
  }
}
