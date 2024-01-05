package com.azimbabu.javapersistence.ch08.mappingcollections.mapofembeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Image {

  @Column(nullable = true)  // Can be null, not part of PK!
  private String title;
  private int width;
  private int height;

  public Image() {
  }

  public Image(String title, int width, int height) {
    this.title = title;
    this.width = width;
    this.height = height;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String filename) {
    this.title = filename;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Image image = (Image) o;
    return width == image.width && height == image.height && Objects.equals(title,
        image.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, width, height);
  }
}
