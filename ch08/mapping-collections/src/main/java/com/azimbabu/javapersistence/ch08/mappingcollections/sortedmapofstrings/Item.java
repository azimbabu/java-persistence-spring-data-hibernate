package com.azimbabu.javapersistence.ch08.mappingcollections.sortedmapofstrings;

import com.azimbabu.javapersistence.ch08.mappingcollections.Constants;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

@Entity
public class Item {

  @ElementCollection
  @CollectionTable(name = "IMAGE")
  @MapKeyColumn(name = "FILENAME")
  @Column(name = "IMAGENAME")
  @org.hibernate.annotations.SortComparator(ReverseStringComparator.class)
  private final SortedMap<String, String> images = new TreeMap<>();
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

  public SortedMap<String, String> getImages() {
    return Collections.unmodifiableSortedMap(images);
  }

  public void addImage(String fileName, String imageName) {
    images.put(fileName, imageName);
  }

  public static class ReverseStringComparator implements Comparator<String> {

    @Override
    public int compare(String s1, String s2) {
      return s2.compareTo(s1);  // descending order
    }
  }
}
