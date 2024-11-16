package com.azimbabu.javapersistence.ch12.profile;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

public class Profile {

  @Test
  void fetchWithProfile() {
    FetchTestData fetchTestData = new FetchTestData();
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12.profile")) {
      TestData testData = fetchTestData.storeTestData(emf);
      long itemId = testData.getItemIds()[0];

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();
        /**
         * The <code>Item#seller</code> is mapped lazy, so the default fetch plan
        *  will only retrieve the <code>Item</code> instance.
         */
        Item item = em.find(Item.class, itemId);
        assertFalse(Hibernate.isInitialized(item.getSeller()));

        em.clear();

        /**
         * You need the Hibernate API to enable a profile, it is then active for any operation in that
         * unit of work. Now the <code>Item#seller</code> will be fetched with a join in the same SQL
         * statement whenever an <code>Item</code> is loaded with this <code>EntityManager</code>.
         */
        em.unwrap(Session.class).enableFetchProfile(Item.PROFILE_JOIN_SELLER);
        item = em.find(Item.class, itemId);

        em.clear();

        assertTrue(Hibernate.isInitialized(item.getSeller()));
        assertNotNull(item.getSeller().getUsername());

        em.clear();
        /**
         * You can overlay another profile on the same unit of work, now the <code>Item#seller</code>
         * and the <code>Item#bids</code> collection will be fetched with a join in the same SQL
         * statement whenever an <code>Item</code> is loaded.
         */
        em.unwrap(Session.class).enableFetchProfile(Item.PROFILE_JOIN_BIDS);
        item = em.find(Item.class, itemId);

        em.clear();

        assertTrue(Hibernate.isInitialized(item.getSeller()));
        assertNotNull(item.getSeller().getUsername());
        assertTrue(item.getBids().size() > 0);

        em.getTransaction().commit();
      }
    }
  }
}
