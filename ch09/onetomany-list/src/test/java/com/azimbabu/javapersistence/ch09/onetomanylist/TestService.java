package com.azimbabu.javapersistence.ch09.onetomanylist;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch09.onetomanylist.model.Bid;
import com.azimbabu.javapersistence.ch09.onetomanylist.model.Item;
import com.azimbabu.javapersistence.ch09.onetomanylist.repository.BidRepository;
import com.azimbabu.javapersistence.ch09.onetomanylist.repository.ItemRepository;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {
  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private BidRepository bidRepository;

  @Transactional
  public void storeLoadEntities() {
    Item item = new Item("Foo");
    itemRepository.save(item);

    Bid someBid = new Bid(item, new BigDecimal("123.00"));
    item.addBid(someBid);
    bidRepository.save(someBid);

    Bid secondBid = new Bid(item, new BigDecimal("456.00"));
    item.addBid(secondBid);
    bidRepository.save(secondBid);

    Item itemFound = itemRepository.findItemWithBids(item.getId());
    assertAll(() -> assertEquals(2, itemFound.getBids().size()),
        () -> assertEquals(0, itemFound.getBids().get(0).getAmount().compareTo(new BigDecimal("123"))),
        () -> assertEquals(0, itemFound.getBids().get(1).getAmount().compareTo(new BigDecimal("456")))
    );
  }
}
