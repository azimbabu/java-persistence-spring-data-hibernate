package com.azimbabu.javapersistence.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.util.Objects;

@Entity
@Table(name = "USERS")
public class User {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private boolean registered;

  private boolean citizen;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  private Address address;

  @Version
  private Long version;

  protected User() {
  }

  public User(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isRegistered() {
    return registered;
  }

  public void setRegistered(boolean registered) {
    this.registered = registered;
  }

  public boolean isCitizen() {
    return citizen;
  }

  public void setCitizen(boolean citizen) {
    this.citizen = citizen;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", isRegistered=" + registered +
        ", isCitizen=" + citizen +
        ", version=" + version +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return registered == user.registered && Objects.equals(name, user.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, registered);
  }
}