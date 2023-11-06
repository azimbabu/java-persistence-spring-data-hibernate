package com.azimbabu.javapersistence.ch04.springdatajpa.repository;

import com.azimbabu.javapersistence.ch04.springdatajpa.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
