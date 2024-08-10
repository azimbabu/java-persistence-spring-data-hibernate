package com.azimbabu.javapersistence.ch11.transactions.concurrency;

import java.util.Collections;
import java.util.List;

public class TestData {
  private final List<Long> categoryIds;
  private final List<Long> itemIds;

  public TestData(List<Long> categoryIds, List<Long> itemIds) {
    this.categoryIds = categoryIds;
    this.itemIds = itemIds;
  }

  public List<Long> getCategoryIds() {
    return Collections.unmodifiableList(categoryIds);
  }

  public List<Long> getItemIds() {
    return Collections.unmodifiableList(itemIds);
  }
}
