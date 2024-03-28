package com.azimbabu.javapersistence.ch09.manytomanybidirectional.model;

import com.azimbabu.javapersistence.ch09.manytomanybidirectional.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Item {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @NotNull
  private String name;

  @ManyToMany(mappedBy = "items")
  private final Set<Category> categories = new HashSet<>();

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

  public Set<Category> getCategories() {
    return Collections.unmodifiableSet(categories);
  }

  public void addCategory(Category category) {
    categories.add(category);
  }
}
