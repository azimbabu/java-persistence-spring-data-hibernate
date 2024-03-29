package com.azimbabu.javapersistence.ch09.mapsmapkey.repository;

import com.azimbabu.javapersistence.ch09.mapsmapkey.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {

}
