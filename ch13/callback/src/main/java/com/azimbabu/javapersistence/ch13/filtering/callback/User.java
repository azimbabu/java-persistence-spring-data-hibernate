package com.azimbabu.javapersistence.ch13.filtering.callback;

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
@ExcludeDefaultListeners
public class User {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @NotNull
  private String username;

  public User() {
  }

  public User(String username) {
    this.username = username;
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

  @PostPersist
  public void logMessage() {
    User currentUser = CurrentUser.INSTANCE.get();
    Log log = Log.INSTANCE;
    log.save("Entity instance persisted by " + currentUser.getUsername() + ":" + this);
  }
}