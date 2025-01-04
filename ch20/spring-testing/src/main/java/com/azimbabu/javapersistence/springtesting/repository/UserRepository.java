package com.azimbabu.javapersistence.springtesting.repository;

import com.azimbabu.javapersistence.springtesting.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
