package com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.model.BankAccount;
import com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.model.CreditCard;
import com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
      CreditCard creditCard1 = new CreditCard("John Smith", "123456789", "10", "2030");
      BankAccount bankAccount1 = new BankAccount("Jane Smith", "345678901", "Chase", "12345");

      CreditCard creditCard2 = new CreditCard("John Doe", "234567890", "11", "2031");
      BankAccount bankAccount2 = new BankAccount("Jane Doe", "4567890123", "Bank of America",
          "23451");

      User user1 = new User("Jack Smith");
      creditCard1.setUser(user1);
      user1.addBillingDetails(creditCard1);

      bankAccount1.setUser(user1);
      user1.addBillingDetails(bankAccount1);

      User user2 = new User("Jack Doe");
      creditCard2.setUser(user2);
      user2.addBillingDetails(creditCard2);

      bankAccount2.setUser(user2);
      user2.addBillingDetails(bankAccount2);

      // save entities
      session.beginTransaction();

      session.persist(user1);
      session.persist(creditCard1);
      session.persist(bankAccount1);

      session.persist(user2);
      session.persist(creditCard2);
      session.persist(bankAccount2);

      session.getTransaction().commit();

      // refresh entities
      session.refresh(user1);
      session.refresh(creditCard1);
      session.refresh(bankAccount1);

      session.refresh(user2);
      session.refresh(creditCard2);
      session.refresh(bankAccount2);

      Map<Long, User> userMap = new HashMap<>();
      userMap.put(user1.getId(), user1);
      userMap.put(user2.getId(), user2);

      // fetch entities
      session.beginTransaction();

      List<User> users = session.createQuery("select u from User u", User.class)
          .getResultList();

      session.getTransaction().commit();

      assertEquals(2, users.size());

      for (User userActual : users) {
        User userExpected = userMap.get(userActual.getId());
        assertEquals(userExpected.getUsername(), userActual.getUsername());
        assertEquals(userExpected.getBillingDetails().size(),
            userActual.getBillingDetails().size());
      }
    }
  }

  private SessionFactory createSessionFactory() {
    Configuration configuration = new Configuration();
    configuration.configure().addAnnotatedClass(CreditCard.class)
        .addAnnotatedClass(BankAccount.class)
        .addAnnotatedClass(User.class);
    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties())
        .build();
    return configuration.buildSessionFactory(serviceRegistry);
  }
}
