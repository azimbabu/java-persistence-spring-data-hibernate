package com.azimbabu.javapersistence.ch05.subselect.repository;

import com.azimbabu.javapersistence.ch05.subselect.model.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {

}
