package com.azimbabu.javapersistence.ch09.onetomanybag.repository;

import com.azimbabu.javapersistence.ch09.onetomanybag.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {

}
