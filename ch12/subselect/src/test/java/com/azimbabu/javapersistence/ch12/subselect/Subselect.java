package com.azimbabu.javapersistence.ch12.subselect;

import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Subselect {
  private static final class TestData {
    private final Long[] itemIds;
    private final Long[] userIds;

    public TestData(Long[] itemIds, Long[] userIds) {
      this.itemIds = itemIds;
      this.userIds = userIds;
    }
  }

  private TestData storeTestData(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
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

      Item itemOne  = new Item("Item One", LocalDateTime.now().plusDays(1), johnDoe);
      em.persist(itemOne);
      itemIds[0] = itemOne.getId();

      for (int i=0; i < 3; i++) {
        Bid bid = new Bid(itemOne, janeRoe, new BigDecimal(10 + i));
        itemOne.addBid(bid);
        em.persist(bid);
      }

      Item itemTwo  = new Item("Item Two", LocalDateTime.now().plusDays(2), janeRoe);
      em.persist(itemTwo);
      itemIds[1] = itemTwo.getId();

      for (int i=0; i < 3; i++) {
        Bid bid = new Bid(itemTwo, robertDoe, new BigDecimal(10 + i));
        itemTwo.addBid(bid);
        em.persist(bid);
      }

      Item itemThree = new Item("Item Three", LocalDateTime.now().plusDays(2), robertDoe);
      em.persist(itemThree);
      itemIds[2] = itemThree.getId();

      for (int i=0; i < 3; i++) {
        Bid bid = new Bid(itemThree, johnDoe, new BigDecimal(10 + i));
        itemTwo.addBid(bid);
        em.persist(bid);
      }

      em.getTransaction().commit();

      return new TestData(itemIds, userIds);
    }
  }

  @Test
  void fetchCollectionSubselect() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12.subselect")) {
      storeTestData(emf);

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        List<Item> items = em.createQuery("select i from Item i", Item.class)
            .getResultList();
        // select * from ITEM

        for (Item item : items) {
          assertTrue(item.getBids().size() > 0);
          // select * from BID where ITEM_ID in (
          //  select ID from ITEM
          // )
        }

        // The actual test
        em.clear();
        items = em.createQuery("select i from Item i", Item.class)
            .getResultList();
        // Access should load all collections
        assertTrue(items.iterator().next().getBids().size() > 0);
        em.clear(); // Detach all
        for (Item item : items) {
          assertTrue(item.getBids().size() > 0);
        }
        em.getTransaction().commit();
      }
    }
  }
}
