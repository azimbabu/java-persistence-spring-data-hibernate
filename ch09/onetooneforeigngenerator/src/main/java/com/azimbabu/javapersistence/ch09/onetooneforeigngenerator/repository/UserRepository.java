package com.azimbabu.javapersistence.ch09.onetooneforeigngenerator.repository;

import com.azimbabu.javapersistence.ch09.onetooneforeigngenerator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
