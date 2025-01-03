package com.azimbabu.javapersistence.querydsl.repository;

import com.azimbabu.javapersistence.querydsl.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
