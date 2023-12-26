package com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone.model.BankAccount;
import com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone.model.CreditCard;
import com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class MappingInheritanceJPATest {

  @Test
  void storeLoadEntities() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory(
        "ch07.mapping-inheritance-manytoone");
        EntityManager em = emf.createEntityManager()) {
      // create entities
      em.getTransaction().begin();

      CreditCard creditCard1 = new CreditCard("John Smith", "123456789", "10", "2030");
      CreditCard creditCard2 = new CreditCard("John Doe", "234567890", "11", "2031");

      BankAccount bankAccount1 = new BankAccount("Jane Smith", "345678901", "Chase", "12345");
      BankAccount bankAccount2 = new BankAccount("Jane Doe", "4567890123", "Bank of America",
          "23451");

      User user1 = new User("Jack Smith");
      user1.setDefaultBilling(creditCard1);

      User user2 = new User("Sean Smith");
      user2.setDefaultBilling(creditCard1);

      User user3 = new User("Jack Doe");
      user3.setDefaultBilling(creditCard2);

      User user4 = new User("Sean Doe");
      user4.setDefaultBilling(creditCard2);

      User user5 = new User("Mary Smith");
      user5.setDefaultBilling(bankAccount1);

      User user6 = new User("Mona Smith");
      user6.setDefaultBilling(bankAccount1);

      User user7 = new User("Mary Doe");
      user7.setDefaultBilling(bankAccount2);

      User user8 = new User("Mona Doe");
      user8.setDefaultBilling(bankAccount2);

      em.persist(creditCard1);
      em.persist(creditCard2);
      em.persist(bankAccount1);
      em.persist(bankAccount2);

      em.persist(user1);
      em.persist(user2);
      em.persist(user3);
      em.persist(user4);
      em.persist(user5);
      em.persist(user6);
      em.persist(user7);
      em.persist(user8);

      em.getTransaction().commit();

      // refresh entities
      em.refresh(creditCard1);
      em.refresh(creditCard2);
      em.refresh(bankAccount1);
      em.refresh(bankAccount2);

      em.refresh(user1);
      em.refresh(user2);
      em.refresh(user3);
      em.refresh(user4);
      em.refresh(user5);
      em.refresh(user6);
      em.refresh(user7);
      em.refresh(user8);

      Map<Long, User> userByIdMap = new HashMap<>();
      userByIdMap.put(user1.getId(), user1);
      userByIdMap.put(user2.getId(), user2);
      userByIdMap.put(user3.getId(), user3);
      userByIdMap.put(user4.getId(), user4);
      userByIdMap.put(user5.getId(), user5);
      userByIdMap.put(user6.getId(), user6);
      userByIdMap.put(user7.getId(), user7);
      userByIdMap.put(user8.getId(), user8);

      // pay
      user1.getDefaultBilling().pay(BigDecimal.valueOf(101));
      user2.getDefaultBilling().pay(BigDecimal.valueOf(102));
      user3.getDefaultBilling().pay(BigDecimal.valueOf(103));
      user4.getDefaultBilling().pay(BigDecimal.valueOf(104));
      user5.getDefaultBilling().pay(BigDecimal.valueOf(105));
      user6.getDefaultBilling().pay(BigDecimal.valueOf(106));
      user7.getDefaultBilling().pay(BigDecimal.valueOf(107));
      user8.getDefaultBilling().pay(BigDecimal.valueOf(108));

      // load entities
      em.getTransaction().begin();

      List<User> users = em.createQuery("select u from User u", User.class).getResultList();

      em.getTransaction().commit();

      assertEquals(8, users.size());
      for (User user : users) {
        User userExpected = userByIdMap.get(user.getId());
        assertAll(() -> assertEquals(userExpected.getUsername(), user.getUsername()),
            () -> assertEquals(userExpected.getDefaultBilling().getOwner(),
                user.getDefaultBilling().getOwner()));

        if (userExpected.getDefaultBilling() instanceof BankAccount bankAccountExpected) {
          BankAccount bankAccountActual = (BankAccount) user.getDefaultBilling();
          assertAll(
              () -> assertEquals(bankAccountExpected.getAccount(), bankAccountActual.getAccount()),
              () -> assertEquals(bankAccountExpected.getBankName(),
                  bankAccountActual.getBankName()),
              () -> assertEquals(bankAccountExpected.getSwift(), bankAccountActual.getSwift()));
        } else {
          CreditCard creditCardExpected = (CreditCard) userExpected.getDefaultBilling();
          CreditCard creditCardActual = (CreditCard) user.getDefaultBilling();
          assertAll(() -> assertEquals(creditCardExpected.getCardNumber(),
                  creditCardActual.getCardNumber()),
              () -> assertEquals(creditCardExpected.getExpMonth(), creditCardActual.getExpMonth()),
              () -> assertEquals(creditCardExpected.getExpYear(), creditCardActual.getExpYear()));
        }
      }

    }
  }
}
