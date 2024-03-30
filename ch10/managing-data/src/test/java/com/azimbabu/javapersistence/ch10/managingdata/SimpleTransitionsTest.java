package com.azimbabu.javapersistence.ch10.managingdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUnitUtil;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import org.hibernate.LazyInitializationException;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

public class SimpleTransitionsTest {

  private static final String PERSISTENCE_UNIT_NAME = "ch10.managingdata";
  private static final String PERSISTENCE_UNIT_NAME_REPLICATE = "ch10.managingdata.replicate";

  @Test
  void makePersistent() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME)) {
      Item item = new Item("Some Item");

      try (EntityManager em = emf.createEntityManager()) { // Application-managed
        em.getTransaction().begin();

        // An entity is in transient state if PersistenceUnitUtil#getIdentifier(e) returns null.
        assertNull(emf.getPersistenceUnitUtil().getIdentifier(item));

        em.persist(item);

        // An entity instance is in persistent state if EntityManager#contains(e) returns true.
        assertTrue(em.contains(item));

        em.getTransaction().commit();
      }

      try (EntityManager em = emf.createEntityManager()) {
        // An entity instance is in the detached state if it’s not persistent, and
        // PersistenceUnitUtil#getIdentifier(e) returns the value of the entity’s identifier property.
        assertFalse(em.contains(item));
        assertNotNull(emf.getPersistenceUnitUtil().getIdentifier(item));

        em.getTransaction().begin();

        assertEquals("Some Item", em.find(Item.class, item.getId()).getName());

        em.getTransaction().commit();
      }
    }
  }

  @Test
  void retrievePersistent() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME)) {
      Item item = new Item("Some Item");

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();
        em.persist(item);
        em.getTransaction().commit();
      }

      Long itemId = item.getId();

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        // Hit the database if not already in persistence context
        Item itemFound = em.find(Item.class, itemId);
        if (itemFound != null) {
          itemFound.setName("New Name");
        }

        em.getTransaction().commit();  // Flush: Dirty check and SQL UPDATE
      }

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        Item itemA = em.find(Item.class, itemId);
        Item itemB = em.find(Item.class, itemId); // Repeatable read

        assertSame(itemA, itemB);
        assertEquals(itemA, itemB);
        assertEquals(itemA.getId(), itemB.getId());

        em.getTransaction().commit(); // Flush: Dirty check and SQL UPDATE
      }

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        assertEquals("New Name", em.find(Item.class, itemId).getName());

        em.getTransaction().commit();
      }
    }
  }

  @Test
  void retrievePersistentReference() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME)) {
      Long itemId;

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        Item someItem = new Item("Some Item");
        em.persist(someItem);
        itemId = someItem.getId();

        em.getTransaction().commit();
      }

      Item item;

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        /*
           If the persistence context already contains an <code>Item</code> with the given identifier, that
           <code>Item</code> instance is returned by <code>getReference()</code> without hitting the database.
           Furthermore, if <em>no</em> persistent instance with that identifier is currently managed, a hollow
           placeholder will be produced by Hibernate, a proxy. This means <code>getReference()</code> will not
           access the database, and it doesn't return <code>null</code>, unlike <code>find()</code>.
        */
        item = em.getReference(Item.class, itemId);

        /*
           JPA offers <code>PersistenceUnitUtil</code> helper methods such as <code>isLoaded()</code> to
           detect if you are working with an uninitialized proxy.
         */
        PersistenceUnitUtil persistenceUnitUtil = emf.getPersistenceUnitUtil();
        assertFalse(persistenceUnitUtil.isLoaded(item));

        /*
           As soon as you call any method such as <code>Item#getName()</code> on the proxy, a
           <code>SELECT</code> is executed to fully initialize the placeholder. The exception to this rule is
           a method that is a mapped database identifier getter method, such as <code>getId()</code>. A proxy
           might look like the real thing but it is only a placeholder carrying the identifier value of the
           entity instance it represents. If the database record doesn't exist anymore when the proxy is
           initialized, an <code>EntityNotFoundException</code> will be thrown.
         */
//        assertEquals("Some Item", item.getName());

        /*
           Hibernate has a convenient static <code>initialize()</code> method, loading the proxy's data.
         */
//        Hibernate.initialize(item);

        em.getTransaction().commit();
      }

      /*
         After the persistence context is closed, <code>item</code> is in detached state. If you do
         not initialize the proxy while the persistence context is still open, you get a
         <code>LazyInitializationException</code> if you access the proxy. You can't load
         data on-demand once the persistence context is closed. The solution is simple: Load the
         data before you close the persistence context.
       */
      assertThrows(LazyInitializationException.class, () -> System.out.println(item.getName()));
    }
  }

  @Test
  void makeTransient() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME)) {
      Long itemId;

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        Item someItem = new Item("Some Item");
        em.persist(someItem);

        em.getTransaction().commit();

        itemId = someItem.getId();
      }

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        /*
           If you call <code>find()</code>, Hibernate will execute a <code>SELECT</code> to
           load the <code>Item</code>. If you call <code>getReference()</code>, Hibernate
           will attempt to avoid the <code>SELECT</code> and return a proxy.
         */
        Item item = em.find(Item.class, itemId);
//        Item item = em.getReference(Item.class, itemId);

        /*
           Calling <code>remove()</code> will queue the entity instance for deletion when
           the unit of work completes, it is now in <em>removed</em> state. If <code>remove()</code>
           is called on a proxy, Hibernate will execute a <code>SELECT</code> to load the data.
           An entity instance has to be fully initialized during life cycle transitions. You may
           have life cycle callback methods or an entity listener enabled
           (see <a href="#EventListenersInterceptors"/>), and the instance must pass through these
           interceptors to complete its full life cycle.
         */
        em.remove(item);

        /*
          An entity in removed state is no longer in persistent state, this can be
          checked with the <code>contains()</code> operation.
         */
        assertFalse(em.contains(item));

        /*
           You can make the removed instance persistent again, cancelling the deletion.
        */
//        em.persist(item);

        // hibernate.use_identifier_rollback was enabled, it now looks like a transient instance
        assertNull(item.getId());

        /*
           When the transaction commits, Hibernate synchronizes the state transitions with the
           database and executes the SQL <code>DELETE</code>. The JVM garbage collector detects that the
           <code>item</code> is no longer referenced by anyone and finally deletes the last trace of
           the data.
         */
        em.getTransaction().commit();
      }

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        Item item = em.find(Item.class, itemId);
        assertNull(item);

        em.getTransaction().commit();
      }
    }
  }

  @Test
  void refresh() throws ExecutionException, InterruptedException {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME)) {
      Long itemId;

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        Item item = new Item("Some Item");
        em.persist(item);

        em.getTransaction().commit();

        itemId = item.getId();
      }

      Item item;
      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        item = em.find(Item.class, itemId);
        item.setName("Some Name");

        // Someone updates this row in the database!
        Executors.newSingleThreadExecutor().submit(() -> {
          try (EntityManager em2 = emf.createEntityManager()) {
            em2.getTransaction().begin();

            Session session = em2.unwrap(Session.class);
            session.doWork(connection -> {
              Item item2 = em2.find(Item.class, itemId);
              item2.setName("Concurrent Update Name");
              em2.persist(item2);
            });

            em2.getTransaction().commit();
          } catch (Exception ex) {
            throw new RuntimeException("Concurrent operation failure: " + ex, ex);
          }
          return null;
        }).get();

        em.refresh(item);

        em.getTransaction().commit(); // Flush: Dirty check and SQL UPDATE

        em.refresh(item);
      }

      assertEquals("Concurrent Update Name", item.getName());
    }
  }

  @Test
  void replicate() {
    Long itemId;

    try (EntityManagerFactory emfA = getDatabaseA();
        EntityManagerFactory emfB = getDatabaseB()) {
      try (EntityManager em = emfA.createEntityManager()) {
        em.getTransaction().begin();

        Item item = new Item("Some Item");
        em.persist(item);
        em.getTransaction().commit();

        itemId = item.getId();
      }

      Item item;

      try (EntityManager em = emfA.createEntityManager()) {
        em.getTransaction().begin();

        item = em.find(Item.class, itemId);

        em.getTransaction().commit();
      }

      try (EntityManager em = emfB.createEntityManager()) {
        em.getTransaction().begin();

        em.unwrap(Session.class).replicate(item, ReplicationMode.LATEST_VERSION);

        Item item2 = em.find(Item.class, itemId);
        assertEquals("Some Item", item2.getName());

        em.getTransaction().commit();
      }
    }
  }

  @Test
  void flushModeType() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME)) {
      Long itemId;

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        Item someItem = new Item("Original Name");
        em.persist(someItem);

        em.getTransaction().commit();

        itemId = someItem.getId();
      }

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        Item item = em.find(Item.class, itemId);
        item.setName("New Name");

        // Disable flushing before queries:
        em.setFlushMode(FlushModeType.COMMIT);

        assertEquals("Original Name",
            em.createQuery("select i.name from Item i where i.id = :id", String.class)
                .setParameter("id", itemId).getSingleResult());

        em.getTransaction().commit(); // Flush!
      }
    }
  }

  @Test
  void scopeOfIdentity() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME)) {
      Long itemId;
      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        Item someItem = new Item("Some Item");
        em.persist(someItem);

        em.getTransaction().commit();
        itemId = someItem.getId();
      }

      Item itemA;
      Item itemB;
      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        itemA = em.find(Item.class, itemId);
        itemB = em.find(Item.class, itemId);
        assertSame(itemA, itemB);
        assertEquals(itemA, itemB);
        assertEquals(itemA.getId(), itemB.getId());

        em.getTransaction().commit();
      }

      // Persistence Context is gone, 'a' and 'b' are now references to instances in detached state!
      Item itemC;
      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        itemC = em.find(Item.class, itemId);
        assertNotSame(itemA, itemC);
        assertNotEquals(itemA, itemC);
        assertEquals(itemA.getId(), itemC.getId());

        em.getTransaction().commit();
      }

      Set<Item> items = new HashSet<>();
      items.add(itemA);
      items.add(itemB);
      items.add(itemC);
      assertEquals(2, items.size());  // That seems wrong and arbitrary!
    }
  }

  @Test
  void detach() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME)) {
      Long userId;
      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        User someUser = new User("John Smith");
        someUser.setHomeAddress(new Address("Some Street", "1234", "Some City"));
        em.persist(someUser);

        em.getTransaction().commit();

        userId = someUser.getId();
      }

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        User someUser = em.find(User.class, userId);
        em.detach(someUser);
        assertFalse(em.contains(someUser));

        em.getTransaction().commit();
      }
    }
  }

  @Test
  void mergeDetached() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME)) {
      Long userId;
      User detachedUser;
      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        detachedUser = new User("Foo");
        detachedUser.setHomeAddress(new Address("Some Street", "1234", "Some City"));
        em.persist(detachedUser);

        em.getTransaction().commit();
        userId = detachedUser.getId();
      }

      detachedUser.setUsername("John Doe");

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        // Discard 'detachedUser' reference after merging!
        User mergedUser = em.merge(detachedUser);
        // The 'mergedUser' is in persistent state
        mergedUser.setUsername("Jane Doe"); // UPDATE in database

        em.getTransaction().commit();
      }

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        User user = em.find(User.class, userId);
        assertEquals("Jane Doe", user.getUsername());

        em.getTransaction().commit();
      }
    }
  }

  private EntityManagerFactory getDatabaseA() {
    return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
  }

  private EntityManagerFactory getDatabaseB() {
    return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME_REPLICATE);
  }
}
