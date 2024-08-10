package com.azimbabu.javapersistence.ch11.transactions2.timestamp;

import com.azimbabu.javapersistence.ch11.transactions2.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Item {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @Version
  private LocalDateTime lastUpdated;

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

  public LocalDateTime getLastUpdated() {
    return lastUpdated;
  }
}
