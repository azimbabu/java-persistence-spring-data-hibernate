package com.azimbabu.javapersistence.ch09.mapsternary.model;

import com.azimbabu.javapersistence.ch09.mapsternary.Constants;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Category {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @NotNull
  private String username;

  @ManyToMany(cascade = CascadeType.PERSIST)
  @MapKeyJoinColumn(name = "ITEM_ID") // Defaults to ITEMADDEDBY_KEY
  @JoinTable(
      name = "CATEGORY_ITEM",
      joinColumns = @JoinColumn(name = "CATEGORY_ID"),
      inverseJoinColumns = @JoinColumn(name = "USER_ID")
  )
  private final Map<Item, User> itemAddedBy = new HashMap<>();

  public Category() {
  }

  public Category(String username) {
    this.username = username;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Map<Item, User> getItemAddedBy() {
    return Collections.unmodifiableMap(itemAddedBy);
  }

  public void addItem(Item item, User user) {
    itemAddedBy.put(item, user);
  }
}
