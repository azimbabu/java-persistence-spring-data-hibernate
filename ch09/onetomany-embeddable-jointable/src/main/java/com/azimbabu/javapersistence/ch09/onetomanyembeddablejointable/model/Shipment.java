package com.azimbabu.javapersistence.ch09.onetomanyembeddablejointable.model;

import com.azimbabu.javapersistence.ch09.onetomanyembeddablejointable.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Shipment {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  //  @NotNull  // cannot be not null
  @CreationTimestamp
  private LocalDateTime createdOn;

  public Shipment() {
  }

  public Long getId() {
    return id;
  }

  public LocalDateTime getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(LocalDateTime createdOn) {
    this.createdOn = createdOn;
  }
}
