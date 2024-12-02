package com.azimbabu.javapersistence.ch13.filtering.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.jupiter.api.Test;

public class AuditLogging {

  @Test
  void writeAuditLog() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch13.filtering.interceptor")) {
      addEventListener(emf);
      Long currentUserId;
      {
        User currentUser;
        try (EntityManager em = emf.createEntityManager()) {
          em.getTransaction().begin();

          currentUser = new User("johndoe");
          em.persist(currentUser);

          em.getTransaction().commit();
        }
        currentUserId = currentUser.getId();
      }

      try (EntityManager em = emf.createEntityManager()) {
        SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
        Session session = sessionFactory.withOptions().interceptor(new AuditLogInterceptor())
            .openSession();
        AuditLogInterceptor interceptor = (AuditLogInterceptor) ((SessionImplementor) session).getInterceptor();
        interceptor.setCurrentSession(session);
        interceptor.setCurrentUserId(currentUserId);

        /**
         * Insert item
         */
        session.beginTransaction();

        Item item0 = new Item("Foo");
        session.persist(item0);
        session.getTransaction().commit();

        em.getTransaction().begin();

        /**
         * Check insert audit log
         */
        List<AuditLogRecord> logs = em.createQuery(
            "select lr from AuditLogRecord lr", AuditLogRecord.class).getResultList();
        assertEquals(1, logs.size());
        assertEquals("insert", logs.get(0).getMessage());
        assertEquals(Item.class, logs.get(0).getEntityClass());
        assertEquals(item0.getId(), logs.get(0).getEntityId());
        assertEquals(currentUserId, logs.get(0).getUserId());

        em.createQuery("delete AuditLogRecord").executeUpdate();

        em.getTransaction().commit();

        /**
         * Update item
         */
        session.beginTransaction();

        Item item = session.find(Item.class, item0.getId());
        item.setName("Bar");
        session.persist(item);
        session.close();

        session.getTransaction().commit();

        /**
         * Check update audit log
         */
        em.getTransaction().begin();

        logs = em.createQuery(
            "select lr from AuditLogRecord lr", AuditLogRecord.class).getResultList();
        assertEquals(1, logs.size());
        assertEquals("update", logs.get(0).getMessage());
        assertEquals(Item.class, logs.get(0).getEntityClass());
        assertEquals(item0.getId(), logs.get(0).getEntityId());
        assertEquals(currentUserId, logs.get(0).getUserId());

        em.createQuery("delete AuditLogRecord").executeUpdate();

        em.getTransaction().commit();
      }
    }
  }

  private static void addEventListener(EntityManagerFactory emf) {
    SessionFactoryImplementor sessionFactory = emf.unwrap( SessionFactoryImplementor.class );
    sessionFactory
        .getServiceRegistry()
        .getService( EventListenerRegistry.class )
        .prependListeners( EventType.LOAD, new SecurityLoadListener() );
  }
}
