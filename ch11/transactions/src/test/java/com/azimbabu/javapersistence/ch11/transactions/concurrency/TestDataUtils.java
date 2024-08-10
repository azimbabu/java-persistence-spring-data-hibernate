package com.azimbabu.javapersistence.ch11.transactions.concurrency;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestDataUtils {
  public static TestData storeCategoriesAndItems(EntityManagerFactory emf) {
    List<Long> categoryIds = new ArrayList<>();
    List<Long> itemIds = new ArrayList<>();
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      for (int i=1; i <= 3; i++) {
        Category category = new Category("Category " + i);
        em.persist(category);
        categoryIds.add(category.getId());

        for (int j=1; j <= 3; j++) {
          Item item = new Item("Item " + j);
          item.setCategory(category);
          item.setBuyNowPrice(new BigDecimal(10 + j));
          em.persist(item);
          itemIds.add(item.getId());
        }
      }

      em.getTransaction().commit();
    }
    return new TestData(categoryIds, itemIds);
  }
}
