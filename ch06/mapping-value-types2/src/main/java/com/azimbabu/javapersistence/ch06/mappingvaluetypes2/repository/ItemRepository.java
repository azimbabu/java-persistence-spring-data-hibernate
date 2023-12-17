package com.azimbabu.javapersistence.ch06.mappingvaluetypes2.repository;

import com.azimbabu.javapersistence.ch06.mappingvaluetypes2.model.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {

  Iterable<Item> findByMetricWeight(double weight);
}
