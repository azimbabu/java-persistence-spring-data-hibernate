package com.azimbabu.javapersistence.ch06.mappingvaluetypes.repository;

import com.azimbabu.javapersistence.ch06.mappingvaluetypes.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
