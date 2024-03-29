package com.azimbabu.javapersistence.ch09.manytomanylinkentity.repository;

import com.azimbabu.javapersistence.ch09.manytomanylinkentity.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
