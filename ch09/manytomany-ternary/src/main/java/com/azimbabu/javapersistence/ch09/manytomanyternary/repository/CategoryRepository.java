package com.azimbabu.javapersistence.ch09.manytomanyternary.repository;

import com.azimbabu.javapersistence.ch09.manytomanyternary.model.Category;
import com.azimbabu.javapersistence.ch09.manytomanyternary.model.Item;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  @Query("select c from Category c join c.categorizedItems ci where ci.item = :itemParameter")
  List<Category> findCategoryWithCategorizedItems(@Param("itemParameter") Item itemParameter);
}
