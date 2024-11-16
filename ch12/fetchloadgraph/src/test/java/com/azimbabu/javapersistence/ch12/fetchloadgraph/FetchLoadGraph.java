package com.azimbabu.javapersistence.ch12.fetchloadgraph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUtil;
import jakarta.persistence.Subgraph;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class FetchLoadGraph {

  @Test
  void loadItem() {
    FetchTestData fetchTestData = new FetchTestData();
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12.fetchloadgraph")) {
      TestData testData = fetchTestData.storeTestData(emf);
      long itemId = testData.getItemIds()[0];
      PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();

      {
        try (EntityManager em = emf.createEntityManager()) {
          em.getTransaction().begin();

          EntityGraph<?> itemGraph = em.getEntityGraph(Item.class.getSimpleName());
          Map<String, Object> properties = new HashMap<>();
          properties.put("jakarta.persistence.loadgraph", itemGraph); // "Item"

          Item item = em.find(Item.class, itemId, properties);
          // select * from ITEM where ID = ?

          assertTrue(persistenceUtil.isLoaded(item));
          assertTrue(persistenceUtil.isLoaded(item, "name"));
          assertTrue(persistenceUtil.isLoaded(item, "auctionEnd"));
          assertFalse(persistenceUtil.isLoaded(item, "seller"));
          assertFalse(persistenceUtil.isLoaded(item, "bids"));

          em.getTransaction().commit();
        }

        {
          try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            EntityGraph<Item> itemGraph = em.createEntityGraph(Item.class);
            Map<String, Object> properties = new HashMap<>();
            properties.put("jakarta.persistence.loadgraph", itemGraph);

            Item item = em.find(Item.class, itemId, properties);
            // select * from ITEM where ID = ?

            assertTrue(persistenceUtil.isLoaded(item));
            assertTrue(persistenceUtil.isLoaded(item, "name"));
            assertTrue(persistenceUtil.isLoaded(item, "auctionEnd"));
            assertFalse(persistenceUtil.isLoaded(item, "seller"));
            assertFalse(persistenceUtil.isLoaded(item, "bids"));

            em.getTransaction().commit();
          }

        }
      }
    }
  }

  @Test
  void loadItemSeller() {
    FetchTestData fetchTestData = new FetchTestData();
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12.fetchloadgraph")) {
      TestData testData = fetchTestData.storeTestData(emf);
      long itemId = testData.getItemIds()[0];
      PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();

      {
        try (EntityManager em = emf.createEntityManager()) {
          em.getTransaction().begin();

          EntityGraph<?> itemSellerGraph = em.getEntityGraph("ItemSeller");
          Map<String, Object> properties = new HashMap<>();
          properties.put("jakarta.persistence.loadgraph", itemSellerGraph);

          Item item = em.find(Item.class, itemId, properties);
          // select i.*, u.*
          //  from ITEM i
          //   inner join USERS u on u.ID = i.SELLER_ID
          // where i.ID = ?

          assertTrue(persistenceUtil.isLoaded(item));
          assertTrue(persistenceUtil.isLoaded(item, "name"));
          assertTrue(persistenceUtil.isLoaded(item, "auctionEnd"));
          assertTrue(persistenceUtil.isLoaded(item, "seller"));
          assertFalse(persistenceUtil.isLoaded(item, "bids"));

          em.getTransaction().commit();
        }
      }

      {
        try (EntityManager em = emf.createEntityManager()) {
          em.getTransaction().begin();

          EntityGraph<Item> itemSellerGraph = em.createEntityGraph(Item.class);
          itemSellerGraph.addAttributeNodes(Item_.seller);
          Map<String, Object> properties = new HashMap<>();
          properties.put("jakarta.persistence.loadgraph", itemSellerGraph);

          Item item = em.find(Item.class, itemId, properties);
          // select i.*, u.*
          //  from ITEM i
          //   inner join USERS u on u.ID = i.SELLER_ID
          // where i.ID = ?

          assertTrue(persistenceUtil.isLoaded(item));
          assertTrue(persistenceUtil.isLoaded(item, "name"));
          assertTrue(persistenceUtil.isLoaded(item, "auctionEnd"));
          assertTrue(persistenceUtil.isLoaded(item, "seller"));
          assertFalse(persistenceUtil.isLoaded(item, "bids"));

          em.getTransaction().commit();
        }
      }

      {
        try (EntityManager em = emf.createEntityManager()) {
          em.getTransaction().begin();

          EntityGraph<Item> itemGraph = em.createEntityGraph(Item.class);
          itemGraph.addAttributeNodes("seller");

          List<Item> items = em.createQuery("select i from Item i", Item.class)
              .setHint("jakarta.persistence.loadgraph", itemGraph)
              .getResultList();
          // select i.*, u.*
          //  from ITEM i
          //   left outer join USERS u on u.ID = i.SELLER_ID

          assertEquals(3, items.size());

          for (Item item : items) {
            assertTrue(persistenceUtil.isLoaded(item));
            assertTrue(persistenceUtil.isLoaded(item, "name"));
            assertTrue(persistenceUtil.isLoaded(item, "auctionEnd"));
            assertTrue(persistenceUtil.isLoaded(item, "seller"));
            assertFalse(persistenceUtil.isLoaded(item, "bids"));
          }
          em.getTransaction().commit();
        }
      }
    }
  }

  @Test
  void loadBidBidderItem() {
    FetchTestData fetchTestData = new FetchTestData();
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12.fetchloadgraph")) {
      TestData testData = fetchTestData.storeTestData(emf);
      long bidId = testData.getBidIds()[0];
      PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();

      {
        try (EntityManager em = emf.createEntityManager()) {
          em.getTransaction().begin();

          Map<String, Object> properties = new HashMap<>();
          properties.put("jakarta.persistence.loadgraph", em.getEntityGraph("BidBidderItem"));

          Bid bid = em.find(Bid.class, bidId, properties);

          assertTrue(persistenceUtil.isLoaded(bid));
          assertTrue(persistenceUtil.isLoaded(bid, "amount"));
          assertTrue(persistenceUtil.isLoaded(bid, "bidder"));
          assertTrue(persistenceUtil.isLoaded(bid, "item"));
          assertTrue(persistenceUtil.isLoaded(bid.getItem(), "name"));
          assertFalse(persistenceUtil.isLoaded(bid.getItem(), "seller"));

          em.getTransaction().commit();
        }
      }

      {
        try (EntityManager em = emf.createEntityManager()) {
          em.getTransaction().begin();

          EntityGraph<Bid> bidGraph = em.createEntityGraph(Bid.class);
          bidGraph.addAttributeNodes("bidder", "item");

          Map<String, Object> properties = new HashMap<>();
          properties.put("jakarta.persistence.loadgraph", bidGraph);

          Bid bid = em.find(Bid.class, bidId, properties);

          assertTrue(persistenceUtil.isLoaded(bid));
          assertTrue(persistenceUtil.isLoaded(bid, "amount"));
          assertTrue(persistenceUtil.isLoaded(bid, "bidder"));
          assertTrue(persistenceUtil.isLoaded(bid, "item"));
          assertTrue(persistenceUtil.isLoaded(bid.getItem(), "name"));
          assertFalse(persistenceUtil.isLoaded(bid.getItem(), "seller"));

          em.getTransaction().commit();
        }
      }
    }
  }

  @Test
  void loadBidBidderItemSellerBids() {
    FetchTestData fetchTestData = new FetchTestData();
    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch12.fetchloadgraph")) {
      TestData testData = fetchTestData.storeTestData(emf);
      Long bidId = testData.getBidIds()[0];
      PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();

      {
        try (EntityManager em = emf.createEntityManager()) {
          em.getTransaction().begin();

          Map<String, Object> properties = new HashMap<>();
          properties.put("jakarta.persistence.loadgraph", em.getEntityGraph("BidBidderItemSellerBids"));

          Bid bid = em.find(Bid.class, bidId, properties);

          assertTrue(persistenceUtil.isLoaded(bid));
          assertTrue(persistenceUtil.isLoaded(bid, "amount"));
          assertTrue(persistenceUtil.isLoaded(bid, "bidder"));
          assertTrue(persistenceUtil.isLoaded(bid, "item"));
          assertTrue(persistenceUtil.isLoaded(bid.getItem(), "name"));
          assertTrue(persistenceUtil.isLoaded(bid.getItem(), "seller"));
          assertTrue(persistenceUtil.isLoaded(bid.getItem().getSeller(), "username"));
          assertTrue(persistenceUtil.isLoaded(bid.getItem(), "bids"));
          em.getTransaction().commit();
        }
      }

      {
        try (EntityManager em = emf.createEntityManager()) {
          em.getTransaction().begin();

          EntityGraph<Bid> bidGraph = em.createEntityGraph(Bid.class);
          bidGraph.addAttributeNodes(Bid_.bidder, Bid_.item);
          Subgraph<Item> itemSubgraph = bidGraph.addSubgraph(Bid_.item);
          itemSubgraph.addAttributeNodes(Item_.seller, Item_.bids);

          Map<String, Object> properties = new HashMap<>();
          properties.put("jakarta.persistence.loadgraph", bidGraph);

          Bid bid = em.find(Bid.class, bidId, properties);

          assertTrue(persistenceUtil.isLoaded(bid));
          assertTrue(persistenceUtil.isLoaded(bid, "amount"));
          assertTrue(persistenceUtil.isLoaded(bid, "bidder"));
          assertTrue(persistenceUtil.isLoaded(bid, "item"));
          assertTrue(persistenceUtil.isLoaded(bid.getItem(), "name"));
          assertTrue(persistenceUtil.isLoaded(bid.getItem(), "seller"));
          assertTrue(persistenceUtil.isLoaded(bid.getItem().getSeller(), "username"));
          assertTrue(persistenceUtil.isLoaded(bid.getItem(), "bids"));
          em.getTransaction().commit();
        }
      }
    }
  }
}
