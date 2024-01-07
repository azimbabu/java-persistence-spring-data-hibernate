package com.azimbabu.javapersistence.ch08.mappingassociations.onetomany.orphanremoval;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {

  Set<Bid> findByItem(Item item);
}
