package com.azimbabu.javapersistence.ch02.repository;

import com.azimbabu.javapersistence.ch02.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {

}
