package com.azimbabu.javapersistence.ch05.mapping;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch05.mapping.model.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

public class HelloWorldJPATest {

  @Test
  void storeLoadItem() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch05.mapping");
        EntityManager em = emf.createEntityManager()) {
      // create an item
      em.getTransaction().begin();

      Item item = new Item();
      item.setName("Some Item");
      item.setAuctionEnd(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)));
      em.persist(item);

      em.getTransaction().commit();

      // fetch all items
      em.getTransaction().begin();

      // SELECT * FROM ITEM
      List<Item> items = em.createQuery("select i from Item i", Item.class)
          .getResultList();

      em.getTransaction().commit();

      assertAll((() -> assertEquals(1, items.size())),
          () -> assertEquals("Some Item", items.get(0).getName()));
    }
  }
}
