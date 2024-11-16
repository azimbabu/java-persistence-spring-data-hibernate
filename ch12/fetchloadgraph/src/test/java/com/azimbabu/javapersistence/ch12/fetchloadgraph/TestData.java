package com.azimbabu.javapersistence.ch12.fetchloadgraph;

public class TestData {

  private final Long[] itemIds;
  private final Long[] userIds;
  private final Long[] bidIds;

  public TestData(Long[] itemIds, Long[] userIds, Long[] bidIds) {
    this.itemIds = itemIds;
    this.userIds = userIds;
    this.bidIds = bidIds;
  }

  public Long[] getItemIds() {
    return itemIds;
  }

  public Long[] getUserIds() {
    return userIds;
  }

  public Long[] getBidIds() {
    return bidIds;
  }
}
