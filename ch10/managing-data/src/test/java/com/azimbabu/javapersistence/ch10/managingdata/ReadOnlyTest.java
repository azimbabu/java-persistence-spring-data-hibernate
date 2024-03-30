package com.azimbabu.javapersistence.ch10.managingdata;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.annotations.QueryHints;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;

public class ReadOnlyTest {

  private static final String PERSISTENCE_UNIT_NAME = "ch10.managingdata";

  @Test
  void selectiveReadOnly() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME)) {
      Long itemId;

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        Item item = new Item("Some Item");
        em.persist(item);

        em.getTransaction().commit();

        itemId = item.getId();
      }

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        {
          em.unwrap(Session.class).setDefaultReadOnly(true);

          Item itemFound = em.find(Item.class, itemId);
          itemFound.setName("New Name");

          em.flush(); // No update
        }

        {
          em.clear();

          Item itemFound = em.find(Item.class, itemId);
          assertNotEquals("New Name", itemFound.getName());
        }

        em.getTransaction().commit();
      }

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        {
          Item itemFound = em.find(Item.class, itemId);
          em.unwrap(Session.class).setReadOnly(itemFound, true);
          itemFound.setName("New Name");

          em.flush(); // No update
        }

        {
          em.clear();

          Item itemFound = em.find(Item.class, itemId);
          assertNotEquals("New Name", itemFound.getName());
        }

        em.getTransaction().commit();
      }

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        {
          Query<Item> query = em.unwrap(Session.class)
              .createQuery("select i from Item i", Item.class);
          List<Item> items = query.setReadOnly(true).list();
          assertFalse(items.isEmpty());
          items.stream().forEach(item -> item.setName("New Name"));

          em.flush(); // No update
        }

        {
          em.clear();

          Item itemFound = em.find(Item.class, itemId);
          assertNotEquals("New Name", itemFound.getName());
        }

        em.getTransaction().commit();
      }

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        {
          TypedQuery<Item> query = em.createQuery("select i from Item i", Item.class)
              .setHint(QueryHints.READ_ONLY, true);
          List<Item> items = query.getResultList();
          assertFalse(items.isEmpty());
          items.stream().forEach(item -> item.setName("New Name"));

          em.flush(); // No update
        }

        {
          em.clear();

          Item itemFound = em.find(Item.class, itemId);
          assertNotEquals("New Name", itemFound.getName());
        }

        em.getTransaction().commit();
      }
    }
  }
}
