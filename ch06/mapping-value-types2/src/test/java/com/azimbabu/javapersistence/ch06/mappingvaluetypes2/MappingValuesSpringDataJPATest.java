package com.azimbabu.javapersistence.ch06.mappingvaluetypes2;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.azimbabu.javapersistence.ch06.mappingvaluetypes2.configuration.SpringDataConfiguration;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes2.model.Address;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes2.model.AuctionType;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes2.model.City;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes2.model.Item;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes2.model.MonetaryAmount;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes2.model.User;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes2.repository.ItemRepository;
import com.azimbabu.javapersistence.ch06.mappingvaluetypes2.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
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
  private ItemRepository itemRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  void saveLoadEntities() {
    City city = new City();
    city.setName("Boston");
    city.setZipcode("12345");
    city.setCountry("USA");

    User user = new User();
    user.setUsername("username");
    user.setHomeAddress(new Address("Flowers Street", city));

    userRepository.save(user);

    Item item = new Item();
    item.setName("Some Item");
    item.setMetricWeight(2);
    item.setBuyNowPrice(new MonetaryAmount(BigDecimal.valueOf(1.1), Currency.getInstance("USD")));
    item.setDescription("descriptiondescription");

    itemRepository.save(item);

    List<User> users = (List<User>) userRepository.findAll();
    assertAll(() -> assertEquals(1, users.size()),
        () -> assertEquals("username", users.get(0).getUsername()),
        () -> assertEquals("Flowers Street", users.get(0).getHomeAddress().getStreet()),
        () -> assertEquals("Boston", users.get(0).getHomeAddress().getCity().getName()),
        () -> assertEquals("12345", users.get(0).getHomeAddress().getCity().getZipcode()),
        () -> assertEquals("USA", users.get(0).getHomeAddress().getCity().getCountry()));

    List<Item> items = (List<Item>) itemRepository.findByMetricWeight(2.0);

    assertAll(() -> assertEquals(1, items.size()),
        () -> assertEquals("AUCTION: Some Item", items.get(0).getName()),
        () -> assertEquals("1.1 USD", items.get(0).getBuyNowPrice().toString()),
        () -> assertEquals(new BigDecimal("1.00"), items.get(0).getInitialPrice()),
        () -> assertEquals("descriptiondescription", items.get(0).getDescription()),
        () -> assertEquals("descriptiond...", items.get(0).getShortDescription()),
        () -> assertEquals(AuctionType.HIGHTEST_BID, items.get(0).getAuctionType()),
        () -> assertEquals(2.0, items.get(0).getMetricWeight()),
        () -> assertEquals(LocalDate.now(), items.get(0).getCreatedOn()),
        () -> assertTrue(
            ChronoUnit.SECONDS.between(LocalDateTime.now(), items.get(0).getLastModified()) < 1));
  }
}
