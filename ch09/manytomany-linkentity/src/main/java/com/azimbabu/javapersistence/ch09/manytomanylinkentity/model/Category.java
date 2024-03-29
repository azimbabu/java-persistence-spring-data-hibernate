package com.azimbabu.javapersistence.ch09.manytomanylinkentity.model;

import com.azimbabu.javapersistence.ch09.manytomanylinkentity.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Category {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @NotNull
  private String name;

  @OneToMany(mappedBy = "category")
  private final Set<CategorizedItem> categorizedItems = new HashSet<>();

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

  public Set<CategorizedItem> getCategorizedItems() {
    return Collections.unmodifiableSet(categorizedItems);
  }

  public void addCategorizedItem(CategorizedItem categorizedItem) {
    categorizedItems.add(categorizedItem);
  }
}
