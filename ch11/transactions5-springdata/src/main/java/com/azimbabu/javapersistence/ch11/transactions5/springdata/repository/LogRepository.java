package com.azimbabu.javapersistence.ch11.transactions5.springdata.repository;

import com.azimbabu.javapersistence.ch11.transactions5.springdata.concurrency.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long>, LogRepositoryCustom {

}
