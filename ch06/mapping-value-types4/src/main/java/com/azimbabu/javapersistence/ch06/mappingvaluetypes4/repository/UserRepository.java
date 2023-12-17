package com.azimbabu.javapersistence.ch06.mappingvaluetypes4.repository;

import com.azimbabu.javapersistence.ch06.mappingvaluetypes4.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
