package com.azimbabu.javapersistence.ch09.manytomanybidirectional.repository;

import com.azimbabu.javapersistence.ch09.manytomanybidirectional.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
