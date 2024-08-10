package com.azimbabu.javapersistence.ch11.transactions.concurrency;

public class InvalidBidException extends RuntimeException {
  public InvalidBidException(String message) {
    super(message);
  }
}
