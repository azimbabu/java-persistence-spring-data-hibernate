package com.azimbabu.javapersistence.ch08.mappingcollections.bagofstrings;

import com.azimbabu.javapersistence.ch08.mappingcollections.Constants;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.type.descriptor.java.LongJavaType;

@Entity
public class Item {

  @ElementCollection
  @CollectionTable(name = "IMAGE")
  @Column(name = "FILENAME")
  @GenericGenerator(
      name = "sequence_gen",
      //strategy = "sequence",  // deprecated, use type instead
      type = org.hibernate.id.enhanced.SequenceStyleGenerator.class
  )
  @org.hibernate.annotations.CollectionId(
      column = @Column(name = "IMAGE_ID"),
      generator = "sequence_gen"
  )
  @org.hibernate.annotations.CollectionIdJavaType(LongJavaType.class)
  private final Collection<String> images = new ArrayList<>();  // No BagImpl in JDK!
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

  public Collection<String> getImages() {
    return Collections.unmodifiableCollection(images);
  }

  public void addImage(String image) {
    images.add(image);
  }
}
