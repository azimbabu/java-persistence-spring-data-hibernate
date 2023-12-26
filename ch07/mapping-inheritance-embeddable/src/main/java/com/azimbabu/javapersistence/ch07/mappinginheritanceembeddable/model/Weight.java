package com.azimbabu.javapersistence.ch07.mappinginheritanceembeddable.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Embeddable
@AttributeOverride(name = "name", column = @Column(name = "WEIGHT_NAME"))
@AttributeOverride(name = "symbol", column = @Column(name = "WEIGHT_SYMBOL"))
public class Weight extends Measurement {

  @NotNull
  @Column(name = "WEIGHT")
  private BigDecimal value;

  public Weight() {
  }

  public Weight(String name, String symbol, BigDecimal value) {
    super(name, symbol);
    this.value = value;
  }

  public static Weight kilograms(BigDecimal weight) {
    return new Weight("kilograms", "kg", weight);
  }

  public static Weight pounds(BigDecimal weight) {
    return new Weight("pounds", "lb", weight);
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }
}
