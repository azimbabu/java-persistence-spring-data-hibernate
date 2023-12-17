package com.azimbabu.javapersistence.ch06.mappingvaluetypes;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.azimbabu.javapersistence.ch06.mappingvaluetypes.configuration.SpringDataConfiguration;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes.model.Address;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes.model.AuctionType;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes.model.Item;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes.model.User;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes.repository.ItemRepository;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class MappingValuesSpringDataJPATest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Test
  void storeLoadEntities() {
    User user = new User();
    user.setUsername("username");
    user.setHomeAddress(new Address("Flowers Street", "12345", "Boston"));
    userRepository.save(user);

    Item item = new Item();
    item.setName("Some Item");
    item.setMetricWeight(2);
    item.setDescription("descriptiondescription");
    itemRepository.save(item);

    List<User> users = (List<User>) userRepository.findAll();
    List<Item> items = (List<Item>) itemRepository.findAll();

    assertAll(() -> assertEquals(1, users.size()),
        () -> assertEquals("username", users.get(0).getUsername()),
        () -> assertEquals("Flowers Street", users.get(0).getHomeAddress().getStreet()),
        () -> assertEquals("12345", users.get(0).getHomeAddress().getZipcode()),
        () -> assertEquals("Boston", users.get(0).getHomeAddress().getCity()),
        () -> assertEquals(1, items.size()),
        () -> assertEquals("AUCTION: Some Item", items.get(0).getName()),
        () -> assertEquals("descriptiondescription", items.get(0).getDescription()),
        () -> assertEquals("descriptiond...", items.get(0).getShortDescription()),
        () -> assertEquals(AuctionType.HIGHTEST_BID, items.get(0).getAuctionType()),
        () -> assertEquals(2, items.get(0).getMetricWeight()),
        () -> assertEquals(LocalDate.now(), items.get(0).getCreatedOn()),
        () -> assertTrue(
            ChronoUnit.SECONDS.between(LocalDateTime.now(), items.get(0).getLastModified()) < 1),
        () -> assertEquals(new BigDecimal("1.00"), items.get(0).getInitialPrice()));
  }
}
