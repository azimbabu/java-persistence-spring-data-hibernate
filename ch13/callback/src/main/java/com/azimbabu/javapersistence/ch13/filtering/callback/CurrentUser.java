package com.azimbabu.javapersistence.ch13.filtering.callback;

public class CurrentUser extends ThreadLocal<User> {

  public static CurrentUser INSTANCE = new CurrentUser();

  private CurrentUser() {
  }
}
