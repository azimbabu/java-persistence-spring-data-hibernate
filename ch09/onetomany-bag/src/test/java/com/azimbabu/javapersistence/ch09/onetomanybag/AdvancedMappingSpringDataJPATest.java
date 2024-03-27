package com.azimbabu.javapersistence.ch09.onetomanybag;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch09.onetomanybag.configuration.SpringDataConfiguration;
import com.azimbabu.javapersistence.ch09.onetomanybag.model.Bid;
import com.azimbabu.javapersistence.ch09.onetomanybag.model.Item;
import com.azimbabu.javapersistence.ch09.onetomanybag.repository.BidRepository;
import com.azimbabu.javapersistence.ch09.onetomanybag.repository.ItemRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringDataConfiguration.class)
public class AdvancedMappingSpringDataJPATest {
  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private BidRepository bidRepository;

  @Test
  void testStoreLoadEntities() {
    Item item = new Item("Foo");
    itemRepository.save(item);

    Bid someBid = new Bid(new BigDecimal("123.00"), item);
    item.addBid(someBid);
    item.addBid(someBid);
    bidRepository.save(someBid);

    Item itemFound = itemRepository.findItemWithBids(item.getId());
    assertAll(() -> assertEquals(2, item.getBids().size()),
        () -> assertEquals(1, itemFound.getBids().size()));

    Bid bid = new Bid(new BigDecimal("456.00"), item);
    item.addBid(bid); // No SELECT!
    bidRepository.save(bid);

    Item itemFound2 = itemRepository.findItemWithBids(item.getId());
    assertEquals(2, itemFound2.getBids().size());
  }
}
