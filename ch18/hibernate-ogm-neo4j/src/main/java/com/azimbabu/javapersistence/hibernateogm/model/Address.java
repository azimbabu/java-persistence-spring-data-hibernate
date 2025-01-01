package com.azimbabu.javapersistence.hibernateogm.model;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {

  @Getter
  @Setter
  private String street;

  @Getter
  @Setter
  private String zipcode;

  @Getter
  @Setter
  private String city;

  @Getter
  @Setter
  private String state;
}
