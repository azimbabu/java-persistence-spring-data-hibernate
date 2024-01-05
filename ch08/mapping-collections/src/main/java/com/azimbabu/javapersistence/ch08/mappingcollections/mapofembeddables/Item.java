package com.azimbabu.javapersistence.ch08.mappingcollections.mapofembeddables;

import com.azimbabu.javapersistence.ch08.mappingcollections.Constants;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Item {

  @ElementCollection
  @CollectionTable(name = "IMAGE")
  private final Map<Filename, Image> images = new HashMap<>();
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

  public Map<Filename, Image> getImages() {
    return Collections.unmodifiableMap(images);
  }

  public void addImage(Filename filename, Image image) {
    images.put(filename, image);
  }
}
