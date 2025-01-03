package com.azimbabu.javapersistence.querydsl.model;

import com.azimbabu.javapersistence.querydsl.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Bid {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  @Getter
  private Long id;

  @Getter
  @Setter
  private BigDecimal amount;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @Getter
  @Setter
  private User user;

  public Bid(BigDecimal amount) {
    this.amount = amount;
  }
}
