package com.azimbabu.javapersistence.ch06.mappingvaluetypes3;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.azimbabu.javapersistence.ch06.mappingvaluetypes3.model.Address;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes3.model.AuctionType;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes3.model.City;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes3.model.GermanZipcode;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes3.model.Item;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes3.model.MonetaryAmount;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes3.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.List;
import org.junit.jupiter.api.Test;

public class MappingValuesJPATest {

  @Test
  void storeLoadEntities() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(
        "ch06.mapping_value_types3");
        EntityManager em = emf.createEntityManager()) {
      // create user and item
      em.getTransaction().begin();

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

      em.persist(user);
      em.persist(item);

      em.getTransaction().commit();

      // refresh user and item
      em.refresh(user);
      em.refresh(item);

      // load user and item
      em.getTransaction().begin();

      List<User> users = em.createQuery("select u from User u", User.class)
          .getResultList();
      List<Item> items = em.createQuery("select i from Item i where i.metricWeight = :w",
              Item.class)
          .setParameter("w", 2.0)
          .getResultList();

      em.getTransaction().commit();

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
}
