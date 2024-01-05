package com.azimbabu.javapersistence.ch08.mappingcollections.mapofstringsorderby;

import com.azimbabu.javapersistence.ch08.mappingcollections.Constants;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Item {

  @ElementCollection
  @CollectionTable(name = "IMAGE")
  @MapKeyColumn(name = "FILENAME")
  @Column(name = "IMAGENAME")
  @org.hibernate.annotations.SQLOrder("FILENAME desc")
  private final Map<String, String> images = new LinkedHashMap<>();
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

  public void setName(String name) {
    this.name = name;
  }

  public Map<String, String> getImages() {
    return Collections.unmodifiableMap(images);
  }

  public void addImage(String fileName, String imageName) {
    images.put(fileName, imageName);
  }
}
