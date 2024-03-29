package com.azimbabu.javapersistence.ch09.manytomanyternary.repository;

import com.azimbabu.javapersistence.ch09.manytomanyternary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
