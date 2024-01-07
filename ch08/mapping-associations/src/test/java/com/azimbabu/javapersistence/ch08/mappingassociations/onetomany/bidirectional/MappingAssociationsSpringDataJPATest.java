package com.azimbabu.javapersistence.ch08.mappingassociations.onetomany.bidirectional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch08.mappingassociations.onetomany.bidirectional.configuration.SpringDataConfiguration;
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

    itemRepository.save(item);
    item.addBid(bid);
    item.addBid(bid2);
    bidRepository.save(bid);
    bidRepository.save(bid2);

    List<Item> items = itemRepository.findAll();
    Set<Bid> bids = bidRepository.findByItem(item);

    assertAll(() -> assertEquals(1, items.size()),
        () -> assertEquals(2, bids.size()));
  }
}
