package com.azimbabu.javapersistence.ch07.mappinginheritanceembeddable.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Embeddable
@AttributeOverride(name = "name", column = @Column(name = "DIMENSIONS_NAME"))
@AttributeOverride(name = "symbol", column = @Column(name = "DIMENSIONS_SYMBOL"))
public class Dimensions extends Measurement {

  @NotNull
  private BigDecimal depth;
  @NotNull
  private BigDecimal height;
  @NotNull
  private BigDecimal width;

  public Dimensions() {
  }

  public Dimensions(String name, String symbol, BigDecimal depth, BigDecimal height,
      BigDecimal width) {
    super(name, symbol);
    this.depth = depth;
    this.height = height;
    this.width = width;
  }

  public static Dimensions centimeters(BigDecimal depth, BigDecimal height, BigDecimal width) {
    return new Dimensions("centimeters", "cm", depth, height, width);
  }

  public static Dimensions inches(BigDecimal depth, BigDecimal height, BigDecimal width) {
    return new Dimensions("inches", "\"", depth, height, width);
  }

  public BigDecimal getDepth() {
    return depth;
  }

  public void setDepth(BigDecimal depth) {
    this.depth = depth;
  }

  public BigDecimal getHeight() {
    return height;
  }

  public void setHeight(BigDecimal height) {
    this.height = height;
  }

  public BigDecimal getWidth() {
    return width;
  }

  public void setWidth(BigDecimal width) {
    this.width = width;
  }
}
