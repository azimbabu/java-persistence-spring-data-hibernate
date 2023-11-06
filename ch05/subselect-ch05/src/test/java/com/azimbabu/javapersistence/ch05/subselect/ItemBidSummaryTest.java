package com.azimbabu.javapersistence.ch05.subselect;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch05.subselect.model.Bid;
import com.azimbabu.javapersistence.ch05.subselect.model.Item;
import com.azimbabu.javapersistence.ch05.subselect.model.ItemBidSummary;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class ItemBidSummaryTest {

  @Test
  void itemBidSummaryTest() {
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch05.subselect");
        EntityManager em = emf.createEntityManager()) {
      // create an item and two bids
      em.getTransaction().begin();

      Item item = new Item();
      item.setName("Some Item");
      item.setAuctionEnd(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)));

      Bid bid1 = new Bid(item, BigDecimal.valueOf(1000.0));
      Bid bid2 = new Bid(item, BigDecimal.valueOf(1100.0));

      em.persist(item);
      em.persist(bid1);
      em.persist(bid2);

      em.getTransaction().commit();

      // load item bid summary
      em.getTransaction().begin();

      TypedQuery<ItemBidSummary> query = em.createQuery(
          "select ibs from ItemBidSummary ibs where ibs.itemId = :id", ItemBidSummary.class);
      ItemBidSummary itemBidSummary = query.setParameter("id", item.getId()).getSingleResult();

      assertAll(() -> assertEquals(item.getId(), itemBidSummary.getItemId()),
          () -> assertEquals(item.getName(), itemBidSummary.getName()),
          () -> assertEquals(2, itemBidSummary.getNumberOfBids()));
      em.getTransaction().commit();
    }
  }
}
