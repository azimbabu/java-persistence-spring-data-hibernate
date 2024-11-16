package com.azimbabu.javapersistence.ch11.transactions5.springdata.concurrency;

import com.azimbabu.javapersistence.ch11.transactions5.springdata.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Log {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  private String message;

  private LocalDateTime dateTime;

  public Log() {
  }

  public Log(String message) {
    this.message = message;
    this.dateTime = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public String getMessage() {
    return message;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  @Override
  public String toString() {
    return "Log{" +
        "id=" + id +
        ", message='" + message + '\'' +
        ", dateTime=" + dateTime +
        '}';
  }
}
