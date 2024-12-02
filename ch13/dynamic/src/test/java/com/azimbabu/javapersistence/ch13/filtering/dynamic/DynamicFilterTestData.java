package com.azimbabu.javapersistence.ch13.filtering.dynamic;

public class DynamicFilterTestData {
  private final TestData categories;
  private final TestData items;
  private final TestData users;

  public DynamicFilterTestData(TestData categories, TestData items, TestData users) {
    this.categories = categories;
    this.items = items;
    this.users = users;
  }

  public TestData getCategories() {
    return categories;
  }

  public TestData getItems() {
    return items;
  }

  public TestData getUsers() {
    return users;
  }
}
