package com.azimbabu.javapersistence.ch08.mappingcollections.setofembeddablesorderby;

import com.azimbabu.javapersistence.ch08.mappingcollections.Constants;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OrderBy;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Item {

  @ElementCollection
  @CollectionTable(name = "IMAGE")
  @OrderBy("filename DESC, width DESC")
  private final Set<Image> images = new LinkedHashSet<>();
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

  public Set<Image> getImages() {
    return Collections.unmodifiableSet(images);
  }

  public void addImage(Image image) {
    images.add(image);
  }
}
