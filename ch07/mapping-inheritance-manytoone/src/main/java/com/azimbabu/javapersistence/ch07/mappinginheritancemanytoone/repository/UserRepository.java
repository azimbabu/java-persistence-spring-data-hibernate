package com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone.repository;

import com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
