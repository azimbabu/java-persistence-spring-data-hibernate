package com.azimbabu.javapersistence.ch12.cartesianproduct;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class CartesianProduct {

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
      itemOne.addImage("foo.jpg");
      itemOne.addImage("bar.jpg");
      itemOne.addImage("baz.jpg");
      em.persist(itemOne);
      itemIds[0] = itemOne.getId();

      for (int i=0; i < 3; i++) {
        Bid bid = new Bid(itemOne, new BigDecimal(10 + i));
        itemOne.addBid(bid);
        em.persist(bid);
      }

      Item itemTwo  = new Item("Item Two", LocalDateTime.now().plusDays(2), janeRoe);
      itemTwo.addImage("foo2.jpg");
      itemTwo.addImage("bar2.jpg");
      itemTwo.addImage("baz2.jpg");
      em.persist(itemTwo);
      itemIds[1] = itemTwo.getId();

      for (int i=0; i < 3; i++) {
        Bid bid = new Bid(itemTwo, new BigDecimal(10 + i));
        itemTwo.addBid(bid);
        em.persist(bid);
      }

      Item itemThree = new Item("Item Three", LocalDateTime.now().plusDays(2), robertDoe);
      itemThree.addImage("foo3.jpg");
      itemThree.addImage("bar3.jpg");
      itemThree.addImage("baz3.jpg");
      em.persist(itemThree);
      itemIds[2] = itemThree.getId();

      for (int i=0; i < 3; i++) {
        Bid bid = new Bid(itemThree, new BigDecimal(10 + i));
        itemTwo.addBid(bid);
        em.persist(bid);
      }

      em.getTransaction().commit();

      return new TestData(itemIds, userIds);
    }
  }

  @Test
  void fetchCollections() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12.cartesianproduct")) {
      TestData testData = storeTestData(emf);
      long itemId = testData.itemIds[0];

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        Item item = em.find(Item.class, itemId);
        // select i.*, b.*, img.*, u.*
        // from Item i
        // left outer join Bid b on i.id = b.item_id
        // left outer join IMAGE img on i.id = img.Item_id
        // join USERS u on u.id = i.seller_id
        // where i.id = ?
        em.detach(item);

        assertEquals(3, item.getImages().size());
        assertEquals(3, item.getBids().size());

        em.getTransaction().commit();
      }
    }
  }
}
