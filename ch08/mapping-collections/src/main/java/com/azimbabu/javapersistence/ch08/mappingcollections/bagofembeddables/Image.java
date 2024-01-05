package com.azimbabu.javapersistence.ch08.mappingcollections.bagofembeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Image {

  @Column(nullable = true)
  private String title;
  @Column(nullable = false)
  private String filename;
  private int width;
  private int height;

  public Image() {
  }

  public Image(String title, String filename, int width, int height) {
    this.title = title;
    this.filename = filename;
    this.width = width;
    this.height = height;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
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
        image.title) && Objects.equals(filename, image.filename);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, filename, width, height);
  }
}
