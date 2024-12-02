package com.azimbabu.javapersistence.ch13.filtering.interceptor;

import com.azimbabu.javapersistence.ch13.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class AuditLogRecord {

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @NotNull
  private String message;

  @NotNull
  private Long entityId;

  @NotNull
  private Class<? extends Auditable> entityClass;

  @NotNull
  private Long userId;

  @NotNull
  private LocalDateTime createdOn = LocalDateTime.now();

  public AuditLogRecord() {
  }

  public AuditLogRecord(String message, Auditable entityInstance, Long userId) {
    this.message = message;
    this.entityId = entityInstance.getId();
    this.entityClass = entityInstance.getClass();
    this.userId = userId;
  }

  public Long getId() {
    return id;
  }

  public @NotNull String getMessage() {
    return message;
  }

  public @NotNull Long getEntityId() {
    return entityId;
  }

  public @NotNull Class<? extends Auditable> getEntityClass() {
    return entityClass;
  }

  public @NotNull Long getUserId() {
    return userId;
  }

  public @NotNull LocalDateTime getCreatedOn() {
    return createdOn;
  }
}
