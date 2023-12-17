package com.azimbabu.javapersistence.ch06.mappingvaluetypes3.repository;

import com.azimbabu.javapersistence.ch06.mappingvaluetypes3.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
