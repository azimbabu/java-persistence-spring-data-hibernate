package com.azimbabu.javapersistence.ch08.mappingcollections.listofstrings;

import com.azimbabu.javapersistence.ch08.mappingcollections.Constants;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OrderColumn;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Item {

  @ElementCollection
  @CollectionTable(name = "IMAGE")
  @OrderColumn  // Enables persistent order, Defaults to IMAGES_ORDER
  @Column(name = "FILENAME")
  private final List<String> images = new ArrayList<>();
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

  public List<String> getImages() {
    return Collections.unmodifiableList(images);
  }

  public void addImage(String image) {
    images.add(image);
  }
}
