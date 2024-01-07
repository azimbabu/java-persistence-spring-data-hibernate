package com.azimbabu.javapersistence.ch08.mappingassociations.onetomany.cascaderemove;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch08.mappingassociations.onetomany.cascaderemove.configuration.SpringDataConfiguration;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class MappingAssociationsSpringDataJPATest {

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private BidRepository bidRepository;

  @Test
  void storeLoadEntities() {
    Item item = new Item("Foo");
    Bid bid = new Bid(item, BigDecimal.valueOf(100));
    Bid bid2 = new Bid(item, BigDecimal.valueOf(200));
    item.addBid(bid);
    item.addBid(bid2);

    itemRepository.save(item);

    List<Item> items = itemRepository.findAll();
    Set<Bid> bids = bidRepository.findByItem(item);

    assertAll(() -> assertEquals(1, items.size()),
        () -> assertEquals(2, bids.size()));

    itemRepository.delete(item);

    List<Item> items2 = itemRepository.findAll();
    Set<Bid> bids2 = bidRepository.findByItem(item);

    assertAll(() -> assertEquals(0, items2.size()),
        () -> assertEquals(0, bids2.size()));
  }
}
