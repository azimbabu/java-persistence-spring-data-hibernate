package com.azimbabu.javapersistence.ch09.onetoonejointable.repository;

import com.azimbabu.javapersistence.ch09.onetoonejointable.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
