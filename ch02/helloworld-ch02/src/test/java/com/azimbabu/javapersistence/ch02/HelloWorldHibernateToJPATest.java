package com.azimbabu.javapersistence.ch02;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelloWorldHibernateToJPATest {

  @Test
  void storeLoadMessage() {
    EntityManagerFactory emf = createEntityManagerFactory();

    try {
      EntityManager em = emf.createEntityManager();

      em.getTransaction().begin();

      Message message = new Message();
      message.setText("Hello World from Hibernate to JPA!");

      em.persist(message);

      em.getTransaction().commit();

      em.getTransaction().begin();

      List<Message> messages = em.createQuery("select m from Message m", Message.class)
          .getResultList();

      em.getTransaction().commit();

      Assertions.assertAll(() -> Assertions.assertEquals(1, messages.size()),
          () -> Assertions.assertEquals("Hello World from Hibernate to JPA!",
              messages.get(0).getText()));
      em.close();
    } finally {
      emf.close();
    }
  }

  private EntityManagerFactory createEntityManagerFactory() {
    Configuration configuration = new Configuration();
    configuration.configure().addAnnotatedClass(Message.class);
    return Persistence.createEntityManagerFactory("ch02", configuration.getProperties());
  }
}
