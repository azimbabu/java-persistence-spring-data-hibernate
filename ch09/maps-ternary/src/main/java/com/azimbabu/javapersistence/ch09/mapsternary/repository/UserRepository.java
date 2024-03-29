package com.azimbabu.javapersistence.ch09.mapsternary.repository;

import com.azimbabu.javapersistence.ch09.mapsternary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
