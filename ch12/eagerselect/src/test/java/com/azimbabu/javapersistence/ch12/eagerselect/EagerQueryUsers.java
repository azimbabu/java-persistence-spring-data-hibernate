package com.azimbabu.javapersistence.ch12.eagerselect;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.junit.jupiter.api.Test;

public class EagerQueryUsers {

  @Test
  void fetchUsers() {
    FetchTestData fetchTestData = new FetchTestData();
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12.eagerselect")) {
      fetchTestData.storeTestData(emf);

      {
        List<Item> items;
        try (EntityManager em = emf.createEntityManager()) {
          em.getTransaction().begin();

          items = em.createQuery("select i from Item i join fetch i.seller",
              Item.class).getResultList();
          // select i.*, u.*
          //  from ITEM i
          //   inner join USERS u on u.ID = i.SELLER_ID
          //  where i.ID = ?

          em.getTransaction().commit();
        }

        for (Item item : items) {
          assertNotNull(item.getSeller().getUsername());
        }
      }

      {
        List<Item> items;
        try (EntityManager em = emf.createEntityManager()) {
          em.getTransaction().begin();

          CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
          CriteriaQuery<Item> criteriaQuery = criteriaBuilder.createQuery(Item.class);

          Root<Item> itemRoot = criteriaQuery.from(Item.class);
          itemRoot.fetch("seller");
          criteriaQuery.select(itemRoot);

          items = em.createQuery(criteriaQuery).getResultList();
          // select i.*, u.*
          //  from ITEM i
          //   inner join USERS u on u.ID = i.SELLER_ID
          //  where i.ID = ?

          em.getTransaction().commit();
        }

        for (Item item : items) {
          assertNotNull(item.getSeller().getUsername());
        }
      }

    }
  }
}
