package com.azimbabu.javapersistence.ch12.proxy;

import com.azimbabu.javapersistence.ch12.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "USERS")
public class User {

  private Long id;

  private String username;

  public User() {
  }

  public User(String username) {
    this.username = username;
  }

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @NotNull
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
