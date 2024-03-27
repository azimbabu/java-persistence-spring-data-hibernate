package com.azimbabu.javapersistence.ch09.onetomanyjointable.model;

import com.azimbabu.javapersistence.ch09.onetomanyjointable.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Item {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @NotNull
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinTable(
      name = "ITEM_BUYER",
      joinColumns = @JoinColumn(name = "ITEM_ID"),  // Defaults to ID
      inverseJoinColumns = @JoinColumn(nullable = false)  // Defaults to BUYER_ID
  )
  private User buyer;

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

  public User getBuyer() {
    return buyer;
  }

  public void setBuyer(User buyer) {
    this.buyer = buyer;
  }
}
