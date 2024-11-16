package com.azimbabu.javapersistence.ch12.proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;

public class LazyProxyCollections {

  private static final class TestData {
    private final Long[] categoryIds;
    private final Long[] itemIds;
    private final Long[] userIds;

    public TestData(Long[] categoryIds, Long[] itemIds, Long[] userIds) {
      this.categoryIds = categoryIds;
      this.itemIds = itemIds;
      this.userIds = userIds;
    }
  }

  private TestData storeTestData(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      Long[] categoryIds = new Long[3];
      Long[] itemIds = new Long[3];
      Long[] userIds = new Long[3];

      em.getTransaction().begin();

      User johnDoe = new User("johndoe");
      em.persist(johnDoe);
      userIds[0] = johnDoe.getId();

      User janeRoe = new User("janeroe");
      em.persist(janeRoe);
      userIds[1] = janeRoe.getId();

      User robertDoe = new User("robertdoe");
      em.persist(robertDoe);
      userIds[2] = robertDoe.getId();

      Category categoryOne = new Category("Category One");
      em.persist(categoryOne);
      categoryIds[0] = categoryOne.getId();

      Item itemOne  = new Item("Item One", LocalDateTime.now().plusDays(1), johnDoe);
      em.persist(itemOne);
      itemIds[0] = itemOne.getId();
      categoryOne.addItem(itemOne);
      itemOne.addCategory(categoryOne);

      for (int i=0; i < 3; i++) {
        Bid bid = new Bid(itemOne, robertDoe, new BigDecimal(10 + i));
        itemOne.addBid(bid);
        em.persist(bid);
      }

      Category categoryTwo = new Category("Category Two");
      em.persist(categoryTwo);
      categoryIds[1] = categoryTwo.getId();

      Item itemTwo  = new Item("Item Two", LocalDateTime.now().plusDays(2), johnDoe);
      em.persist(itemTwo);
      itemIds[1] = itemTwo.getId();
      categoryTwo.addItem(itemTwo);
      itemTwo.addCategory(categoryTwo);

      for (int i=0; i < 3; i++) {
        Bid bid = new Bid(itemTwo, janeRoe, new BigDecimal(10 + i));
        itemTwo.addBid(bid);
        em.persist(bid);
      }

      Item itemThree = new Item("Item Two", LocalDateTime.now().plusDays(2), janeRoe);
      em.persist(itemThree);
      itemIds[2] = itemThree.getId();
      categoryTwo.addItem(itemThree);
      itemThree.addCategory(categoryTwo);

      Category categoryThree = new Category("Category Three");
      em.persist(categoryThree);
      categoryIds[2] = categoryThree.getId();

      em.getTransaction().commit();

      return new TestData(categoryIds, itemIds, userIds);
    }
  }

  @Test
  void lazyEntityProxies() {
    try(EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12.proxy")) {
      TestData testData = storeTestData(emf);

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        Long itemId = testData.itemIds[0];
        Long userId = testData.userIds[0];

        {
          Item item = em.getReference(Item.class, itemId);  // No SELECT
          assertEquals(itemId, item.getId());
          assertNotEquals(Item.class, item.getClass());
          // using getSuperclass as HibernateProxyHelper is not available anymore
          assertEquals(Item.class, item.getClass().getSuperclass());

          PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
          assertFalse(persistenceUtil.isLoaded(item));
          assertFalse(persistenceUtil.isLoaded(item, "seller"));
          assertFalse(Hibernate.isInitialized(item));

          // Would trigger initialization of item!
          // assertFalse(Hibernate.isInitialized(item.getSeller()));
          // assertTrue(Hibernate.isInitialized(item));

          Hibernate.initialize(item);
          // select * from ITEM where ID = ?

          assertFalse(Hibernate.isInitialized(item.getSeller()));

          Hibernate.initialize(item.getSeller());
          // select * from USERS where ID = ?
        }
        em.clear();
        {
          /*
             An <code>Item</code> entity instance is loaded in the persistence context, its
             <code>seller</code> is not initialized, it's a <code>User</code> proxy.
           */
          Item item = em.find(Item.class, itemId);
          // select * from ITEM where ID = ?

          /*
             You can manually detach the data from the persistence context, or close the
             persistence context and detach everything.
           */
          em.detach(item);
          em.detach(item.getSeller());
          // em.close();

          /*
             The static <code>PersistenceUtil</code> helper works without a persistence
             context, you can check at any time if the data you want to access has
             actually been loaded.
           */
          PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
          assertTrue(persistenceUtil.isLoaded(item));
          assertFalse(persistenceUtil.isLoaded(item, "seller"));

          /*
             In detached state, you can call the identifier getter method of the
             <code>User</code> proxy. However, calling any other method on the proxy,
             such as <code>getUsername()</code>, will throw a <code>LazyInitializationException</code>.
             Data can only be loaded on-demand while the persistence context manages the proxy, not in detached
             state.
           */
          assertEquals(userId, item.getSeller().getId());

          // Throws exception!
          assertThrows(LazyInitializationException.class, () -> item.getSeller().getUsername());
        }
        em.clear();
        {
          Item item = em.getReference(Item.class, itemId);
          User user = em.getReference(User.class, userId);

          Bid newBid = new Bid(item, user, new BigDecimal("99.00"));
          em.persist(newBid);
          // insert into BID values (?, ? ,? , ...)

          em.flush();
          em.clear();
          assertEquals(0, em.find(Bid.class, newBid.getId()).getAmount().compareTo(new BigDecimal("99")));
        }

        em.getTransaction().commit();
      }
    }
  }

  @Test
  void lazyCollections() {
    try(EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12.proxy")) {
      TestData testData = storeTestData(emf);

      try(EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        Long itemId = testData.itemIds[0];

        {
          Item item = em.find(Item.class, itemId);
          // select * from ITEM where ID = ?

          // Collection is not initialized
          Set<Bid> bids = item.getBids(); // Collection is not initialized
          PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
          assertFalse(persistenceUtil.isLoaded(item, "bids"));

          // It's a Set
          assertTrue(Set.class.isAssignableFrom(bids.getClass()));

          // It's not a HashSet
          assertNotEquals(HashSet.class, bids.getClass());
          assertEquals(org.hibernate.collection.spi.PersistentSet.class, bids.getClass());

          Bid firstBid = bids.iterator().next();
          // select * from BID where ITEM_ID = ?
          // Hibernate.initialize(bids);  // Alternative
        }
        em.clear();
        {
          Item item = em.find(Item.class, itemId);
          // select * from ITEM where ID = ?

          assertEquals(3, item.getBids().size());
          // select count(b) from BID b where b.ITEM_ID = ?
        }
        em.getTransaction().commit();
      }
    }
  }
}
