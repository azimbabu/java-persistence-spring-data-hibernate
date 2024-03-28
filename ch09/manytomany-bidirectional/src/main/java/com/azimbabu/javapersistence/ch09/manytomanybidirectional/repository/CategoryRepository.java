package com.azimbabu.javapersistence.ch09.manytomanybidirectional.repository;

import com.azimbabu.javapersistence.ch09.manytomanybidirectional.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
