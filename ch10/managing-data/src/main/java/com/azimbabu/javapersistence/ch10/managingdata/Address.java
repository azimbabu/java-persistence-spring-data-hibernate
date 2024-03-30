package com.azimbabu.javapersistence.ch10.managingdata;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

/**
 * Instead of <code>@Entity</code>, this component POJO is marked with <code>@Embeddable</code>. It
 * has no identifier property.
 */
@Embeddable
public class Address {

  @NotNull  // Ignored for DDL generation!
  @Column(nullable = false) // Used for DDL generation!
  private String street;

  @NotNull  // Ignored for DDL generation!
  @Column(nullable = false, length = 5) // Used for DDL generation and  override VARCHAR(255).
  private String zipcode;

  @NotNull  // Ignored for DDL generation!
  @Column(nullable = false) // Used for DDL generation!
  private String city;

  /**
   * Hibernate will call this no-argument constructor to create an instance, and then populate the
   * fields directly.
   */
  public Address() {
  }

  /**
   * You can have additional (public) constructors for convenience.
   */
  public Address(String street, String zipcode, String city) {
    this.street = street;
    this.zipcode = zipcode;
    this.city = city;
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
