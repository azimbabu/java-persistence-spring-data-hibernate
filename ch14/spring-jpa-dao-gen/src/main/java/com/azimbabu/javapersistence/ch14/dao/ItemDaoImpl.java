package com.azimbabu.javapersistence.ch14.dao;

import com.azimbabu.javapersistence.ch14.Bid;
import com.azimbabu.javapersistence.ch14.Item;

public class ItemDaoImpl extends AbstractGenericDao<Item> {
  public ItemDaoImpl() {
    super(Item.class);
  }

  @Override
  public void insert(Item item) {
    em.persist(item);
    for (Bid bid : item.getBids()) {
      em.persist(bid);
    }
  }

  @Override
  public void delete(Item item) {
    for (Bid bid : item.getBids()) {
      em.remove(bid);
    }
    em.remove(item);
  }
}
