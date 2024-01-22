package com.azimbabu.javapersistence.ch09.onetooneforeigngenerator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.validation.constraints.NotNull;
import org.hibernate.id.ForeignGenerator;

@Entity
public class Address {

  @Id
  @GeneratedValue(generator = "addressKeyGenerator")
  @org.hibernate.annotations.GenericGenerator(
      name = "addressKeyGenerator",
      //strategy = "foreign",
      type = ForeignGenerator.class,
      parameters = @org.hibernate.annotations.Parameter(
          name = "property", value = "user"
      )
  )
  private Long id;

  @OneToOne(optional = false)
  @PrimaryKeyJoinColumn
  private User user;

  @NotNull
  private String street;

  @NotNull
  private String zipcode;

  @NotNull
  private String city;

  public Address() {
  }

  public Address(User user) {
    this.user = user;
  }

  public Address(User user, String street, String zipcode, String city) {
    this.user = user;
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
