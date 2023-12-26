package com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Entity
public class BankAccount extends BillingDetails {

  private static final Logger LOGGER = LogManager.getLogger(BankAccount.class);

  @NotNull
  private String account;

  @NotNull
  private String bankName;

  @NotNull
  private String swift;

  public BankAccount() {
  }

  public BankAccount(String owner, String account, String bankName, String swift) {
    super(owner);
    this.account = account;
    this.bankName = bankName;
    this.swift = swift;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getSwift() {
    return swift;
  }

  public void setSwift(String swift) {
    this.swift = swift;
  }

  @Override
  public void pay(BigDecimal amount) {
    LOGGER.info("Paying from a bank account, amount={}", amount);
  }
}
