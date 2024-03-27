package com.azimbabu.javapersistence.ch09.onetomanyjointable.repository;

import com.azimbabu.javapersistence.ch09.onetomanyjointable.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
