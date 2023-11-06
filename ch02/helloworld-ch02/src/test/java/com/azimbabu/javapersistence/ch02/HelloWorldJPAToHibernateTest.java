package com.azimbabu.javapersistence.ch02;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

public class HelloWorldJPAToHibernateTest {

  @Test
  void storeLoadMessage() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch02");
    try (SessionFactory sessionFactory = getSessionFactory(emf);
        Session session = sessionFactory.openSession()) {
      session.beginTransaction();

      Message message = new Message();
      message.setText("Hello World from JPA to Hibernate!");

      session.persist(message);
      session.getTransaction().commit();

      session.beginTransaction();

      CriteriaQuery<Message> criteriaQuery = session.getCriteriaBuilder()
          .createQuery(Message.class);
      criteriaQuery.from(Message.class);

      List<Message> messages = session.createQuery(criteriaQuery).getResultList();

      session.getTransaction().commit();

      assertAll(() -> assertEquals(1, messages.size()),
          () -> assertEquals("Hello World from JPA to Hibernate!",
              messages.get(0).getText()));
    }
  }

  private SessionFactory getSessionFactory(EntityManagerFactory emf) {
    return emf.unwrap(SessionFactory.class);
  }
}
