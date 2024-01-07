package com.azimbabu.javapersistence.ch08.mappingassociations.onetomany.ondeletecascade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {

  @Query("select i from Item i inner join fetch i.bids where i.id = :id")
  Item findItemWithBids(@Param("id") Long id);
}
