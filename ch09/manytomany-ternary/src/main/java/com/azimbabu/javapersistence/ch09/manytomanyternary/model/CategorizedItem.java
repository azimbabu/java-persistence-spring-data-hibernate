package com.azimbabu.javapersistence.ch09.manytomanyternary.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class CategorizedItem {

  @Column(updatable = false)
  @NotNull  // Doesn't generate SQL constraint, so not part of the PK!
  private final LocalDateTime addedOn = LocalDateTime.now();
  @ManyToOne
  @JoinColumn(
      name = "ITEM_ID",
      nullable = false, updatable = false
  )
  private Item item;
  @ManyToOne
  @JoinColumn(
      name = "USER_ID",
      updatable = false
  )
  @NotNull  // Doesn't generate SQL constraint, so not part of the PK!
  private User addedBy;

  public CategorizedItem() {
  }

  public CategorizedItem(User addedBy, Item item) {
    this.item = item;
    this.addedBy = addedBy;
  }

  public Item getItem() {
    return item;
  }

  public User getAddedBy() {
    return addedBy;
  }

  public LocalDateTime getAddedOn() {
    return addedOn;
  }

  // Careful! Equality as shown here is not 'detached' safe!
  // Don't put detached instances into a HashSet! Or, if you
  // really have to compare detached instances, make sure they
  // were all loaded in the same persistence context.
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    // We are comparing instances by Java identity, not by primary key
    // equality. The scope where Java identity is the same as primary
    // key equality is the persistence context, not when instances are
    // in detached state!
    CategorizedItem that = (CategorizedItem) o;
    return Objects.equals(item, that.item) && Objects.equals(addedBy,
        that.addedBy) && Objects.equals(addedOn, that.addedOn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(item, addedBy, addedOn);
  }
}
