package com.azimbabu.javapersistence.ch08.mappingcollections.sortedsetofstrings;

import java.util.SortedSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {

  @Query("select i from Item i inner join fetch i.images where i.id = :id")
  Item findItemWithImages(@Param("id") Long id);

  @Query(value = "select FILENAME from IMAGE where ITEM_ID = ?1", nativeQuery = true)
  SortedSet<String> findImagesNative(Long id);
}
