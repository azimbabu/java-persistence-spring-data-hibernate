package com.azimbabu.javapersistence.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("USERS")
public class User {
  @Id
  private Long id;

  private String username;

  private LocalDate registrationDate;

  private String email;

  private int level;

  private boolean active;

  @MappedCollection(idColumn = "USER_ID")
  private Set<UserAddress> addresses = new HashSet<>();

  public User() {
  }

  public User(String username) {
    this.username = username;
  }

  public User(String username, LocalDate registrationDate) {
    this.username = username;
    this.registrationDate = registrationDate;
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

  public LocalDate getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(LocalDate registrationDate) {
    this.registrationDate = registrationDate;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Set<UserAddress> getAddresses() {
    return Collections.unmodifiableSet(addresses);
  }

  public void addAddress(Address address) {
    addresses.add(new UserAddress(address.getId()));
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", registrationDate=" + registrationDate +
        ", email='" + email + '\'' +
        ", level=" + level +
        ", active=" + active +
        ", addresses=" + addresses +
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
    return id.equals(user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
