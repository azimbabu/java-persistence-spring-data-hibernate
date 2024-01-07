package com.azimbabu.javapersistence.ch08.mappingassociations.onetomany.orphanremoval;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch08.mappingassociations.onetomany.orphanremoval.configuration.SpringDataConfiguration;
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

  @Autowired
  private UserRepository userRepository;

  @Test
  void storeLoadEntities() {
    User john = new User("John");
    userRepository.save(john);

    Item item = new Item("Foo");

    Bid bid = new Bid(item, BigDecimal.valueOf(100));
    Bid bid2 = new Bid(item, BigDecimal.valueOf(200));
    item.addBid(bid);
    bid.setBidder(john);
    item.addBid(bid2);
    bid2.setBidder(john);

    itemRepository.save(item);

    List<Item> items = itemRepository.findAll();
    Set<Bid> bids = bidRepository.findByItem(item);
    User user = userRepository.findUserWithBids(john.getId());

    assertAll(() -> assertEquals(1, items.size()),
        () -> assertEquals(2, bids.size()),
        () -> assertEquals(2, user.getBids().size()));

    Item itemFound = itemRepository.findItemWithBids(item.getId());
    Bid firstBid = itemFound.getBids().iterator().next();
    itemFound.removeBid(firstBid);

    itemRepository.save(itemFound);

    List<Item> items2 = itemRepository.findAll();
    List<Bid> bids2 = bidRepository.findAll();

    assertAll(() -> assertEquals(1, items2.size()),
        () -> assertEquals(1, bids2.size()),
        //() -> assertEquals(1, user.getBids().size()), // FAILURE
        () -> assertEquals(2, user.getBids().size()));
  }
}
