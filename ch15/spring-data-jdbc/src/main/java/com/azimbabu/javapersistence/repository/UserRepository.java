package com.azimbabu.javapersistence.repository;

import com.azimbabu.javapersistence.hibernateogm.model.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  @Override
  List<User> findAll();
}
