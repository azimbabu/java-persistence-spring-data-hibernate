package com.azimbabu.javapersistence.ch11.transactions.concurrency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.Persistence;
import jakarta.persistence.RollbackException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;

public class Versioning {

  @Test
  void firstCommitWins() throws ExecutionException, InterruptedException {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch11.transactions")) {
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
        /*
           Retrieving an entity instance by identifier loads the current version from the
           database with a <code>SELECT</code>.
        */
        Item item = em.find(Item.class, itemId);  // select * from ITEM where ID = ?

        /*
           The current version of the <code>Item</code> instance is 0.
        */
        assertEquals(0, item.getVersion());

        item.setName("New Name");

        Executors.newSingleThreadExecutor().submit(() -> {
          try (EntityManager em2 = emf.createEntityManager()) {
            em2.getTransaction().begin();

            Item item2 = em2.find(Item.class, itemId);

            assertEquals(0, item2.getVersion());

            item2.setName("Other Name");

            em2.getTransaction().commit();
            return null;
          } catch (Exception ex) {
            // This shouldn't happen, this commit should win!
            throw new RuntimeException("Concurrent operation failure: " + ex, ex);
          }
        }).get();

        /*
           When the persistence context is flushed Hibernate will detect the dirty
           <code>Item</code> instance and increment its version to 1. The SQL
           <code>UPDATE</code> now performs the version check, storing the new version
           in the database, but only if the database version is still 0.
        */
        // update Item set buyNowPrice=?, category_id=?, name=?, version=? where id=? and version=?
        assertThrows(OptimisticLockException.class, () -> em.flush());

        em.getTransaction().commit();
      }
    }
  }

  @Test
  void manualVersionChecking() throws ExecutionException, InterruptedException {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch11.transactions")) {
      TestData testData = TestDataUtils.storeCategoriesAndItems(emf);
      BigDecimal totalPrice = BigDecimal.ZERO;

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        for (Long categoryId : testData.getCategoryIds()) {
          /*
             For each <code>Category</code>, query all <code>Item</code> instances with
             an <code>OPTIMISTIC</code> lock mode. Hibernate now knows it has to
             check each <code>Item</code> at flush time.
           */
          List<Item> items = em.createQuery(
                  "select  i from Item i where i.category.id = :categoryId", Item.class)
              .setLockMode(LockModeType.OPTIMISTIC)
              .setParameter("categoryId", categoryId)
              .getResultList();

          for (Item item : items) {
            totalPrice = totalPrice.add(item.getBuyNowPrice());
          }

          // Now a concurrent transaction will move an item to another category
          if (categoryId.equals(testData.getCategoryIds().get(0))) {
            Executors.newSingleThreadExecutor().submit(() -> {
              try (EntityManager em2 = emf.createEntityManager()) {
                em2.getTransaction().begin();

                // Moving the first item from the first category into the last category
                List<Item> items2 = em2.createQuery(
                        "select i from Item i where i.category.id = :categoryId", Item.class)
                    .setParameter("categoryId", categoryId)
                    .getResultList();

                Category lastCategory = em2.getReference(Category.class,
                    testData.getCategoryIds().get(testData.getCategoryIds().size() - 1));

                items2.iterator().next().setCategory(lastCategory);
                em2.flush();
                em2.getTransaction().commit();
              } catch (Exception ex) {
                // This shouldn't happen, this commit should win!
                throw new RuntimeException("Concurrent operation failure: " + ex, ex);
              }
              return null;
            }).get();
          }
        }

        //
        /**
         * Note that this commit won't fail in MySQL as MySQL's default isolation level is REPEATABLE_READ.
         * In order to fail this commit, we need to set hibernate.connection.isolation=2 in persistence.xml
         */
        em.getTransaction().commit();
      }
      /*
         For each <code>Item</code> loaded earlier with the locking query, Hibernate will
         now execute a <code>SELECT</code> during flushing. It checks if the database
         version of each <code>ITEM</code> row is still the same as when it was loaded
         earlier. If any <code>ITEM</code> row has a different version, or the row doesn't
         exist anymore, an <code>OptimisticLockException</code> will be thrown.
       */
      assertEquals(108.00, totalPrice.doubleValue());
    }
  }

  @Test
  void forceIncrement() throws ExecutionException, InterruptedException {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch11.transactions")) {
      TestData testData = storeItemAndBids(emf);
      Long itemId = testData.getItemIds().get(0);

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        /*
           The <code>find()</code> method accepts a <code>LockModeType</code>. The
           <code>OPTIMISTIC_FORCE_INCREMENT</code> mode tells Hibernate that the version
           of the retrieved <code>Item</code> should be incremented after loading,
           even if it's never modified in the unit of work.
        */
        Item item = em.find(Item.class, itemId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

        Bid highestBid = queryHighestBid(em, item);

        // Now a concurrent transaction will place a bid for this item, and
        // succeed because the first commit wins!
        Executors.newSingleThreadExecutor().submit(() -> {
          try (EntityManager em2 = emf.createEntityManager()) {
            em2.getTransaction().begin();

            Item item2 = em2.find(Item.class, itemId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            Bid highestBid2 = queryHighestBid(em2, item2);

            Bid newBid = new Bid(item2, BigDecimal.valueOf(44.44), highestBid2);
            em2.persist(newBid);

            em2.getTransaction().commit();
          } catch (Exception ex) {
            // This shouldn't happen, this commit should win!
            throw new RuntimeException("Concurrent operation failure: " + ex, ex);
          }
          return null;
        }).get();

        /*
           The code persists a new <code>Bid</code> instance; this does not affect
           any values of the <code>Item</code> instance. A new row will be inserted
           into the <code>BID</code> table. Hibernate would not detect concurrently
           made bids at all without a forced version increment of the
           <code>Item</code>. We also use a checked exception to validate the
           new bid amount; it must be greater than the currently highest bid.
        */

        Bid newBid = new Bid(item, BigDecimal.valueOf(45.45), highestBid);
        em.persist(newBid);

        /*
            When flushing the persistence context, Hibernate will execute an
            <code>INSERT</code> for the new <code>Bid</code> and force an
            <code>UPDATE</code> of the <code>Item</code> with a version check.
            If someone modified the <code>Item</code> concurrently, or placed a
            <code>Bid</code> concurrently with this procedure, Hibernate throws
            an exception.
        */
        assertThrows(RollbackException.class, () -> em.getTransaction().commit());
      }
    }
  }

  private Bid queryHighestBid(EntityManager em, Item item) {
    try {
      return em.createQuery("select b from Bid b where b.item = :item order by b.amount desc", Bid.class)
          .setParameter("item", item)
          .setMaxResults(1)
          .getSingleResult();
    } catch (NoResultException ex) {
      return null;
    }
  }

  private TestData storeItemAndBids(EntityManagerFactory emf) {
    List<Long> itemIds = new ArrayList<>();
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      Item item = new Item("Some Item");
      em.persist(item);
      itemIds.add(item.getId());

      for (int i=1; i <= 3; i++) {
        Bid bid = new Bid(item, BigDecimal.valueOf(10 + i));
        em.persist(bid);
      }

      em.getTransaction().commit();
    }
    return new TestData(Collections.emptyList(), itemIds);
  }
}
