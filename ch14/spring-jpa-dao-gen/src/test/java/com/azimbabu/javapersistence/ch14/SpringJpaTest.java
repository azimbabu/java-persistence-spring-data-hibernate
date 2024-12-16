package com.azimbabu.javapersistence.ch14;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.azimbabu.javapersistence.ch14.configuration.SpringConfiguration;
import com.azimbabu.javapersistence.ch14.dao.GenericDao;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringConfiguration.class)
//@ContextConfiguration("classpath:application-context.xml")
public class SpringJpaTest {

  @Autowired
  private DatabaseService databaseService;

  @Autowired
  private GenericDao<Item> itemDao;

  @Autowired
  private GenericDao<Bid> bidDao;

  @BeforeEach
  void setUp() {
    databaseService.init();
  }

  @AfterEach
  void tearDown() {
    databaseService.clear();
  }

  @Test
  void testInsertItems() {
    List<Item> items = itemDao.getAll();
    List<Bid> bids = bidDao.getAll();
    assertAll(() -> assertNotNull(items),
        () -> assertEquals(10, items.size()),
        () -> assertNotNull(itemDao.findByProperty("name", "Item 1")),
        () -> assertNotNull(bids),
        () -> assertEquals(20, bids.size()),
        () -> assertEquals(10, bidDao.findByProperty("amount", new BigDecimal(1000.0)).size()));
  }

  @Test
  void testDeleteItem() {
    itemDao.delete(itemDao.findByProperty("name", "Item 2").get(0));
    assertEquals(0, itemDao.findByProperty("name", "Item 2").size());
  }

  @Test
  void testUpdateItem() {
    List<Item> items = itemDao.findByProperty("name", "Item 1");
    itemDao.update(items.get(0).getId(), "name",  "Item 1 Updated");
    assertEquals("Item 1 Updated", itemDao.getById(items.get(0).getId()).getName());
    assertEquals(1, itemDao.findByProperty("name", "Item 1 Updated").size());
  }

  @Test
  void testInsertBid() {
    Item item = itemDao.findByProperty("name", "Item 3").get(0);
    Bid bid = new Bid(new BigDecimal("2000.00"), item);
    bidDao.insert(bid);
    assertAll(() -> assertEquals(new BigDecimal("2000.00"), bidDao.getById(bid.getId()).getAmount()),
        () -> assertEquals(21, bidDao.getAll().size()));
  }

  @Test
  void testUpdateBid() {
    Bid bid = bidDao.findByProperty("amount", new BigDecimal("1000.00")).get(0);
    bidDao.update(bid.getId(), "amount", new BigDecimal("1200.00"));
    assertEquals(new BigDecimal("1200.00"), bidDao.getById(bid.getId()).getAmount());
    assertEquals(1, bidDao.findByProperty("amount", new BigDecimal("1200.00")).size());
  }

  @Test
  void testDeleteBid() {
    bidDao.delete(bidDao.findByProperty("amount", new BigDecimal("1000.00")).get(0));
    assertEquals(19, bidDao.getAll().size());
  }
}
