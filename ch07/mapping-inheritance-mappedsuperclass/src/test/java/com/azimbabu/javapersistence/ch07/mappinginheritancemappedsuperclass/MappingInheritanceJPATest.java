package com.azimbabu.javapersistence.ch07.mappinginheritancemappedsuperclass;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch07.mappinginheritancemappedsuperclass.model.BankAccount;
import com.azimbabu.javapersistence.ch07.mappinginheritancemappedsuperclass.model.CreditCard;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import org.junit.jupiter.api.Test;

public class MappingInheritanceJPATest {
  @Test
  void storeLoadEntities() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch07.mapping-inheritance-mappedsuperclass");
        EntityManager em = emf.createEntityManager()) {
      // create entities
      em.getTransaction().begin();

      CreditCard creditCard = new CreditCard("John Smith", "123456789", "10", "2030");
      BankAccount bankAccount = new BankAccount("Mike Johnson", "12345", "Delta Bank", "BANKXY12");

      em.persist(creditCard);
      em.persist(bankAccount);

      em.getTransaction().commit();

      // refresh entities
      em.refresh(creditCard);
      em.refresh(bankAccount);

      // load entities
      em.getTransaction().begin();

      List<CreditCard> creditCards = em.createQuery("select cc from CreditCard cc",
          CreditCard.class).getResultList();
      List<BankAccount> bankAccounts = em.createQuery("select ba from BankAccount ba",
          BankAccount.class).getResultList();

      em.getTransaction().commit();

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
    }
  }
}
