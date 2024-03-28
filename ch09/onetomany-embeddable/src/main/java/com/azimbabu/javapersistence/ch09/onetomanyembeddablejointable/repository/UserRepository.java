package com.azimbabu.javapersistence.ch09.onetomanyembeddablejointable.repository;

import com.azimbabu.javapersistence.ch09.onetomanyembeddablejointable.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
