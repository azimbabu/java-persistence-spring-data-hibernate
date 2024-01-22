package com.azimbabu.javapersistence.ch09.onetoonejointable.model;

import com.azimbabu.javapersistence.ch09.onetoonejointable.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Shipment {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  //  @NotNull
  @CreationTimestamp
  private LocalDateTime createdOn;

  @NotNull
  @Enumerated(EnumType.STRING)
  private ShipmentState shipmentState = ShipmentState.TRANSIT;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinTable(
      name = "ITEM_SHIPMENT",  // Required!
      joinColumns = @JoinColumn(name = "SHIPMENT_ID"),  // Defaults to ID
      inverseJoinColumns = @JoinColumn(
          name = "ITEM_ID", // // Defaults to AUCTION_ID
          nullable = false,
          unique = true
      )
  )
  private Item auction;

  public Shipment() {
  }

  public Shipment(Item auction) {
    this.auction = auction;
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

  public ShipmentState getShipmentState() {
    return shipmentState;
  }

  public void setShipmentState(
      ShipmentState shipmentState) {
    this.shipmentState = shipmentState;
  }

  public Item getAuction() {
    return auction;
  }

  public void setAuction(Item auction) {
    this.auction = auction;
  }
}
