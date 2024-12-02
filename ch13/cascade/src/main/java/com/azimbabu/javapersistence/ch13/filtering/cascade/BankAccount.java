package com.azimbabu.javapersistence.ch13.filtering.cascade;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class BankAccount extends BillingDetails {

  @NotNull
  private String account;

  @NotNull
  private String bankName;

  @NotNull
  private String swiftCode;

  public BankAccount() {
  }

  public BankAccount(String owner, String account, String bankName, String swiftCode) {
    super(owner);
    this.account = account;
    this.bankName = bankName;
    this.swiftCode = swiftCode;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(@NotNull String account) {
    this.account = account;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(@NotNull String bankName) {
    this.bankName = bankName;
  }

  public String getSwiftCode() {
    return swiftCode;
  }

  public void setSwiftCode(@NotNull String swiftCode) {
    this.swiftCode = swiftCode;
  }
}
