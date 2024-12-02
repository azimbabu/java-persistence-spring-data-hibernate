package com.azimbabu.javapersistence.ch13.filtering.callback;

import java.util.ArrayList;

public class Log extends ArrayList<String> {
  public static final Log INSTANCE = new Log();

  private Log() {
  }

  public void save(String message) {
    add(message);
  }
}
