package com.azimbabu.javapersistence.ch06.mappingvaluetypes.model;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.generator.EventType;

@Entity
public class Item {

  @Id
  @GeneratedValue(generator = "ID_GENERATOR")
  private Long id;

  @Access(AccessType.PROPERTY)
  @Column(name = "ITEM_NAME")
  private String name;

  @NotNull
  @Basic(fetch = FetchType.LAZY)  // Defaults to EAGER
  private String description;

  //@Formula("CONCAT(SUBSTR(DESCRIPTION, 1, 12), '...')")
  @Formula(
      "CONCAT(SUBSTR(DESCRIPTION, 1, 12), '...')"
  )
  private String shortDescription;

  @NotNull
  @Enumerated(EnumType.STRING)  // Defaults to ORDINAL
  private AuctionType auctionType = AuctionType.HIGHTEST_BID;

  @Formula("(SELECT AVG(B.AMOUNT) FROM BID B WHERE B.ITEM_ID = ID)")
  private BigDecimal averageBidAmount;

  @Column(name = "IMPERIALWEIGHT")
  @ColumnTransformer(
      read = "IMPERIALWEIGHT / 2.20462",
      write = "? * 2.20462"
  )
  private double metricWeight;

  @Column(insertable = false)
  @ColumnDefault("1.00")
  //@Generated(GenerationTime.INSERT) // GenerationTime.INSERT is deprecated
  @Generated(event = EventType.INSERT)
  private BigDecimal initialPrice;

  @OneToMany(
      mappedBy = "item",
      cascade = CascadeType.PERSIST,
      orphanRemoval = true
  )
  private Set<Bid> bids = new HashSet<>();

  @CreationTimestamp
  private LocalDate createdOn;

  @UpdateTimestamp
  private LocalDateTime lastModified;

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = !name.startsWith("AUCTION: ") ? "AUCTION: " + name : name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public BigDecimal getAverageBidAmount() {
    return averageBidAmount;
  }

  public double getMetricWeight() {
    return metricWeight;
  }

  public void setMetricWeight(double metricWeight) {
    this.metricWeight = metricWeight;
  }

  public Set<Bid> getBids() {
    return Collections.unmodifiableSet(bids);
  }

  public AuctionType getAuctionType() {
    return auctionType;
  }

  public void setAuctionType(
      AuctionType auctionType) {
    this.auctionType = auctionType;
  }

  public BigDecimal getInitialPrice() {
    return initialPrice;
  }

  public LocalDate getCreatedOn() {
    return createdOn;
  }

  public LocalDateTime getLastModified() {
    return lastModified;
  }
}
