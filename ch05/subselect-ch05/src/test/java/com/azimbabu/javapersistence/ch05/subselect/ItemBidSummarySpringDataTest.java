package com.azimbabu.javapersistence.ch05.subselect;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch05.subselect.configuration.SpringDataConfiguration;
import com.azimbabu.javapersistence.ch05.subselect.model.Bid;
import com.azimbabu.javapersistence.ch05.subselect.model.Item;
import com.azimbabu.javapersistence.ch05.subselect.model.ItemBidSummary;
import com.azimbabu.javapersistence.ch05.subselect.repository.BidRepository;
import com.azimbabu.javapersistence.ch05.subselect.repository.ItemBidSummaryRepository;
import com.azimbabu.javapersistence.ch05.subselect.repository.ItemRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class ItemBidSummarySpringDataTest {

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private BidRepository bidRepository;

  @Autowired
  private ItemBidSummaryRepository itemBidSummaryRepository;

  @Test
  void itemBidSummaryTest() {
    // create an item and two bids
    Item item = new Item();
    item.setName("Some Item");
    item.setAuctionEnd(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)));

    Bid bid1 = new Bid(item, BigDecimal.valueOf(1000.0));
    Bid bid2 = new Bid(item, BigDecimal.valueOf(1100.0));

    itemRepository.save(item);
    bidRepository.save(bid1);
    bidRepository.save(bid2);

    // fetch item bid summary
    Optional<ItemBidSummary> itemBidSummaryOptional = itemBidSummaryRepository.findById(
        item.getId());

    assertAll(() -> assertEquals(item.getId(), itemBidSummaryOptional.get().getItemId()),
        () -> assertEquals(item.getName(), itemBidSummaryOptional.get().getName()),
        () -> assertEquals(2, itemBidSummaryOptional.get().getNumberOfBids()));
  }
}
