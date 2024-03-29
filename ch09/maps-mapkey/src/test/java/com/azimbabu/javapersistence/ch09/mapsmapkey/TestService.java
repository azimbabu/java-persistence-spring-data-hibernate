package com.azimbabu.javapersistence.ch09.mapsmapkey;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch09.mapsmapkey.model.Bid;
import com.azimbabu.javapersistence.ch09.mapsmapkey.model.Item;
import com.azimbabu.javapersistence.ch09.mapsmapkey.repository.BidRepository;
import com.azimbabu.javapersistence.ch09.mapsmapkey.repository.ItemRepository;
import java.math.BigDecimal;
import java.util.Map;
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
    Item someItem = new Item("Some Item");
    itemRepository.save(someItem);

    Bid someBid = new Bid(someItem, new BigDecimal("123.00"));
    bidRepository.save(someBid);
    someItem.addBid(someBid);

    Bid otherBid = new Bid(someItem, new BigDecimal("456.00"));
    bidRepository.save(otherBid);
    someItem.addBid(otherBid);

    Item someItemFound = itemRepository.findById(someItem.getId()).get();
    assertEquals(2, someItemFound.getBids().size());

    for (Map.Entry<Long, Bid> entry : someItemFound.getBids().entrySet()) {
      // The key is the identifier of each Bid
      assertEquals(entry.getKey(), entry.getValue().getId());
    }
  }
}
