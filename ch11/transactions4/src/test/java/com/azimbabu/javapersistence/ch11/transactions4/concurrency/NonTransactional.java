package com.azimbabu.javapersistence.ch11.transactions4.concurrency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

public class NonTransactional {

  @Test
  void autoCommit() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch11.transactions4")) {
      Long itemId = createItem(emf);
      // Reading data in auto-commit mode
      readData(emf, itemId);
      // Queueing modifications
      queueingModifications(emf, itemId);
    }
  }

  private static Long createItem(EntityManagerFactory emf) {
    Long itemId;

    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      Item someItem = new Item("Original Name");
      em.persist(someItem);

      em.getTransaction().commit();

      itemId = someItem.getId();
    }
    return itemId;
  }

  private static void readData(EntityManagerFactory emf, Long itemId) {
    try (EntityManager em = emf.createEntityManager()) {
      /*
         No transaction is active when we create the <code>EntityManager</code>. The
         persistence context is now in a special <em>unsynchronized</em> mode, Hibernate
         will not flush automatically at any time.
      */

      /*
          You can access the database to read data; this operation will execute a
          <code>SELECT</code> statement, sent to the database in auto-commit mode.
      */
      Item item = em.find(Item.class, itemId);
      item.setName("New Name");
      assertEquals("Original Name",
          em.createQuery("select i.name from Item i where i.id = :id", String.class)
              .setParameter("id", itemId)
              .getSingleResult()
      );

      /*
         Retrieving a managed entity instance involves a lookup, during JDBC
         result set marshaling, in the current persistence context. The
         already loaded <code>Item</code> instance with the changed name will
         be returned from the persistence context, values from the database
         will be ignored. This is a repeatable read of an entity instance,
         even without a system transaction.
       */
      assertEquals("New Name",
          em.createQuery("select i from Item i where i.id = :id", Item.class)
              .setParameter("id", itemId)
              .getSingleResult()
              .getName()
      );

      /*
         If you try to flush the persistence context manually, to store the new
         <code>Item#name</code>, Hibernate will throw a
         <code>javax.persistence.TransactionRequiredException</code>. You are
         prevented from executing an <code>UPDATE</code> statement in
         <em>unsynchronized</em> mode, as you wouldn't be able to roll back the change.
      */
     // em.flush();

      /*
         You can roll back the change you made with the <code>refresh()</code>
         method, it loads the current <code>Item</code> state from the database
         and overwrites the change you have made in memory.
       */
      em.refresh(item);
      assertEquals("Original Name", item.getName());
    }
  }

  private static void queueingModifications(EntityManagerFactory emf, Long itemId) {
    try(EntityManager em = emf.createEntityManager()) {
      Item item = new Item("New Item");

      /*
         You can call <code>persist()</code> to save a transient entity instance with an
         unsynchronized persistence context. Hibernate will only fetch a new identifier
         value, typically by calling a database sequence, and assign it to the instance.
         The instance is now in persistent state in the context but the SQL
         <code>INSERT</code> hasn't happened. Note that this is only possible with
         <em>pre-insert</em> identifier generators; see <a href="#GeneratorStrategies"/>.
      */
      em.persist(item);
      assertNotNull(item.getId());

      /*
         When you are ready to store the changes, join the persistence context with
         a transaction. Synchronization and flushing will occur as usual, when the
         transaction commits. Hibernate writes all queued operations to the database.
       */
      em.getTransaction().begin();
      if (!em.isJoinedToTransaction()) {
        em.joinTransaction();
      }
      em.getTransaction().commit(); // Flush!
    }

    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();
      assertEquals("Original Name", em.find(Item.class, itemId).getName());
      assertEquals(2L, em.createQuery("select count(i) from Item i").getSingleResult());
      em.getTransaction().commit();
    }

    // Queueing merged changes of a detached entity
    Item detachedItem;
    try (EntityManager em = emf.createEntityManager()) {
      detachedItem = em.find(Item.class, itemId);
    }
    detachedItem.setName("New Name");

    try (EntityManager em = emf.createEntityManager()) {
      em.merge(detachedItem);

      em.getTransaction().begin();
      em.joinTransaction();
      em.getTransaction().commit(); // Flush!
    }

    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();
      assertEquals("New Name", em.find(Item.class, itemId).getName());
      em.getTransaction().commit();
    }

    // Queueing removal of entity instances and DELETE operations

    try (EntityManager em = emf.createEntityManager()) {
      Item item = em.find(Item.class, itemId);
      em.remove(item);

      em.getTransaction().begin();
      em.joinTransaction();
      em.getTransaction().commit();
    }

    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();
      assertEquals(1L, em.createQuery("select count(i) from Item i").getSingleResult());
      em.getTransaction().commit();
    }
  }
}
