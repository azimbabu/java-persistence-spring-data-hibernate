package com.azimbabu.javapersistence.ch11.transactions5.springdata.repository;

public interface LogRepositoryCustom {

  void log(String message);

  void showLogs();

  void addSeparateLogsNotSupported();

  void addSeparateLogsSupports();
}
