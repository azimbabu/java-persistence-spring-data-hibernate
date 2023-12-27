package com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.model.BankAccount;
import com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.model.CreditCard;
import com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class MappingInheritanceJPATest {
  @Test
  void storeLoadEntities() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch07.mapping-inheritance-onetomany");
        EntityManager em = emf.createEntityManager()) {
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
      em.getTransaction().begin();

      em.persist(user1);
      em.persist(creditCard1);
      em.persist(bankAccount1);

      em.persist(user2);
      em.persist(creditCard2);
      em.persist(bankAccount2);

      em.getTransaction().commit();

      // refresh entities
      em.refresh(user1);
      em.refresh(creditCard1);
      em.refresh(bankAccount1);

      em.refresh(user2);
      em.refresh(creditCard2);
      em.refresh(bankAccount2);

      Map<Long, User> userMap = new HashMap<>();
      userMap.put(user1.getId(), user1);
      userMap.put(user2.getId(), user2);

      // fetch entities
      em.getTransaction().begin();

      List<User> users = em.createQuery("select u from User u", User.class)
          .getResultList();

      em.getTransaction().commit();

      assertEquals(2, users.size());

      for (User userActual : users) {
        User userExpected = userMap.get(userActual.getId());
        assertEquals(userExpected.getUsername(), userActual.getUsername());
        assertEquals(userExpected.getBillingDetails().size(), userActual.getBillingDetails().size());
      }
    }
  }
}
