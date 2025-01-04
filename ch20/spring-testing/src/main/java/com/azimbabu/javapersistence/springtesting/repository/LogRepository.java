package com.azimbabu.javapersistence.springtesting.repository;

import com.azimbabu.javapersistence.springtesting.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {

}
