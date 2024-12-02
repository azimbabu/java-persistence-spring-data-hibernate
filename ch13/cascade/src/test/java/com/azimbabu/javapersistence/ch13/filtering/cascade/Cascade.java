package com.azimbabu.javapersistence.ch13.filtering.cascade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Persistence;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

public class Cascade {

  @Test
  void detachMerge() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(
        "ch13.filtering.cascade")) {
      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();
        Long itemId;

        {
          User user = new User("johndoe");
          em.persist(user);

          Item item = new Item("Some Item", user);
          em.persist(item);
          itemId = item.getId();

          Bid firstBid = new Bid(new BigDecimal("99.00"), item);
          item.addBid(firstBid);
          em.persist(firstBid);

          Bid secondBid = new Bid(new BigDecimal("100.00"), item);
          item.addBid(secondBid);
          em.persist(secondBid);

          em.flush();
        }
        em.clear();

        Item item = em.find(Item.class, itemId);
        assertEquals(2, item.getBids().size());  // Initializes bids
        em.detach(item);

        em.clear();

        item.setName("New Name");
        Bid thirdBid = new Bid(new BigDecimal("101.00"), item);
        item.addBid(thirdBid);

        /*
           Hibernate merges the detached <code>item</code>: First, it checks if the
           persistence context already contains an <code>Item</code> with the given
           identifier value. In this case, there isn't any, so the <code>Item</code>
           is loaded from the database. Hibernate is smart enough to know that
           it will also need the <code>bids</code> during merging, so it fetches them
           right away in the same SQL query. Hibernate then copies the detached <code>item</code>
           values onto the loaded instance, which it returns to you in persistent state.
           The same procedure is applied to every <code>Bid</code>, and Hibernate
           will detect that one of the <code>bids</code> is new.
        */
        Item mergedItem = em.merge(item);
        // select i.*, b.*
        //  from ITEM i
        //    left outer join BID b on i.ID = b.ITEM_ID
        //  where i.ID = ?

        /**
         * Hibernate made the new <code>Bid</code> persistent during merging, it
         * now has an identifier value assigned.
         */
        for (Bid bid : mergedItem.getBids()) {
          assertNotNull(bid.getId());
        }

        /*
           When you flush the persistence context, Hibernate detects that the
           <code>name</code> of the <code>Item</code> changed during merging.
           The new <code>Bid</code> will also be stored.
         */
        em.flush();
        // update ITEM set NAME = ? where ID = ?
        // insert into BID values (?, ?, ?, ...)

        em.clear();

        item = em.find(Item.class, itemId);
        assertEquals("New Name", item.getName());
        assertEquals(3, item.getBids().size());

        em.getTransaction().commit();
      }
    }
  }

  @Test
  void refresh() throws ExecutionException, InterruptedException {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(
        "ch13.filtering.cascade")) {
      Long userId;
      Long creditCardId;

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();
        {
          User user = new User("johndoe");
          user.addBillingDetails(new CreditCard("John Doe", "1234567890", "11", "2020"));
          user.addBillingDetails(new BankAccount("John Doe", "45678", "Some Bank", "1234"));
          em.persist(user);
          em.flush();

          userId = user.getId();
          creditCardId = user.getBillingDetails().stream()
              .filter(billingDetails -> billingDetails instanceof CreditCard).findFirst().get()
              .getId();
          assertNotNull(creditCardId);
        }
        em.getTransaction().commit();
      }
      // Locks from INSERTs must be released, commit and start a new unit of work

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        /**
         * An instance of <code>User</code> is loaded from the database.
         */
        User user = em.find(User.class, userId);

        /*
           Its lazy <code>billingDetails</code> collection is initialized when
           you iterate through the elements or when you call <code>size()</code>.
         */
        assertEquals(2, user.getBillingDetails().size());
        for (BillingDetails billingDetails : user.getBillingDetails()) {
          assertEquals("John Doe", billingDetails.getOwner());
        }

        // Someone modifies the billing information in the database!
        Long someUserId = userId;
        Long someCreditCardId = creditCardId;
        // In a separate transaction, so no locks are held in the database on the
        // updated/deleted rows and we can SELECT them again in the original transaction
        Executors.newSingleThreadExecutor()
            .submit(() -> {
              try (EntityManager em1 = emf.createEntityManager()) {
                em1.getTransaction().begin();

                em1.unwrap(Session.class).doWork(connection -> {
                  // Update the bank account
                  PreparedStatement preparedStatement = connection.prepareStatement(
                      "update BILLINGDETAILS set OWNER = ? where USER_ID = ?");
                  preparedStatement.setString(1, "Doe John");
                  preparedStatement.setLong(2, someUserId);
                  preparedStatement.executeUpdate();
                });
                em1.getTransaction().commit();
              }
              return null;
            }).get();

        /*
           When you <code>refresh()</code> the managed <code>User</code> instance,
           Hibernate cascades the operation to the managed <code>BillingDetails</code>
           and refreshes each with a SQL <code>SELECT</code>. If one of these instances
           is no longer in the database, Hibernate throws an <code>EntityNotFoundException</code>.
           Then, Hibernate refreshes the <code>User</code> instance and eagerly
           loads the whole <code>billingDetails</code> collection to discover any
           new <code>BillingDetails</code>.
         */
        em.refresh(user);
        // select * from CREDITCARD join BILLINGDETAILS where ID = ?
        // select * from BANKACCOUNT join BILLINGDETAILS where ID = ?
        // select * from USERS
        //  left outer join BILLINGDETAILS
        //  left outer join CREDITCARD
        //  left outer JOIN BANKACCOUNT
        // where ID = ?
        for (BillingDetails billingDetails : user.getBillingDetails()) {
          assertEquals("Doe John", billingDetails.getOwner());
        }

        em.getTransaction().commit();
      }
    }
  }

  @Test
  void refreshException() throws ExecutionException, InterruptedException {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(
        "ch13.filtering.cascade")) {
      Long userId;
      Long creditCardId;

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();
        {
          User user = new User("johndoe");
          user.addBillingDetails(new CreditCard("John Doe", "1234567890", "11", "2020"));
          user.addBillingDetails(new BankAccount("John Doe", "45678", "Some Bank", "1234"));
          em.persist(user);
          em.flush();

          userId = user.getId();
          creditCardId = user.getBillingDetails().stream()
              .filter(billingDetails -> billingDetails instanceof CreditCard).findFirst().get()
              .getId();
          assertNotNull(creditCardId);
        }
        em.getTransaction().commit();
      }
      // Locks from INSERTs must be released, commit and start a new unit of work

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        /**
         * An instance of <code>User</code> is loaded from the database.
         */
        User user = em.find(User.class, userId);

        /*
           Its lazy <code>billingDetails</code> collection is initialized when
           you iterate through the elements or when you call <code>size()</code>.
         */
        assertEquals(2, user.getBillingDetails().size());
        for (BillingDetails billingDetails : user.getBillingDetails()) {
          assertEquals("John Doe", billingDetails.getOwner());
        }

        // Someone modifies the billing information in the database!
        Long someUserId = userId;
        Long someCreditCardId = creditCardId;
        // In a separate transaction, so no locks are held in the database on the
        // updated/deleted rows and we can SELECT them again in the original transaction
        Executors.newSingleThreadExecutor()
            .submit(() -> {
              try (EntityManager em1 = emf.createEntityManager()) {
                em1.getTransaction().begin();

                em1.unwrap(Session.class).doWork(connection -> {
                  /**
                   * Delete the credit card, this will cause the refresh to
                   * fail with EntityNotFoundException!
                   */
                  PreparedStatement preparedStatement = connection.prepareStatement(
                      "delete from CREDITCARD where ID = ?");
                  preparedStatement.setLong(1, someCreditCardId);
                  preparedStatement.executeUpdate();
                  preparedStatement = connection.prepareStatement(
                      "delete from BILLINGDETAILS where ID = ?");
                  preparedStatement.setLong(1, someCreditCardId);
                  preparedStatement.executeUpdate();
                });
                em1.getTransaction().commit();
              }
              return null;
            }).get();

        assertThrows(EntityNotFoundException.class, () -> em.refresh(user));

        em.getTransaction().commit();
      }
    }
  }

  @Test
  void replicate() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(
        "ch13.filtering.cascade")) {
      Long itemId;
      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        User user = new User("johndoe");
        em.persist(user);

        Item item = new Item("Some Item", user);
        em.persist(item);
        itemId = item.getId();

        em.getTransaction().commit();
      }

      Item item;
      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        item = em.find(Item.class, itemId);

        // Initialize the lazy Item#seller
        assertNotNull(item.getSeller().getUsername());

        em.getTransaction().commit();
      }

      try (EntityManager otherDatabase = emf.createEntityManager()) {
        otherDatabase.getTransaction().begin();

        otherDatabase.unwrap(Session.class).replicate(item, ReplicationMode.OVERWRITE);
        // select ID from ITEM where ID = ?
        // select ID from USERS where ID = ?

        otherDatabase.getTransaction().commit();
        // update ITEM set NAME = ?, SELLER_ID = ?, ... where ID = ?
        // update USERS set USERNAME = ?, ... where ID = ?
      }
    }
  }
}
