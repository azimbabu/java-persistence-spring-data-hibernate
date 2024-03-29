package com.azimbabu.javapersistence.ch09.mapsternary.repository;

import com.azimbabu.javapersistence.ch09.mapsternary.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
