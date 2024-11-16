package com.azimbabu.javapersistence.ch12.eagerselect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

public class EagerSelect {

  @Test
  void fetchEagerSelect() {
    FetchTestData fetchTestData = new FetchTestData();
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12.eagerselect")) {
      TestData testData = fetchTestData.storeTestData(emf);
      long itemId = testData.getItemIds()[0];

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        Item item = em.find(Item.class, itemId);
        // select * from ITEM where ID = ?
        // select * from USERS where ID = ?
        // select * from BID where ITEM_ID = ?

        em.detach(item);

        assertEquals(3, item.getBids().size());
        assertNotNull(item.getBids().iterator().next().getAmount());
        assertEquals("johndoe", item.getSeller().getUsername());

        em.getTransaction().commit();
      }
    }
  }
}
