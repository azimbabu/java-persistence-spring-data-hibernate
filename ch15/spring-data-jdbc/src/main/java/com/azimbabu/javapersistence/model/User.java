package com.azimbabu.javapersistence.model;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("USERS")
public class User {
  @Id
  @Column("ID")
  private Long id;

  @Column("USERNAME")
  private String username;

  @Column("REGISTRATION_DATE")
  private LocalDate registrationDate;

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

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", registrationDate=" + registrationDate +
        '}';
  }
}
