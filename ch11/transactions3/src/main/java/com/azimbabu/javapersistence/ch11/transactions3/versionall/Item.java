package com.azimbabu.javapersistence.ch11.transactions3.versionall;

import com.azimbabu.javapersistence.ch11.transactions3.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OptimisticLockType;

@Entity
@org.hibernate.annotations.OptimisticLocking(
    type = OptimisticLockType.ALL
)
@org.hibernate.annotations.DynamicUpdate
public class Item {

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
}
