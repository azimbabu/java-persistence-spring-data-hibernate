package com.azimbabu.javapersistence.repository;

import com.azimbabu.javapersistence.hibernateogm.model.User;
import com.azimbabu.javapersistence.hibernateogm.model.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "users", excerptProjection = UserProjection.class)
public interface UserRepository extends JpaRepository<User, Long> {
}
