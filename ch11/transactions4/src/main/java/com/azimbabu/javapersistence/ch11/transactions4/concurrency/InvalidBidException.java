package com.azimbabu.javapersistence.ch11.transactions4.concurrency;

public class InvalidBidException extends RuntimeException {
  public InvalidBidException(String message) {
    super(message);
  }
}
