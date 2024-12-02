package com.azimbabu.javapersistence.ch13.filtering.cascade;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class CreditCard extends BillingDetails {

  @NotNull
  private String cardNumber;

  @NotNull
  private String expMonth;

  @NotNull
  private String expYear;

  public CreditCard() {
  }

  public CreditCard(String owner, String cardNumber, String expMonth, String expYear) {
    super(owner);
    this.cardNumber = cardNumber;
    this.expMonth = expMonth;
    this.expYear = expYear;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(@NotNull String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public String getExpMonth() {
    return expMonth;
  }

  public void setExpMonth(@NotNull String expMonth) {
    this.expMonth = expMonth;
  }

  public String getExpYear() {
    return expYear;
  }

  public void setExpYear(@NotNull String expYear) {
    this.expYear = expYear;
  }
}
