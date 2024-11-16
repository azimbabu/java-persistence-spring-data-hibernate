package com.azimbabu.javapersistence.ch12.eagerjoin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class EagerJoin {

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
        Bid bid = new Bid(itemOne, robertDoe, new BigDecimal(10 + i));
        itemOne.addBid(bid);
        em.persist(bid);
      }

      Item itemTwo  = new Item("Item Two", LocalDateTime.now().plusDays(2), johnDoe);
      em.persist(itemTwo);
      itemIds[1] = itemTwo.getId();

      for (int i=0; i < 3; i++) {
        Bid bid = new Bid(itemTwo, janeRoe, new BigDecimal(10 + i));
        itemTwo.addBid(bid);
        em.persist(bid);
      }

      Item itemThree = new Item("Item Two", LocalDateTime.now().plusDays(2), janeRoe);
      em.persist(itemThree);
      itemIds[2] = itemThree.getId();

      em.getTransaction().commit();

      return new TestData(itemIds, userIds);
    }
  }
  @Test
  void fetchEagerJoin() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12.eagerjoin")){
      TestData testData = storeTestData(emf);
      long itemId = testData.itemIds[0];

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        Item item = em.find(Item.class, itemId);
        // select i.*, u.*, b.*
        //  from ITEM i
        //   left outer join USERS u on u.ID = i.SELLER_ID
        //   left outer join BID b on b.ITEM_ID = i.ID
        //  where i.ID = ?

        em.detach(item);  // Done fetching, no more lazy loading

        // In detached state, bids are available...
        assertEquals(3, item.getBids().size());
        assertNotNull(item.getBids().iterator().next().getAmount());

        // .. and the seller
        assertEquals("johndoe", item.getSeller().getUsername());
        em.getTransaction().commit();
      }
    }
  }
}
