package com.azimbabu.javapersistence.ch09.onetooneforeigngenerator.model;

import com.azimbabu.javapersistence.ch09.onetooneforeigngenerator.Constants;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS")
public class User {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  private String username;

  @OneToOne(
      mappedBy = "user",
      cascade = CascadeType.PERSIST
  )
  private Address shippingAddress;

  public User() {
  }

  public User(String username) {
    this.username = username;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Address getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(
      Address shippingAddress) {
    this.shippingAddress = shippingAddress;
  }
}
