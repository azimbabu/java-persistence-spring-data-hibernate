package com.azimbabu.javapersistence.ch09.mapsmapkey.repository;

import com.azimbabu.javapersistence.ch09.mapsmapkey.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
