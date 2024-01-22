package com.azimbabu.javapersistence.ch09.onetoonejointable.model;

import com.azimbabu.javapersistence.ch09.onetoonejointable.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Item {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  private String name;

  public Item() {
  }

  public Item(String name) {
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
