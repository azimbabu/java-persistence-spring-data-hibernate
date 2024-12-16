package com.azimbabu.javapersistence.ch14.dao;

import com.azimbabu.javapersistence.ch14.Bid;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BidDaoImpl implements BidDao {

  @Autowired
  private SessionFactory sessionFactory;

  @Override
  public Bid getBydId(long id) {
    return sessionFactory.getCurrentSession().get(Bid.class, id);
  }

  @Override
  public List<Bid> getAll() {
    return sessionFactory.getCurrentSession().createQuery("from Bid", Bid.class).list();
  }

  @Override
  public void insert(Bid bid) {
    sessionFactory.getCurrentSession().persist(bid);
  }

  @Override
  public void update(long id, BigDecimal amount) {
    Bid bid = sessionFactory.getCurrentSession().get(Bid.class, id);
    bid.setAmount(amount);
    sessionFactory.getCurrentSession().update(bid);
//    sessionFactory.getCurrentSession().merge(bid);
  }

  @Override
  public void delete(Bid bid) {
    sessionFactory.getCurrentSession().remove(bid);
  }

  @Override
  public List<Bid> findByAmount(BigDecimal amount) {
    return sessionFactory.getCurrentSession().createQuery("from Bid where amount = :amount", Bid.class)
        .setParameter("amount", amount)
        .list();
  }
}
