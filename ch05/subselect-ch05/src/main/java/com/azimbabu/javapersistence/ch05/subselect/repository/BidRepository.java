package com.azimbabu.javapersistence.ch05.subselect.repository;

import com.azimbabu.javapersistence.ch05.subselect.model.Bid;
import org.springframework.data.repository.CrudRepository;

public interface BidRepository extends CrudRepository<Bid, Long> {

}
