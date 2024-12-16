package com.azimbabu.javapersistence.ch14;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.azimbabu.javapersistence.ch14.configuration.SpringConfiguration;
import com.azimbabu.javapersistence.ch14.dao.BidDao;
import com.azimbabu.javapersistence.ch14.dao.ItemDao;
import jakarta.persistence.NoResultException;
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
  private ItemDao itemDao;

  @Autowired
  private BidDao bidDao;

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
        () -> assertNotNull(itemDao.findByName("Item 1")),
        () -> assertNotNull(bids),
        () -> assertEquals(20, bids.size()),
        () -> assertEquals(10, bidDao.findByAmount(new BigDecimal(1000.0)).size()));
  }

  @Test
  void testDeleteItem() {
    itemDao.delete(itemDao.findByName("Item 2"));
    assertThrows(NoResultException.class, () -> itemDao.findByName("Item 2"));
  }

  @Test
  void testUpdateItem() {
    Item item = itemDao.findByName("Item 1");
    itemDao.update(item.getId(), "Item 1 Updated");
    assertEquals("Item 1 Updated", itemDao.getBydId(item.getId()).getName());
  }

  @Test
  void testInsertBid() {
    Item item = itemDao.findByName("Item 3");
    Bid bid = new Bid(new BigDecimal("2000.00"), item);
    bidDao.insert(bid);
    assertAll(() -> assertEquals(new BigDecimal("2000.00"), bidDao.getBydId(bid.getId()).getAmount()),
        () -> assertEquals(21, bidDao.getAll().size()));
  }

  @Test
  void testUpdateBid() {
    Bid bid = bidDao.findByAmount(new BigDecimal("1000.00")).get(0);
    bidDao.update(bid.getId(), new BigDecimal("1200.00"));
    assertEquals(new BigDecimal("1200.00"), bidDao.getBydId(bid.getId()).getAmount());
  }

  @Test
  void testDeleteBid() {
    bidDao.delete(bidDao.findByAmount(new BigDecimal("1000.00")).get(0));
    assertEquals(19, bidDao.getAll().size());
  }
}
