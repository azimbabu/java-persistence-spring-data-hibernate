package com.azimbabu.javapersistence.ch13.filtering.dynamic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

public class DynamicFilter {

  private DynamicFilterTestData storeTestData(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      TestData users = new TestData(new Long[2]);
      User johndoe = new User("johndoe");
      em.persist(johndoe);
      users.getIds()[0] = johndoe.getId();

      User janeroe = new User("janeroe", 100);
      em.persist(janeroe);
      users.getIds()[1] = janeroe.getId();

      TestData categories = new TestData(new Long[2]);
      Category categoryOne = new Category("One");
      em.persist(categoryOne);
      categories.getIds()[0] = categoryOne.getId();

      Category categoryTwo = new Category("Two");
      em.persist(categoryTwo);
      categories.getIds()[1] = categoryTwo.getId();

      TestData items = new TestData(new Long[3]);
      Item itemFoo = new Item("Foo", categoryOne, johndoe);
      em.persist(itemFoo);
      items.getIds()[0] = itemFoo.getId();

      Item itemBar = new Item("Bar", categoryOne, janeroe);
      em.persist(itemBar);
      items.getIds()[1] = itemBar.getId();

      Item itemBaz = new Item("Baz", categoryTwo, janeroe);
      em.persist(itemBaz);
      items.getIds()[2] = itemBaz.getId();

      em.getTransaction().commit();

      return new DynamicFilterTestData(categories, items, users);
    }
  }

  @Test
  void filterItems() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch13.filtering.dynamic")) {
      DynamicFilterTestData testData = storeTestData(emf);

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();
        {
          org.hibernate.Filter filter = em.unwrap(Session.class).enableFilter("limitByUserRanking");
          filter.setParameter("currentUserRanking", 0);
          {
            List<Item> items = em.createQuery("select i from Item i", Item.class)
                .getResultList();
            // select * from ITEM where 0 >=
            //  (select u.RANKING from USERS u  where u.ID = SELLER_ID)
            assertEquals(1, items.size());
          }
          em.clear();
          {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Item> criteriaQuery = criteriaBuilder.createQuery(Item.class);
            criteriaQuery.select(criteriaQuery.from(Item.class));
            List<Item> items = em.createQuery(criteriaQuery).getResultList();
            // select * from ITEM where 0 >=
            //  (select u.RANKING from USERS u  where u.ID = SELLER_ID)
            assertEquals(1, items.size());
          }
          em.clear();
          {
            filter.setParameter("currentUserRanking", 100);
            List<Item> items = em.createQuery("select i from Item i", Item.class)
                .getResultList();
            // select * from ITEM where 100 >=
            //  (select u.RANKING from USERS u  where u.ID = SELLER_ID)
            assertEquals(3, items.size());

          }
        }
        em.getTransaction().commit();
      }
    }
  }

  @Test
  void filterCollection() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch13.filtering.dynamic")) {
      DynamicFilterTestData testData = storeTestData(emf);
      Long categoryId = testData.getCategories().getFirstId();

      try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();
        {
          org.hibernate.Filter filter = em.unwrap(Session.class).enableFilter("limitByUserRanking");
          filter.setParameter("currentUserRanking", 0);
          Category category = em.find(Category.class, categoryId);
          assertEquals(1, category.getItems().size());

          em.clear();

          filter.setParameter("currentUserRanking", 100);
          category = em.find(Category.class, categoryId);
          assertEquals(2, category.getItems().size());
        }
        em.getTransaction().commit();
      }
    }
  }
}
