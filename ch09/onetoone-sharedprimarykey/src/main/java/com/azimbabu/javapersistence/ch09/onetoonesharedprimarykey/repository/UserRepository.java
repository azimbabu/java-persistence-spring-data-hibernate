package com.azimbabu.javapersistence.ch09.onetoonesharedprimarykey.repository;

import com.azimbabu.javapersistence.ch09.onetoonesharedprimarykey.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
