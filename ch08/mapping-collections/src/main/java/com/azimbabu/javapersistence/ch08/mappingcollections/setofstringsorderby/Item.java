package com.azimbabu.javapersistence.ch08.mappingcollections.setofstringsorderby;

import com.azimbabu.javapersistence.ch08.mappingcollections.Constants;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Item {

  @ElementCollection
  @CollectionTable(name = "IMAGE")
  @Column(name = "FILENAME")  // Defaults to IMAGES
  //@OrderBy("FILENAME asc")
  //@org.hibernate.annotations.OrderBy(clause = "FILENAME asc") // deprecated and marked ForRemoval
  @org.hibernate.annotations.SQLOrder("FILENAME asc")
  private final Set<String> images = new LinkedHashSet<>();
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

  public Set<String> getImages() {
    return Collections.unmodifiableSet(images);
  }

  public void addImage(String image) {
    images.add(image);
  }
}
