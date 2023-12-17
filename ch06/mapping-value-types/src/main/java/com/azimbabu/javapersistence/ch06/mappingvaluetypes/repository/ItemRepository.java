package com.azimbabu.javapersistence.ch06.mappingvaluetypes.repository;

import com.azimbabu.javapersistence.ch06.mappingvaluetypes.model.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {

}
