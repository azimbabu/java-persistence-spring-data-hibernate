package com.azimbabu.javapersistence.ch08.mappingcollections.mapofembeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Filename {

  @Column(nullable = false) // Must be NOT NULL, part of PK!
  private String name;

  public Filename() {
  }

  public Filename(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Filename filename = (Filename) o;
    return Objects.equals(name, filename.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
