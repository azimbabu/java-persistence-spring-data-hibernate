package com.azimbabu.javapersistence.ch09.onetooneforeignkey.repository;

import com.azimbabu.javapersistence.ch09.onetooneforeignkey.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

  @Query("select u from User u inner join fetch u.shippingAddress where u.id = :id")
  User findUserWithAddress(@Param("id") Long id);
}
