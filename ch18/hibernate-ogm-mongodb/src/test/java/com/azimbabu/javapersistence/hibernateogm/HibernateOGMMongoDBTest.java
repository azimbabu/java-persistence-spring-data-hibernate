package com.azimbabu.javapersistence.hibernateogm;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.azimbabu.javapersistence.hibernateogm.model.Address;
import com.azimbabu.javapersistence.hibernateogm.model.Bid;
import com.azimbabu.javapersistence.hibernateogm.model.Item;
import com.azimbabu.javapersistence.hibernateogm.model.User;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HibernateOGMMongoDBTest {

  private static EntityManagerFactory entityManagerFactory;

  private User user;
  private Item item;
  private Bid bid1, bid2;

  @BeforeAll
  static void beforeAll() {
    entityManagerFactory = Persistence.createEntityManagerFactory("ch18.hibernate.ogm");
  }

  @AfterAll
  static void afterAll() {
    entityManagerFactory.close();
  }

  @BeforeEach
  void setUp() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    try {
      entityManager.getTransaction().begin();

      user = new User("John", "Smith");
      user.setAddress(new Address("Flowers Street", "12345", "Boston", "MA"));

      item = new Item("Item1");

      bid1 = new Bid(BigDecimal.valueOf(1000));
      bid2 = new Bid(BigDecimal.valueOf(2000));

      bid1.setItem(item);
      item.addBid(bid1);

      bid1.setUser(user);
      user.addBid(bid1);

      bid2.setItem(item);
      item.addBid(bid2);

      bid2.setUser(user);
      user.addBid(bid2);

      entityManager.persist(item);
      entityManager.persist(user);

      entityManager.getTransaction().commit();
    } finally {
      entityManager.close();
    }
  }

  @AfterEach
  void tearDown() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    try {

      entityManager.getTransaction().begin();

      User fetchedUser = entityManager.find(User.class, user.getId());
      Item fetchedItem = entityManager.find(Item.class, item.getId());
      Bid fetchedBid1 = entityManager.find(Bid.class, bid1.getId());
      Bid fetchedBid2 = entityManager.find(Bid.class, bid2.getId());

      entityManager.remove(fetchedBid1);
      entityManager.remove(fetchedBid2);
      entityManager.remove(fetchedItem);
      entityManager.remove(fetchedUser);

      entityManager.getTransaction().commit();
    } finally {
      entityManager.close();
    }
  }

  @Test
  void testCRUDOperations() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    try {
      entityManager.getTransaction().begin();

      User fetchedUser = entityManager.find(User.class, user.getId());
      Item fetchedItem = entityManager.find(Item.class, item.getId());
      Bid fetchedBid1 = entityManager.find(Bid.class, bid1.getId());
      Bid fetchedBid2 = entityManager.find(Bid.class, bid2.getId());

      assertAll(() -> assertNotNull(fetchedUser),
          () -> assertNotNull(fetchedUser.getId()),
          () -> assertEquals("John", fetchedUser.getFirstName()),
          () -> assertEquals("Smith", fetchedUser.getLastName()),
          () -> assertNotNull(fetchedItem),
          () -> assertNotNull(fetchedItem.getId()),
          () -> assertEquals("Item1", fetchedItem.getName()),
          () -> assertNotNull(fetchedBid1),
          () -> assertNotNull(fetchedBid1.getId()),
          () -> assertEquals(BigDecimal.valueOf(1000), fetchedBid1.getAmount()),
          () -> assertNotNull(fetchedBid2),
          () -> assertNotNull(fetchedBid2.getId()),
          () -> assertEquals(BigDecimal.valueOf(2000), fetchedBid2.getAmount()));

      entityManager.getTransaction().commit();
    } finally {
      entityManager.close();
    }
  }

  @Test
  void testJPQLQuery() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    try {
      entityManager.getTransaction().begin();

      User fetchedUser = entityManager.createQuery("select u from User u where u.id = :id", User.class)
          .setParameter("id", user.getId())
          .getSingleResult();

      Item fetchedItem = entityManager.createQuery("select i from Item i where i.id = :id", Item.class)
          .setParameter("id", item.getId())
          .getSingleResult();

      Bid fetchedBid1 = entityManager.createQuery("select b from Bid b where b.id = :id", Bid.class)
          .setParameter("id", bid1.getId())
          .getSingleResult();

      Bid fetchedBid2 = entityManager.createQuery("select b from Bid b where b.id = :id", Bid.class)
          .setParameter("id", bid2.getId())
          .getSingleResult();

      assertAll(() -> assertNotNull(fetchedUser),
          () -> assertNotNull(fetchedUser.getId()),
          () -> assertEquals("John", fetchedUser.getFirstName()),
          () -> assertEquals("Smith", fetchedUser.getLastName()),
          () -> assertNotNull(fetchedItem),
          () -> assertNotNull(fetchedItem.getId()),
          () -> assertEquals("Item1", fetchedItem.getName()),
          () -> assertNotNull(fetchedBid1),
          () -> assertNotNull(fetchedBid1.getId()),
          () -> assertEquals(BigDecimal.valueOf(1000), fetchedBid1.getAmount()),
          () -> assertNotNull(fetchedBid2),
          () -> assertNotNull(fetchedBid2.getId()),
          () -> assertEquals(BigDecimal.valueOf(2000), fetchedBid2.getAmount()));

      entityManager.getTransaction().commit();
    } finally {
      entityManager.close();
    }
  }
}
