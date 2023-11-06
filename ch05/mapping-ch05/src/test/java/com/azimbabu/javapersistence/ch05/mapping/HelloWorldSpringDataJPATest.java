package com.azimbabu.javapersistence.ch05.mapping;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch05.mapping.configuration.SpringDataConfiguration;
import com.azimbabu.javapersistence.ch05.mapping.model.Item;
import com.azimbabu.javapersistence.ch05.mapping.repository.ItemRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class HelloWorldSpringDataJPATest {

  @Autowired
  private ItemRepository itemRepository;

  @Test
  void storeLoadItem() {
    Item item = new Item();
    item.setName("Some Item");
    item.setAuctionEnd(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)));

    itemRepository.save(item);

    List<Item> items = (List<Item>) itemRepository.findAll();

    assertAll((() -> assertEquals(1, items.size())),
        () -> assertEquals("Some Item", items.get(0).getName()));
  }
}
