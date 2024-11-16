package com.azimbabu.javapersistence.ch11.transactions5.springdata.repository;

import java.time.LocalDateTime;

public interface ItemRepositoryCustom {

  void addItem(String name, LocalDateTime createdAt);

  void checkDuplicateName(String name);

  void addLogs();

  void showLogs();

  void addItemNoRollback(String name, LocalDateTime createdAt);
}
