package com.azimbabu.javapersistence.ch09.manytomanyternary.model;

import com.azimbabu.javapersistence.ch09.manytomanyternary.Constants;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Category {

  @ElementCollection
  @CollectionTable(
      name = "CATEGORY_ITEM",
      joinColumns = @JoinColumn(name = "CATEGORY_ID")
  )
  private final Set<CategorizedItem> categorizedItems = new HashSet<>();
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

  public Set<CategorizedItem> getCategorizedItems() {
    return Collections.unmodifiableSet(categorizedItems);
  }

  public void addCategorizedItem(CategorizedItem categorizedItem) {
    categorizedItems.add(categorizedItem);
  }
}
