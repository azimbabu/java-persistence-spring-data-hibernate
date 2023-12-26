package com.azimbabu.javapersistence.ch07.mappinginheritanceembeddable.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

@MappedSuperclass
public abstract class Measurement {

  @NotNull
  private String name;
  @NotNull
  private String symbol;

  public Measurement() {
  }

  public Measurement(String name, String symbol) {
    this.name = name;
    this.symbol = symbol;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
}
