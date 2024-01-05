package com.azimbabu.javapersistence.ch08.mappingcollections.embeddablesetofstrings;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Embeddable
public class Address {

  @NotNull
  @Column(nullable = false)
  private String street;

  @NotNull
  @Column(nullable = false, length = 5)
  private String zipcode;

  @NotNull
  @Column(nullable = false)
  private String city;

  @ElementCollection
  @CollectionTable(
      name = "CONTACT", // Defaults to USER_CONTACTS
      joinColumns = @JoinColumn(name = "USER_ID") // Default, actually
  )
  @Column(name = "NAME", nullable = false)  // Defaults to CONTACTS
  private final Set<String> contacts = new HashSet<>();

  public Address() {
  }

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

  public Set<String> getContacts() {
    return Collections.unmodifiableSet(contacts);
  }

  public void addContact(String contact) {
    contacts.add(contact);
  }
}
