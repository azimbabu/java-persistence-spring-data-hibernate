package com.azimbabu.javapersistence.ch09.onetomanyembeddable.repository;

import com.azimbabu.javapersistence.ch09.onetomanyembeddable.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
