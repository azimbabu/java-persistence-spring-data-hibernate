package com.azimbabu.javapersistence.ch11.transactions4.concurrency;

import com.azimbabu.javapersistence.ch11.transactions4.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Category {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @NotNull
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;

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

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }
}
