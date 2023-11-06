package com.azimbabu.javapersistence.ch05.generator.repository;

import com.azimbabu.javapersistence.ch05.generator.model.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {

}
