package com.azimbabu.javapersistence.ch06.mappingvaluetypes2.model;

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

  // The Address is @Embeddable, no annotation needed here...
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
