package com.azimbabu.javapersistence.ch08.mappingcollections.bagofembeddables;

import com.azimbabu.javapersistence.ch08.mappingcollections.Constants;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.type.descriptor.java.LongJavaType;

@Entity
public class Item {

  @ElementCollection
  @CollectionTable(name = "IMAGE")
  @GenericGenerator(
      name = "sequence_gen",
      //strategy = "sequence",  // deprecated, use type instead
      type = org.hibernate.id.enhanced.SequenceStyleGenerator.class
  )
  @org.hibernate.annotations.CollectionId(  // Surrogate PK allows duplicates!
      column = @Column(name = "IMAGE_ID"),
      generator = "sequence_gen"
  )
  @org.hibernate.annotations.CollectionIdJavaType(LongJavaType.class)
  private final Collection<Image> images = new ArrayList<>();
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

  public Collection<Image> getImages() {
    return Collections.unmodifiableCollection(images);
  }

  public void addImage(Image image) {
    images.add(image);
  }
}
