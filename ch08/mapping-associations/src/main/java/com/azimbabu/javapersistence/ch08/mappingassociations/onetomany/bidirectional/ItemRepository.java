package com.azimbabu.javapersistence.ch08.mappingassociations.onetomany.bidirectional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
