package com.azimbabu.javapersistence.ch09.onetomanylist.repository;

import com.azimbabu.javapersistence.ch09.onetomanylist.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {
}
