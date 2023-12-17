package com.azimbabu.javapersistence.ch06.mappingvaluetypes4.model;

import com.azimbabu.javapersistence.ch06.mappingvaluetypes4.converter.ZipcodeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS")
public class User {

  @Id
  @GeneratedValue(generator = "ID_GENERATOR")
  private Long id;

  private String username;

  @Embedded // this annotation is needed for Convert annotation to work
  @Convert(
      converter = ZipcodeConverter.class,
      attributeName = "city.zipcode"
  )
  private Address homeAddress;

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Address getHomeAddress() {
    return homeAddress;
  }

  public void setHomeAddress(
      Address homeAddress) {
    this.homeAddress = homeAddress;
  }
}
