package com.azimbabu.javapersistence.ch14.dao;

import com.azimbabu.javapersistence.ch14.Bid;
import com.azimbabu.javapersistence.ch14.Item;

public class ItemDaoImpl extends AbstractGenericDao<Item> {
  public ItemDaoImpl() {
    super(Item.class);
  }

  @Override
  public void insert(Item item) {
    sessionFactory.getCurrentSession().persist(item);
    for (Bid bid : item.getBids()) {
      sessionFactory.getCurrentSession().persist(bid);
    }
  }

  @Override
  public void delete(Item item) {
    sessionFactory.getCurrentSession().createQuery("delete from Bid b where b.item.id = :id")
        .setParameter("id", item.getId())
        .executeUpdate();
    sessionFactory.getCurrentSession().createQuery("delete from Item i where i.id = :id")
        .setParameter("id", item.getId())
        .executeUpdate();
  }
}
