package com.azimbabu.javapersistence.ch13.filtering.dynamic;

public class TestData {
  private final Long[] ids;

  public TestData(Long[] ids) {
    this.ids = ids;
  }

  public Long getFirstId() {
    return ids.length > 0 ? ids[0] : null;
  }

  public Long getLastId() {
    return ids.length > 0 ? ids[ids.length-1] : null;
  }

  public Long[] getIds() {
    return ids;
  }
}
