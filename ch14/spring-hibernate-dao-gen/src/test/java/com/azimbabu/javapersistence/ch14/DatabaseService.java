package com.azimbabu.javapersistence.ch14;

import com.azimbabu.javapersistence.ch14.dao.GenericDao;
import java.math.BigDecimal;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class DatabaseService {
  @Autowired
  private SessionFactory sessionFactory;

  @Autowired
  private GenericDao<Item> itemDao;

  @Transactional
  public void init() {
    for (int i=0; i < 10; i++) {
      String itemName = "Item " + (i+1);
      Item item = new Item(itemName);
      Bid bid1 = new Bid(new BigDecimal(1000.0), item);
      Bid bid2 = new Bid(new BigDecimal(1100.0), item);
      itemDao.insert(item);
    }
  }

  @Transactional
  public void clear() {
    sessionFactory.getCurrentSession().createQuery("delete from Bid b").executeUpdate();
    sessionFactory.getCurrentSession().createQuery("delete from Item i").executeUpdate();
  }
}
