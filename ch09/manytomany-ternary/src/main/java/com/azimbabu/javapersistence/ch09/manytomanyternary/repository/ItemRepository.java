package com.azimbabu.javapersistence.ch09.manytomanyternary.repository;

import com.azimbabu.javapersistence.ch09.manytomanyternary.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
