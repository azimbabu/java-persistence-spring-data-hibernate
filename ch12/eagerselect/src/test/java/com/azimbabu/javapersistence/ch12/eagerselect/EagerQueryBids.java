package com.azimbabu.javapersistence.ch12.eagerselect;

import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.junit.jupiter.api.Test;

public class EagerQueryBids {
  @Test
  void fetchBids() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12.eagerselect")) {
      FetchTestData fetchTestData = new FetchTestData();
      fetchTestData.storeTestData(emf);

      {
        List<Item> items;
        try (EntityManager em = emf.createEntityManager()) {
          em.getTransaction().begin();

          items = em.createQuery("select i from Item i left join fetch i.bids",
              Item.class).getResultList();
          // select i.*, b.*
          //  from ITEM i
          //   left outer join BID b on b.ITEM_ID = i.ID
          //  where i.ID = ?

          em.getTransaction().commit();
        }

        for (Item item : items) {
          assertTrue(item.getBids().size() > 0);
        }
      }

      {
        List<Item> items;
        try (EntityManager em = emf.createEntityManager()) {
          em.getTransaction().begin();

          CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
          CriteriaQuery<Item> criteriaQuery = criteriaBuilder.createQuery(Item.class);
          Root<Item> itemRoot = criteriaQuery.from(Item.class);
          itemRoot.fetch("bids", JoinType.LEFT);
          criteriaQuery.select(itemRoot);

          items = em.createQuery(criteriaQuery).getResultList();
          // select i.*, b.*
          //  from ITEM i
          //   left outer join BID b on b.ITEM_ID = i.ID
          //  where i.ID = ?

          em.getTransaction().commit();
        }

        for (Item item : items) {
          assertTrue(item.getBids().size() > 0);
        }
      }
    }
  }
}
