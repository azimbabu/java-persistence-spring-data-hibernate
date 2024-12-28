package com.azimbabu.javapersistence.repository;

import com.azimbabu.javapersistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
