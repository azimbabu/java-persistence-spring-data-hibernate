package com.azimbabu.javapersistence.ch09.manytomanylinkentity.repository;

import com.azimbabu.javapersistence.ch09.manytomanylinkentity.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
