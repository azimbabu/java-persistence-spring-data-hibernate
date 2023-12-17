package com.azimbabu.javapersistence.ch07.mappinginheritancesingletableformula;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch07.mappinginheritancesingletableformula.model.BankAccount;
import com.azimbabu.javapersistence.ch07.mappinginheritancesingletableformula.model.BillingDetails;
import com.azimbabu.javapersistence.ch07.mappinginheritancesingletableformula.model.CreditCard;
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

      CreditCard creditCard = new CreditCard("John Smith", "123456789", "10", "2030");
      BankAccount bankAccount = new BankAccount("Mike Johnson", "12345", "Delta Bank", "BANKXY12");

      session.persist(creditCard);
      session.persist(bankAccount);

      session.getTransaction().commit();

      // refresh entities
      session.refresh(creditCard);
      session.refresh(bankAccount);

      // load entities
      session.beginTransaction();

      List<BillingDetails> billingDetails = session.createQuery("select bd from BillingDetails bd",
          BillingDetails.class).getResultList();
      List<CreditCard> creditCards = session.createQuery("select cc from CreditCard cc",
          CreditCard.class).getResultList();
      List<BankAccount> bankAccounts = session.createQuery("select ba from BankAccount ba",
          BankAccount.class).getResultList();

      session.getTransaction().commit();

      // validate
      assertAll(() -> assertEquals(1, creditCards.size()),
          () -> assertEquals("John Smith", creditCards.get(0).getOwner()),
          () -> assertEquals("123456789", creditCards.get(0).getCardNumber()),
          () -> assertEquals("10", creditCards.get(0).getExpMonth()),
          () -> assertEquals("2030", creditCards.get(0).getExpYear()));

      assertAll(() -> assertEquals(1, bankAccounts.size()),
          () -> assertEquals("Mike Johnson", bankAccounts.get(0).getOwner()),
          () -> assertEquals("12345", bankAccounts.get(0).getAccount()),
          () -> assertEquals("Delta Bank", bankAccounts.get(0).getBankName()),
          () -> assertEquals("BANKXY12", bankAccounts.get(0).getSwift()));

      assertAll(() -> assertEquals(2, billingDetails.size()));
    }
  }

  private SessionFactory createSessionFactory() {
    Configuration configuration = new Configuration();
    configuration.configure().addAnnotatedClass(CreditCard.class)
        .addAnnotatedClass(BankAccount.class);
    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties())
        .build();
    return configuration.buildSessionFactory(serviceRegistry);
  }
}
