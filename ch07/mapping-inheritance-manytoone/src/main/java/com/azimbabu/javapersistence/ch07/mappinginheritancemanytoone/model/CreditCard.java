package com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Entity
@PrimaryKeyJoinColumn(name = "CREDITCARD_ID")
public class CreditCard extends BillingDetails {

  private static final Logger LOGGER = LogManager.getLogger(CreditCard.class);
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

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public String getExpMonth() {
    return expMonth;
  }

  public void setExpMonth(String expMonth) {
    this.expMonth = expMonth;
  }

  public String getExpYear() {
    return expYear;
  }

  public void setExpYear(String expYear) {
    this.expYear = expYear;
  }

  @Override
  public void pay(BigDecimal amount) {
    LOGGER.info("Paying from a credit card, amount={}", amount);
  }
}
