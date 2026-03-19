package com.azimbabu.javapersistence.ch02;

import static org.junit.jupiter.api.Assertions.assertAll;

import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelloWorldHibernateTest {

  private static SessionFactory createSessionFactory() {
    Configuration configuration = new Configuration();
    configuration.configure().addAnnotatedClass(Message.class);
    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties()).build();
    return configuration.buildSessionFactory(serviceRegistry);
  }

  @Test
  void storeLoadMessage() {
    try (SessionFactory sessionFactory = createSessionFactory()) {
      try (Session session = sessionFactory.openSession()) {
        // store a message
        session.beginTransaction();

        Message message = new Message();
        message.setText("Hello World from Hibernate!");

        session.persist(message);

        session.getTransaction().commit();
        // INSERT into MESSAGE (ID, TEXT) VALUES (1, 'Hello World from Hibernate!')

        // load the message
        session.beginTransaction();

        CriteriaQuery<Message> criteriaQuery = session.getCriteriaBuilder().createQuery(Message.class);
        criteriaQuery.from(Message.class);

        List<Message> messages = session.createQuery(criteriaQuery).getResultList();
        // SELECT * from MESSAGE

        session.getTransaction().commit();

        assertAll(() -> Assertions.assertEquals(1, messages.size()),
            () -> Assertions.assertEquals("Hello World from Hibernate!", messages.get(0).getText()));
      }
    }
  }
}
