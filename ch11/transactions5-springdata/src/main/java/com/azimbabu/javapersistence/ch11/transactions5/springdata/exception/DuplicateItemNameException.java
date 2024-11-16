package com.azimbabu.javapersistence.ch11.transactions5.springdata.exception;

public class DuplicateItemNameException extends RuntimeException {

  public DuplicateItemNameException(String message) {
    super(message);
  }
}
