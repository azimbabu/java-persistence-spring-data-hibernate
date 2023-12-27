package com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.repository;

import com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
  // using a join fetch directive to avoid LazyInitializationException
  // https://vladmihalcea.com/the-best-way-to-handle-the-lazyinitializationexception/
  @Query("select u from User u join fetch u.billingDetails")
  List<User> findAll();
}
