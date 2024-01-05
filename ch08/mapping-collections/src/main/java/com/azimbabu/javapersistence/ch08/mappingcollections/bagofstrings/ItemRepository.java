package com.azimbabu.javapersistence.ch08.mappingcollections.bagofstrings;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {

  @Query("select i from Item i inner join fetch i.images where i.id = :id")
  Item findItemWithImages(@Param("id") Long id);

  @Query(value = "SELECT FILENAME FROM IMAGE WHERE ItEM_ID = ?1", nativeQuery = true)
  Collection<String> findImagesNative(Long id);
}
