package com.azimbabu.javapersistence.ch07.mappinginheritancemixed.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SecondaryTable;
import jakarta.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("CC")
@SecondaryTable(name = "CREDITCARD", pkJoinColumns = @PrimaryKeyJoinColumn(name = "CREDITCARD_ID"))
public class CreditCard extends BillingDetails {
  @NotNull  // Ignored by JPA for DDL, strategy is SINGLE_TABLE!
  @Column(table = "CREDITCARD", nullable = false)
  private String cardNumber;

  @NotNull  // Ignored by JPA for DDL, strategy is SINGLE_TABLE!
  @Column(table = "CREDITCARD", nullable = false)
  private String expMonth;

  @NotNull  // Ignored by JPA for DDL, strategy is SINGLE_TABLE!
  @Column(table = "CREDITCARD", nullable = false)
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
}
