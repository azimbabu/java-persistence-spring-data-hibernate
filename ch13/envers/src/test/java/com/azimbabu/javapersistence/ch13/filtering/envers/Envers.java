package com.azimbabu.javapersistence.ch13.filtering.envers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.MatchMode;
import org.junit.jupiter.api.Test;

public class Envers {

  @Test
  void auditLogging() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch13.filtering.envers")) {
      Long userId;
      Long itemId;
      {
        User user;
        Item item;
        try (EntityManager em = emf.createEntityManager()) {
          em.getTransaction().begin();

          user = new User("johndoe");
          em.persist(user);

          item = new Item("Foo", user);
          em.persist(item);

          em.getTransaction().commit();
        }
        userId = user.getId();
        itemId = item.getId();
      }
      LocalDateTime timestampCreate = LocalDateTime.now();

      {
        try (EntityManager em = emf.createEntityManager()) {
          em.getTransaction().begin();

          Item item = em.find(Item.class, itemId);
          item.setName("Bar");
          item.getSeller().setUsername("doejohn");

          em.getTransaction().commit();
        }
      }
      LocalDateTime timestampUpdate = LocalDateTime.now();

      {
        try (EntityManager em = emf.createEntityManager()) {
          em.getTransaction().begin();

          Item item = em.find(Item.class, itemId);
          em.remove(item);

          em.getTransaction().commit();
        }
      }
      LocalDateTime timestampDelete = LocalDateTime.now();

      {
        try (EntityManager em = emf.createEntityManager()) {
          em.getTransaction().begin();

          /*
             The main Envers API is the <code>AuditReader</code>, it can be accessed with
             an <code>EntityManager</code>.
           */
          AuditReader auditReader = AuditReaderFactory.get(em);

          /*
             Given a timestamp, you can find the revision number of a change set, made
             before or on that timestamp.
           */
          Number revisionCreate = auditReader.getRevisionNumberForDate(timestampCreate);
          Number revisionUpdate = auditReader.getRevisionNumberForDate(timestampUpdate);
          Number revisionDelete = auditReader.getRevisionNumberForDate(timestampDelete);

          /*
             If you don't have a timestamp, you can get all revision numbers in which a
             particular audited entity instance was involved. This operation finds all
             change sets where the given <code>Item</code> was created, modified, or
             deleted. In our example, we created, modified, and then deleted the
             <code>Item</code>. Hence, we have three revisions.
           */
          List<Number> itemRevisions = auditReader.getRevisions(Item.class, itemId);
          assertEquals(3, itemRevisions.size());
          for (Number itemRevision : itemRevisions) {
            /*
               If you have a revision number, you can get the timestamp when Envers
               logged the change set.
             */
            Date itemRevisionTimestamp = auditReader.getRevisionDate(itemRevision);
            assertNotNull(itemRevisionTimestamp);
          }

          /*
            We created and modified the <code>User</code>, so there are two revisions.
           */
          List<Number> userRevisions = auditReader.getRevisions(User.class, userId);
          assertEquals(2, userRevisions.size());

          em.clear();

          {
            /*
               If you don't know modification timestamps or revision numbers, you can write
               a query with <code>forRevisionsOfEntity()</code> to obtain all audit trail
               details of a particular entity.
             */
            AuditQuery auditQuery = auditReader.createQuery()
                .forRevisionsOfEntity(Item.class, false, false);

            /*
               This query returns the audit trail details as a <code>List</code> of
               <code>Object[]</code>.
             */
            @SuppressWarnings("unchecked")
            List<Object[]> result = auditQuery.getResultList();
            for (Object[] tuple : result) {
              /*
                 Each result tuple contains the entity instance for a particular revision, the
                 revision details (including revision number and timestamp), as well as the
                 revision type.
               */
              Item item = (Item) tuple[0];
              DefaultRevisionEntity revision = (DefaultRevisionEntity) tuple[1];
              RevisionType revisionType = (RevisionType) tuple[2];

              /*
                 The revision type indicates why Envers created the revision, because
                 the entity instance was inserted, modified, or deleted in the database.
               */
              if (revision.getId() == 1) {
                assertEquals(RevisionType.ADD, revisionType);
                assertEquals("Foo", item.getName());
              } else if (revision.getId() == 2) {
                assertEquals(RevisionType.MOD, revisionType);
                assertEquals("Bar", item.getName());
              } else if (revision.getId() == 3) {
                assertEquals(RevisionType.DEL, revisionType);
                assertNull(item);
              }
            }
          }
          em.clear();

          {
            /*
               The <code>find()</code> method returns an audited entity instance version,
               given a revision. This operation loads the <code>Item</code> as it was after
               creation.
             */
            Item item = auditReader.find(Item.class, itemId, revisionCreate);
            assertEquals("Foo", item.getName());
            assertEquals("johndoe", item.getSeller().getUsername());

            /*
               This operation loads the <code>Item</code> after it was updated. Note how
               the modified <code>seller</code> of this change set was also retrieved
               automatically.
             */
            Item modifiedItem = auditReader.find(Item.class, itemId, revisionUpdate);
            assertEquals("Bar", modifiedItem.getName());
            assertEquals("doejohn", modifiedItem.getSeller().getUsername());

            /*
               In this revision, the <code>Item</code> was deleted, so <code>find()</code>
               returns <code>null</code>.
             */
            Item deletedItem = auditReader.find(Item.class, itemId, revisionDelete);
            assertNull(deletedItem);

            /*
               However, the example did not modify the <code>User</code> in this revision,
               so Envers returns its closest historical revision.
             */
            User user = auditReader.find(User.class, userId, revisionDelete);
            assertEquals("doejohn", user.getUsername());
          }
          em.clear();

          {
            AuditQuery auditQuery = auditReader.createQuery()
                .forEntitiesAtRevision(Item.class, revisionUpdate);

            /*
               You can add further restrictions to the query; here the <code>Item#name</code>
               must start with "Ba".
             */
            auditQuery.add(AuditEntity.property("name").like("Ba", MatchMode.START));

            /*
               Restrictions can include entity associations, for example, we are looking for
               the revision of an <code>Item</code> sold by a particular <code>User</code>.
             */
            auditQuery.add(AuditEntity.relatedId("seller").eq(userId));

            /**
             * You can order query results.
             */
            auditQuery.addOrder(AuditEntity.property("name").desc());

            /**
             * You can paginate through large results.
             */
            auditQuery.setFirstResult(0);
            auditQuery.setMaxResults(10);

            assertEquals(1, auditQuery.getResultList().size());
            Item result = (Item) auditQuery.getResultList().get(0);
            assertEquals("doejohn", result.getSeller().getUsername());
          }
          em.clear();

          {
            AuditQuery auditQuery = auditReader.createQuery()
                .forEntitiesAtRevision(Item.class, revisionUpdate);

            auditQuery.addProjection(AuditEntity.property("name"));

            assertEquals(1, auditQuery.getResultList().size());
            String result = (String) auditQuery.getSingleResult();
            assertEquals("Bar", result);
          }
          em.clear();

          {
            User user = auditReader.find(User.class, userId, revisionCreate);

            em.unwrap(Session.class).replicate(user, ReplicationMode.OVERWRITE);
            em.flush();
            em.clear();

            user = em.find(User.class, userId);
            assertEquals("johndoe", user.getUsername());
          }
          em.getTransaction().commit();
        }
      }
    }
  }
}
