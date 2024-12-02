package com.azimbabu.javapersistence.ch13.filtering.callback;

import com.azimbabu.javapersistence.ch13.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
@EntityListeners(
    PersistEntityListener.class
)
public class Item {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @NotNull
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SELLER_ID", nullable = false)
  private User seller;

  public Item() {
  }

  public Item(String name, User seller) {
    this.name = name;
    this.seller = seller;
  }

  public Long getId() {
    return id;
  }

  public @NotNull String getName() {
    return name;
  }

  public void setName(@NotNull String name) {
    this.name = name;
  }

  public User getSeller() {
    return seller;
  }

  public void setSeller(User seller) {
    this.seller = seller;
  }
}
