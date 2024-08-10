package com.azimbabu.javapersistence.ch11.transactions.concurrency;

import com.azimbabu.javapersistence.ch11.transactions.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Category {
  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @NotNull
  private String name;

  public Category() {
  }

  public Category(String name) {
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
}
