package com.azimbabu.javapersistence.ch05.subselect.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@org.hibernate.annotations.Immutable
@org.hibernate.annotations.Subselect(
    value = "select i.ID as ITEMID, i.NAME as NAME, " +
        "count(b.ID)as NUMBEROFBIDS " +
        "from ITEM i left outer join BID b on i.ID = b.ITEM_ID " +
        "group by i.ID, i.NAME"
)
@org.hibernate.annotations.Synchronize({"ITEM", "BID"})
public class ItemBidSummary {

  @Id
  private Long itemId;

  private String name;

  private long numberOfBids;

  public Long getItemId() {
    return itemId;
  }

  public String getName() {
    return name;
  }

  public long getNumberOfBids() {
    return numberOfBids;
  }
}
