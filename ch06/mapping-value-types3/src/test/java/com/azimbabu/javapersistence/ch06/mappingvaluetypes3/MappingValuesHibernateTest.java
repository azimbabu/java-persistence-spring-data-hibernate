package com.azimbabu.javapersistence.ch06.mappingvaluetypes3;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.azimbabu.javapersistence.ch06.mappingvaluetypes3.model.Address;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes3.model.AuctionType;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes3.model.Bid;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes3.model.City;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes3.model.GermanZipcode;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes3.model.Item;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes3.model.MonetaryAmount;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes3.model.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.Test;

public class MappingValuesHibernateTest {

  @Test
  void storeLoadEntities() {
    try (SessionFactory sessionFactory = createSessionFactory();
        Session session = sessionFactory.openSession()) {
      // create user and item
      session.beginTransaction();
      City city = new City();
      city.setName("Boston");
      city.setZipcode(new GermanZipcode("12345"));
      city.setCountry("USA");

      User user = new User();
      user.setUsername("username");
      user.setHomeAddress(new Address("Flowers Street", city));

      Item item = new Item();
      item.setName("Some Item");
      item.setMetricWeight(2);
      item.setBuyNowPrice(new MonetaryAmount(BigDecimal.valueOf(1.1), Currency.getInstance("USD")));
      item.setDescription("descriptiondescription");

      session.persist(user);
      session.persist(item);

      session.getTransaction().commit();

      // refresh user and item
      session.refresh(user);
      session.refresh(item);

      // load user and item
      session.beginTransaction();

      List<User> users = session.createQuery("select u from User u", User.class).getResultList();
      List<Item> items = session.createQuery("select i from Item i where i.metricWeight = :w",
              Item.class)
          .setParameter("w", 2.0)
          .getResultList();

      session.getTransaction().commit();

      assertAll(() -> assertEquals(1, users.size()),
          () -> assertEquals("username", users.get(0).getUsername()),
          () -> assertEquals("Flowers Street", users.get(0).getHomeAddress().getStreet()),
          () -> assertEquals("Boston", users.get(0).getHomeAddress().getCity().getName()),
          () -> assertEquals("12345",
              users.get(0).getHomeAddress().getCity().getZipcode().getValue()),
          () -> assertEquals("USA", users.get(0).getHomeAddress().getCity().getCountry()));

      assertAll(() -> assertEquals(1, items.size()),
          () -> assertEquals("AUCTION: Some Item", items.get(0).getName()),
          () -> assertEquals("1.1 USD", items.get(0).getBuyNowPrice().toString()),
          () -> assertEquals(new BigDecimal("1.00"), items.get(0).getInitialPrice()),
          () -> assertEquals("descriptiondescription", items.get(0).getDescription()),
          () -> assertEquals("descriptiond...", items.get(0).getShortDescription()),
          () -> assertEquals(AuctionType.HIGHTEST_BID, items.get(0).getAuctionType()),
          () -> assertEquals(2.0, items.get(0).getMetricWeight()),
          () -> assertEquals(LocalDate.now(), items.get(0).getCreatedOn()),
          () -> assertTrue(
              ChronoUnit.SECONDS.between(LocalDateTime.now(), items.get(0).getLastModified()) < 1));
    }
  }

  private SessionFactory createSessionFactory() {
    Configuration configuration = new Configuration();
    configuration.configure().addAnnotatedClass(User.class)
        .addAnnotatedClass(Item.class)
        .addAnnotatedClass(Bid.class);
    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
            configuration.getProperties())
        .build();
    return configuration.buildSessionFactory(serviceRegistry);
  }
}
