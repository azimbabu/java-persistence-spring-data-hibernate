package com.azimbabu.javapersistence.ch13.filtering.callback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

public class Callback {

  @Test
  void notifyPostPersist() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch13.filtering.callback")) {
      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        {
          User user = new User("johndoe");
          CurrentUser.INSTANCE.set(user);

          em.persist(user);
          assertEquals(0, Log.INSTANCE.size());
          em.flush();
          assertEquals(1, Log.INSTANCE.size());
          assertTrue(Log.INSTANCE.get(0).contains("johndoe"));
          Log.INSTANCE.clear();

          Item item = new Item("Foo", user);
          em.persist(item);
          assertEquals(0, Log.INSTANCE.size());
          em.flush();
          assertEquals(1, Log.INSTANCE.size());
          assertTrue(Log.INSTANCE.get(0).contains("johndoe"));
          Log.INSTANCE.clear();

          CurrentUser.INSTANCE.set(null);
        }
        em.clear();

        em.getTransaction().rollback();
      }
    }
  }
}
