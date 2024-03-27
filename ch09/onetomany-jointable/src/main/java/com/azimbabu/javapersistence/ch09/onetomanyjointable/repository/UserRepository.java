package com.azimbabu.javapersistence.ch09.onetomanyjointable.repository;

import com.azimbabu.javapersistence.ch09.onetomanyjointable.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
