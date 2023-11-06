package com.azimbabu.javapersistence.ch05.mapping.repository;

import com.azimbabu.javapersistence.ch05.mapping.model.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {

}
