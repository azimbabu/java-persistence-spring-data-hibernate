package com.azimbabu.javapersistence.ch13.filtering.dynamic;

import com.azimbabu.javapersistence.ch13.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.ExcludeDefaultListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "USERS")
public class User {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @NotNull
  private String username;

  @NotNull
  private Integer ranking = 0;

  public User() {
  }

  public User(String username) {
    this.username = username;
  }

  public User(String username, Integer ranking) {
    this.username = username;
    this.ranking = ranking;
  }

  public Long getId() {
    return id;
  }

  public @NotNull String getUsername() {
    return username;
  }

  public void setUsername(@NotNull String username) {
    this.username = username;
  }

  public @NotNull Integer getRanking() {
    return ranking;
  }
}
