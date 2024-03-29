package com.azimbabu.javapersistence.ch09.manytomanylinkentity.repository;

import com.azimbabu.javapersistence.ch09.manytomanylinkentity.model.CategorizedItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorizedItemRepository extends
    JpaRepository<CategorizedItem, CategorizedItem.CategorizedItemId> {

}
