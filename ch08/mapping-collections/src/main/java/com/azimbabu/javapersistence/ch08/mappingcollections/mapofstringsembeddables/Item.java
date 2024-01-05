package com.azimbabu.javapersistence.ch08.mappingcollections.mapofstringsembeddables;

import com.azimbabu.javapersistence.ch08.mappingcollections.Constants;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Item {

  @ElementCollection
  @CollectionTable(name = "IMAGE")
  @MapKeyColumn(name = "TITLE")
  private final Map<String, Image> images = new HashMap<>();
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

  public Map<String, Image> getImages() {
    return Collections.unmodifiableMap(images);
  }

  public void addImage(String title, Image image) {
    images.put(title, image);
  }
}
