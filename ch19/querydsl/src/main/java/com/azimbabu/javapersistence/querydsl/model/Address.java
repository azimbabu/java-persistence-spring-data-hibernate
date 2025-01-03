package com.azimbabu.javapersistence.querydsl.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Address {

  @Getter
  private String street;

  @Getter
  private String zipcode;

  @Getter
  private String city;

  @Getter
  private String state;

  @Override
  public String toString() {
    return "%s, %s %s, %s".formatted(street, zipcode, city, state);
  }
}
