package com.azimbabu.javapersistence.ch06.mappingvaluetypes;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.azimbabu.javapersistence.ch06.mappingvaluetypes.model.Address;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes.model.AuctionType;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes.model.Bid;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes.model.Item;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes.model.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;

public class MappingValuesHibernateTest {

  @Test
  void storeLoadEntities() {
    try (SessionFactory sessionFactory = createSessionFactory();
        Session session = sessionFactory.openSession()) {
      // create user and item
      session.beginTransaction();

      User user = new User();
      user.setUsername("username");
      user.setHomeAddress(new Address("Flowers Street", "12345", "Boston"));

      Item item = new Item();
      item.setName("Some Item");
      item.setMetricWeight(2);
      item.setDescription("descriptiondescription");

      session.persist(user);
      session.persist(item);

      session.getTransaction().commit();

      session.refresh(user);
      session.refresh(item);

      // load users and items
      session.beginTransaction();

      List<User> users = session.createQuery("select u from User u", User.class)
          .getResultList();
      List<Item> items = session.createQuery("select i from Item i where i.metricWeight = :w",
              Item.class)
          .setParameter("w", 2.0)
          .getResultList();

      session.getTransaction().commit();

      assertAll(() -> assertEquals(1, users.size()),
          () -> assertEquals("username", users.get(0).getUsername()),
          () -> assertEquals("Flowers Street", users.get(0).getHomeAddress().getStreet()),
          () -> assertEquals("12345", users.get(0).getHomeAddress().getZipcode()),
          () -> assertEquals("Boston", users.get(0).getHomeAddress().getCity()),
          () -> assertEquals(1, items.size()),
          () -> assertEquals("AUCTION: Some Item", items.get(0).getName()),
          () -> assertEquals("descriptiondescription", items.get(0).getDescription()),
          () -> assertEquals("descriptiond...", items.get(0).getShortDescription()),
          () -> assertEquals(AuctionType.HIGHTEST_BID, items.get(0).getAuctionType()),
          () -> assertEquals(2, items.get(0).getMetricWeight()),
          () -> assertEquals(LocalDate.now(), items.get(0).getCreatedOn()),
          () -> assertTrue(
              ChronoUnit.SECONDS.between(LocalDateTime.now(), items.get(0).getLastModified()) < 1),
          () -> assertEquals(new BigDecimal("1.00"), items.get(0).getInitialPrice()));
    }
  }

  private SessionFactory createSessionFactory() {
    Configuration configuration = new Configuration();
    configuration.configure().addAnnotatedClass(User.class).addAnnotatedClass(Item.class)
        .addAnnotatedClass(
            Bid.class);
    StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties())
        .build();
    return configuration.buildSessionFactory(serviceRegistry);
  }
}
