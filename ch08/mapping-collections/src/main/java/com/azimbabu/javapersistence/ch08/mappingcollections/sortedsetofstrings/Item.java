package com.azimbabu.javapersistence.ch08.mappingcollections.sortedsetofstrings;

import com.azimbabu.javapersistence.ch08.mappingcollections.Constants;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
public class Item {

  @ElementCollection
  @CollectionTable(
      name = "IMAGE", // Defaults to ITEM_IMAGES
      joinColumns = @JoinColumn(name = "ITEM_ID") // Default, actually
  )
  @Column(name = "FILENAME")  // Defaults to IMAGES
  @org.hibernate.annotations.SortNatural
  private final SortedSet<String> images = new TreeSet<>(); // Initialize field here
  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;
  @NotNull
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

  public SortedSet<String> getImages() {
    return Collections.unmodifiableSortedSet(images);
  }

  public void addImage(String image) {
    images.add(image);
  }
}
