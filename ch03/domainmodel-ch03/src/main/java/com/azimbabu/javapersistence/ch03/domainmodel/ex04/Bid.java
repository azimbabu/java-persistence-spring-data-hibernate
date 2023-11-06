package com.azimbabu.javapersistence.ch03.domainmodel.ex04;

public class Bid {

  private Item item;

  public Bid(Item item) {
    this.item = item;
    item.bids.add(this);
  }

  public Item getItem() {
    return item;
  }
}
