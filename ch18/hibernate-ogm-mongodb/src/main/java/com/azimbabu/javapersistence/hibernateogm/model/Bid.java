package com.azimbabu.javapersistence.hibernateogm.model;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@NoArgsConstructor
public class Bid {

  @Id
  @GeneratedValue(generator = "ID_GENERATOR")
  @GenericGenerator(name = "ID_GENERATOR", strategy = "uuid2")
  @Getter
  private String id;

  @Getter
  @Setter
  private BigDecimal amount;

  @ManyToOne
  @Getter
  @Setter
  private User user;

  @ManyToOne
  @Getter
  @Setter
  private Item item;

  public Bid(BigDecimal amount) {
    this.amount = amount;
  }
}
