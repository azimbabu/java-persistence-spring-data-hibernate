package com.azimbabu.javapersistence.ch06.mappingvaluetypes2.repository;

import com.azimbabu.javapersistence.ch06.mappingvaluetypes2.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
