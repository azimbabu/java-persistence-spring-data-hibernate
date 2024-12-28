package com.azimbabu.javapersistence.repository;

import com.azimbabu.javapersistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

//@RepositoryRestResource(path = "users", exported = false)
@RepositoryRestResource(path = "users")
public interface UserRepository extends JpaRepository<User, Long> {

  @RestResource(exported = false)
  void deleteById(Long id);

  @Override
  @RestResource(exported = false)
  void delete(User user);
}
