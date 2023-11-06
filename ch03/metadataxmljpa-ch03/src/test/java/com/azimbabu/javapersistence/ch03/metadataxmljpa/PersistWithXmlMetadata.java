package com.azimbabu.javapersistence.ch03.metadataxmljpa;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PersistWithXmlMetadata {

  @Test
  void testPersistItem() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch03.metadataxmljpa");
        EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      Item item = new Item();
      item.setName("Some Item");
      item.setAuctionEnd(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)));
      em.persist(item);

      em.getTransaction().commit();
      em.getTransaction().begin();

      List<Item> items = em.createQuery("select i from Item i", Item.class)
          .getResultList();
      em.getTransaction().commit();

      assertAll(() -> assertEquals(1, items.size()),
          () -> assertEquals("Some Item", items.get(0).getName()));
    }
  }
}
