package com.azimbabu.javapersistence.springtesting.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Log {

  @Id
  @GeneratedValue
  @Getter
  private Long id;

  @NotNull
  @Size(
      min = 2,
      max = 255,
      message = "Info is required, maximum 255 characters."
  )
  @Getter
  @Setter
  private String info;

  public Log(String info) {
    this.info = info;
  }
}
