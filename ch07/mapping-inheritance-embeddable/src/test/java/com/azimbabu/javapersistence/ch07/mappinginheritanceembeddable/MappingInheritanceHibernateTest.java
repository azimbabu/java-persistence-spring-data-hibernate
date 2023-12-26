package com.azimbabu.javapersistence.ch07.mappinginheritanceembeddable;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch07.mappinginheritanceembeddable.model.Dimensions;
import com.azimbabu.javapersistence.ch07.mappinginheritanceembeddable.model.Item;
import com.azimbabu.javapersistence.ch07.mappinginheritanceembeddable.model.Weight;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.Test;

public class MappingInheritanceHibernateTest {

  @Test
  void storeLoadEntities() {
    try (SessionFactory sessionFactory = createSessionFactory();
        Session session = sessionFactory.openSession()) {
      // create entities
      session.beginTransaction();

      Item item1 = new Item("Item 1",
          Dimensions.centimeters(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE),
          Weight.kilograms(BigDecimal.ONE));
      Item item2 = new Item("Item 2",
          Dimensions.inches(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN),
          Weight.pounds(BigDecimal.TEN));

      session.persist(item1);
      session.persist(item2);

      session.getTransaction().commit();

      // refresh entities
      session.refresh(item1);
      session.refresh(item2);

      // load entities
      session.beginTransaction();

      List<Item> items = session.createQuery("select i from Item i", Item.class).getResultList();

      session.getTransaction().commit();

      assertEquals(2, items.size());
      for (Item item : items) {
        if (item.getId().equals(item1.getId())) {
          assertAll(() -> assertEquals("Item 1", item.getName()),
              () -> assertEquals("centimeters", item.getDimensions().getName()),
              () -> assertEquals("cm", item.getDimensions().getSymbol()),
              () -> assertEquals(0, BigDecimal.ONE.compareTo(item.getDimensions().getDepth())),
              () -> assertEquals(0, BigDecimal.ONE.compareTo(item.getDimensions().getWidth())),
              () -> assertEquals(0, BigDecimal.ONE.compareTo(item.getDimensions().getHeight())),
              () -> assertEquals("kilograms", item.getWeight().getName()),
              () -> assertEquals("kg", item.getWeight().getSymbol()),
              () -> assertEquals(0, BigDecimal.ONE.compareTo(item.getWeight().getValue())));
        } else {
          assertAll(() -> assertEquals("Item 2", item.getName()),
              () -> assertEquals("inches", item.getDimensions().getName()),
              () -> assertEquals("\"", item.getDimensions().getSymbol()),
              () -> assertEquals(0, BigDecimal.TEN.compareTo(item.getDimensions().getDepth())),
              () -> assertEquals(0, BigDecimal.TEN.compareTo(item.getDimensions().getWidth())),
              () -> assertEquals(0, BigDecimal.TEN.compareTo(item.getDimensions().getHeight())),
              () -> assertEquals("pounds", item.getWeight().getName()),
              () -> assertEquals("lb", item.getWeight().getSymbol()),
              () -> assertEquals(0, BigDecimal.TEN.compareTo(item.getWeight().getValue())));
        }
      }
    }
  }

  private SessionFactory createSessionFactory() {
    Configuration configuration = new Configuration();
    configuration.configure().addAnnotatedClass(Item.class);
    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties())
        .build();
    return configuration.buildSessionFactory(serviceRegistry);
  }
}
