package com.azimbabu.javapersistence.ch07.mappinginheritanceembeddable.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Item {

  @Id
  @GeneratedValue(generator = "ID_GENERATOR")
  private Long id;

  @NotNull
  @Size(
      min = 2,
      max = 255,
      message = "Name is required, maximum 255 characters."
  )
  private String name;

  private Dimensions dimensions;

  private Weight weight;

  public Item() {
  }

  public Item(String name, Dimensions dimensions, Weight weight) {
    this.name = name;
    this.dimensions = dimensions;
    this.weight = weight;
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

  public Dimensions getDimensions() {
    return dimensions;
  }

  public void setDimensions(
      Dimensions dimensions) {
    this.dimensions = dimensions;
  }

  public Weight getWeight() {
    return weight;
  }

  public void setWeight(Weight weight) {
    this.weight = weight;
  }
}
