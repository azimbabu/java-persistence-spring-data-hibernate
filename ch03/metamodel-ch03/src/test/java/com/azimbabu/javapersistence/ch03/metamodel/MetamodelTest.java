package com.azimbabu.javapersistence.ch03.metamodel;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.Type.PersistenceType;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MetamodelTest {

  private static EntityManagerFactory emf;

  @BeforeAll
  static void beforeAll() {
    emf = Persistence.createEntityManagerFactory("ch03.metamodel");
  }

  @AfterAll
  static void afterAll() {
    emf.close();
  }

  @Test
  void accessDynamicMetamodel() {
    Metamodel metamodel = emf.getMetamodel();
    Set<ManagedType<?>> managedTypes = metamodel.getManagedTypes();
    ManagedType<?> itemType = managedTypes.iterator().next();

    assertAll(() -> assertEquals(1, managedTypes.size()),
        () -> assertEquals(PersistenceType.ENTITY, itemType.getPersistenceType()));

    SingularAttribute<?, ?> idAttribute = itemType.getSingularAttribute("id");
    assertFalse(idAttribute.isOptional());

    SingularAttribute<?, ?> nameAttribute = itemType.getSingularAttribute("name");
    assertAll(() -> assertEquals(String.class, nameAttribute.getJavaType()),
        () -> assertEquals(Attribute.PersistentAttributeType.BASIC,
            nameAttribute.getPersistentAttributeType()));

    SingularAttribute<?, ?> auctionEndAttribute = itemType.getSingularAttribute("auctionEnd");
    assertAll(() -> assertEquals(Timestamp.class, auctionEndAttribute.getJavaType()),
        () -> assertTrue(Date.class.isAssignableFrom(auctionEndAttribute.getJavaType())),
        () -> assertEquals(Attribute.PersistentAttributeType.BASIC,
            auctionEndAttribute.getPersistentAttributeType()),
        () -> assertFalse(auctionEndAttribute.isCollection()),
        () -> assertFalse(auctionEndAttribute.isAssociation()));
  }

  @Test
  void accessStaticMetamodel() {
    try (EntityManager em = emf.createEntityManager()) {
      deleteItems(em);
      persistItems(em);

      em.getTransaction().begin();

      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Item> query = cb.createQuery(Item.class);
      Root<Item> fromItem = query.from(Item.class);
      query.select(fromItem);
      List<Item> items = em.createQuery(query).getResultList();

      em.getTransaction().commit();

      assertEquals(2, items.size());
    }
  }

  @Test
  void testItemsPattern() {
    try (EntityManager em = emf.createEntityManager()) {
      deleteItems(em);
      persistItems(em);

      em.getTransaction().begin();

      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Item> query = cb.createQuery(Item.class);
      Root<Item> fromItem = query.from(Item.class);
      Path<String> namePath = fromItem.get("name");
      query.where(cb.like(namePath, cb.parameter(String.class, "pattern")));

      List<Item> items = em.createQuery(query).setParameter("pattern", "%Item 1%").getResultList();

      em.getTransaction().commit();

      assertAll(() -> assertEquals(1, items.size()),
          () -> assertEquals("Item 1", items.iterator().next().getName()));
    }
  }

  @Test
  void testItemsPatternWithGeneratedClass() {
    try (EntityManager em = emf.createEntityManager()) {
      deleteItems(em);
      persistItems(em);

      em.getTransaction().begin();

      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Item> query = cb.createQuery(Item.class);
      Root<Item> fromItem = query.from(Item.class);
      Path<String> namePath = fromItem.get(Item_.name);
      query.where(cb.like(namePath, cb.parameter(String.class, "pattern")));

      List<Item> items = em.createQuery(query).setParameter("pattern", "%Item 1%").getResultList();

      em.getTransaction().commit();

      assertAll(() -> assertEquals(1, items.size()),
          () -> assertEquals("Item 1", items.iterator().next().getName()));
    }
  }

  private void deleteItems(EntityManager em) {
    em.getTransaction().begin();

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaDelete<Item> query = cb.createCriteriaDelete(Item.class);
    query.from(Item.class);
    em.createQuery(query).executeUpdate();

    em.getTransaction().commit();
  }

  private void persistItems(EntityManager em) {
    em.getTransaction().begin();

    Item item1 = new Item();
    item1.setName("Item 1");
    item1.setAuctionEnd(tomorrow());

    Item item2 = new Item();
    item2.setName("Item 2");
    item2.setAuctionEnd(tomorrow());

    em.persist(item1);
    em.persist(item2);

    em.getTransaction().commit();
  }

  private Date tomorrow() {
    return Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
  }
}
