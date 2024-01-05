package com.azimbabu.javapersistence.ch08.mappingcollections.embeddablesetofstrings;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

  @Query("select u from User u inner join fetch u.address.contacts")
  List<User> findAll();
}
