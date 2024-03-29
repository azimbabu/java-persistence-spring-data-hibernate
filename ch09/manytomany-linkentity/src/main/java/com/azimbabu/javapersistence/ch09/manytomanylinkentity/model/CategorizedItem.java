package com.azimbabu.javapersistence.ch09.manytomanylinkentity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "CATEGORY_ITEM")
@Immutable
public class CategorizedItem {

  @EmbeddedId
  private final CategorizedItemId id = new CategorizedItemId();
  @Column(updatable = false)
  @NotNull
  private String addedBy;
  @Column(updatable = false)
  //@NotNull
  @CreationTimestamp
  private LocalDateTime addedOn;
  @ManyToOne
  @JoinColumn(
      name = "CATEGORY_ID",
      insertable = false, updatable = false
  )
  private Category category;
  @ManyToOne
  @JoinColumn(
      name = "CATEGORY_ID",
      insertable = false, updatable = false
  )
  private Item item;

  public CategorizedItem() {
  }

  public CategorizedItem(String addedBy, Category category, Item item) {
    // Set fields
    this.addedBy = addedBy;
    this.category = category;
    this.item = item;

    // set identifier values
    this.id.categoryId = category.getId();
    this.id.itemId = item.getId();

    // Guarantee referential integrity if made bidirectional
    category.addCategorizedItem(this);
    item.addCategorizedItem(this);
  }

  public CategorizedItemId getId() {
    return id;
  }

  public String getAddedBy() {
    return addedBy;
  }

  public LocalDateTime getAddedOn() {
    return addedOn;
  }

  public Category getCategory() {
    return category;
  }

  public Item getItem() {
    return item;
  }

  @Embeddable
  public static class CategorizedItemId implements Serializable {

    @Column(name = "CATEGORY_ID")
    private Long categoryId;

    @Column(name = "ITEM_ID")
    private Long itemId;

    public CategorizedItemId() {
    }

    public CategorizedItemId(Long categoryId, Long itemId) {
      this.categoryId = categoryId;
      this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      CategorizedItemId that = (CategorizedItemId) o;
      return Objects.equals(categoryId, that.categoryId) && Objects.equals(itemId,
          that.itemId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(categoryId, itemId);
    }
  }
}
