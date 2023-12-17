package com.azimbabu.javapersistence.ch06.mappingvaluetypes.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
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

  // The Address is @Embeddable, no annotation needed here...
  private Address homeAddress;

  @Embedded // Not necessary...
  @AttributeOverride(name = "street", column = @Column(name = "BILLING_STREET"))
  @AttributeOverride(name = "zipcode", column = @Column(name = "BILLING_ZIPCODE", length = 5))
  @AttributeOverride(name = "city", column = @Column(name = "BILLING_CITY"))
  private Address billingAddress;

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

  public void setHomeAddress(Address homeAddress) {
    this.homeAddress = homeAddress;
  }

  public Address getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(
      Address billingAddress) {
    this.billingAddress = billingAddress;
  }
}
