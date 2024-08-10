package com.azimbabu.javapersistence.ch11.transactions.concurrency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.LockModeType;
import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.PessimisticLockException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

public class Locking {

  private static final String PERSISTENCE_LOCK_TIMEOUT = "jakarta.persistence.lock.timeout";

  @Test
  void pessimisticReadWrite() throws ExecutionException, InterruptedException {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch11.transactions")) {
      TestData testData = TestDataUtils.storeCategoriesAndItems(emf);
      BigDecimal totalPrice = BigDecimal.ZERO;

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        for (Long categoryId : testData.getCategoryIds()) {
          /*
             For each <code>Category</code>, query all <code>Item</code> instances in
             <code>PESSIMISTIC_READ</code> lock mode. Hibernate will lock the rows in
             the database with the SQL query. If possible, wait for 5 seconds if some
             other transaction already holds a conflicting lock. If the lock can't
             be obtained, the query throws an exception.
           */
          List<Item> items = em.createQuery(
                  "select i from Item i where i.category.id = :categoryId", Item.class)
              .setLockMode(LockModeType.PESSIMISTIC_READ)
              .setHint(PERSISTENCE_LOCK_TIMEOUT, 5000)
              .setParameter("categoryId", categoryId)
              .getResultList();

          /*
             If the query returns successfully, you know that you hold an exclusive lock
             on the data and no other transaction can access it with an exclusive lock or
             modify it until this transaction commits.
           */
          for (Item item : items) {
            totalPrice = totalPrice.add(item.getBuyNowPrice());
          }

          // Now a concurrent transaction will try to obtain a write lock, it fails because
          // we hold a read lock on the data already. Note that on H2 there actually are no
          // read or write locks, only exclusive locks.
          if (categoryId.equals(testData.getCategoryIds().get(0))) {
            Executors.newSingleThreadExecutor().submit(() -> {
              try (EntityManager em2 = emf.createEntityManager()) {
                em2.getTransaction().begin();

                // The next query's lock attempt must fail at _some_ point, and
                // we'd like to wait 5 seconds for the lock to become available:
                //
                // - H2 fails with a default global lock timeout of 1 second.
                //
                // - Oracle supports dynamic lock timeouts, we set it with
                //   the 'jakarta.persistence.lock.timeout' hint on the query:
                //
                //      no hint == FOR UPDATE
                //      jakarta.persistence.lock.timeout 0ms == FOR UPDATE NOWAIT
                //      jakarta.persistence.lock.timeout >0ms == FOR UPDATE WAIT [seconds]
                //
                em2.unwrap(Session.class).doWork(connection -> {
                  switch (connection.getMetaData().getDatabaseProductName()) {
                    // - MySQL also doesn't support query lock timeouts, but you
                    //   can set a timeout for the whole connection/session.
                    case "MySQL":
                      connection.createStatement().execute("set innodb_lock_wait_timeout = 5;");
                      break;
                    // - PostgreSQL doesn't timeout and just hangs indefinitely if
                    //   NOWAIT isn't specified for the query. One possible way to
                    //   wait for a lock is to set a statement timeout for the whole
                    //   connection/session.
                    case "PostgreSQL":
                      connection.createStatement().execute("set statement_timeout = 5000");
                      break;
                  }
                });

                // Moving the first item from the first category into the last category
                // This query should fail as someone else holds a lock on the rows.
                List<Item> items2 = em2.createQuery(
                        "select i from Item i where i.category.id = :categoryId", Item.class)
                    .setParameter("categoryId", categoryId)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)  // Prevent concurrent access
                    .setHint(PERSISTENCE_LOCK_TIMEOUT, 5000)  // Only works on Oracle...
                    .getResultList();

                Category lastCategory = em2.getReference(Category.class,
                    testData.getCategoryIds().get(testData.getCategoryIds().size() - 1));

                items2.iterator().next().setCategory(lastCategory);

                em2.getTransaction().commit();
              } catch (Exception ex) {
                // This should fail, as the data is already locked!
                em.unwrap(Session.class).doWork(connection -> {
                  switch (connection.getMetaData().getDatabaseProductName()) {
                    // On MySQL we get a LockTimeoutException
                    case "MySQL":
                      assertTrue(ex instanceof LockTimeoutException);
                      break;
                    // A statement timeout on PostgreSQL doesn't produce a specific exception
                    case "PostgreSQL":
                      assertTrue(ex instanceof PersistenceException);
                      break;
                    // On H2 and Oracle we get a PessimisticLockException
                    default:
                      assertTrue(ex instanceof PessimisticLockException);
                      break;
                  }
                });
              }
              return null;
            }).get();
          }
        }

        /*
           Our locks will be released after commit, when the transaction completes.
         */
        em.getTransaction().commit();
      }

      assertEquals(0, totalPrice.compareTo(new BigDecimal("108")));
    }
  }

  @Test
  void findLock() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch11.transactions")) {
      TestData testData = TestDataUtils.storeCategoriesAndItems(emf);
      Long categoryId = testData.getCategoryIds().get(0);

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        Map<String, Object> hints = new HashMap<>();
        hints.put("jakarta.persistence.lock.timeout", 5000);

        // Executes a SELECT .. FOR UPDATE WAIT 5000 if supported by dialect
        Category category = em.find(Category.class, categoryId, LockModeType.PESSIMISTIC_WRITE,
            hints);
        category.setName("New Name");

        em.getTransaction().commit();
      }
    }
  }
}
