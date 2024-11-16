package com.azimbabu.javapersistence.ch12.eagerselect;

public class TestData {

  private final Long[] itemIds;
  private final Long[] userIds;

  public TestData(Long[] itemIds, Long[] userIds) {
    this.itemIds = itemIds;
    this.userIds = userIds;
  }

  public Long[] getItemIds() {
    return itemIds;
  }

  public Long[] getUserIds() {
    return userIds;
  }
}
