package com.azimbabu.javapersistence.ch14.dao;

import com.azimbabu.javapersistence.ch14.Bid;
import java.math.BigDecimal;
import java.util.List;

public interface BidDao {

  Bid getBydId(long id);

  List<Bid> getAll();

  void insert(Bid bid);

  void update(long id, BigDecimal amount);

  void delete(Bid bid);

  List<Bid> findByAmount(BigDecimal amount);
}
