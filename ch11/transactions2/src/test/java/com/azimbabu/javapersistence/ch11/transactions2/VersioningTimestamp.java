package com.azimbabu.javapersistence.ch11.transactions2;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.azimbabu.javapersistence.ch11.transactions2.timestamp.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.Persistence;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;

public class VersioningTimestamp {

  @Test
  void firstCommitWins() throws ExecutionException, InterruptedException {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch11.transactions2")) {
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

        Item item = em.find(Item.class, itemId);
        item.setName("New Name");

        // The concurrent second unit of work doing the same
        Executors.newSingleThreadExecutor().submit(() -> {
          try (EntityManager em2 = emf.createEntityManager()) {
            em2.getTransaction().begin();

            Item item2 = em2.find(Item.class, itemId);
            item2.setName("Other Name");

            em2.getTransaction().commit();
          } catch (Exception ex) {
            // This shouldn't happen, this commit should win!
            throw new RuntimeException("Concurrent operation failure: " + ex, ex);
          }
          return null;
        }).get();

        assertThrows(OptimisticLockException.class, () -> em.flush());

        em.getTransaction().commit();
      }
    }
  }
}
