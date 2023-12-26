package com.azimbabu.javapersistence.ch07.mappinginheritanceembeddable;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch07.mappinginheritanceembeddable.configuration.SpringDataConfiguration;
import com.azimbabu.javapersistence.ch07.mappinginheritanceembeddable.model.Dimensions;
import com.azimbabu.javapersistence.ch07.mappinginheritanceembeddable.model.Item;
import com.azimbabu.javapersistence.ch07.mappinginheritanceembeddable.model.Weight;
import com.azimbabu.javapersistence.ch07.mappinginheritanceembeddable.repository.ItemRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class MappingInheritanceSpringDataJPATest {

  @Autowired
  private ItemRepository itemRepository;

  @Test
  void storeLoadEntities() {
    // create entities
    Item item1 = new Item("Item 1",
        Dimensions.centimeters(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE),
        Weight.kilograms(BigDecimal.ONE));
    Item item2 = new Item("Item 2",
        Dimensions.inches(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN),
        Weight.pounds(BigDecimal.TEN));

    itemRepository.save(item1);
    itemRepository.save(item2);

    // load entities
    List<Item> items = itemRepository.findAll();

    assertEquals(2, items.size());
    for (Item item : items) {
      if (item.getId().equals(item1.getId())) {
        assertAll(() -> assertEquals("Item 1", item.getName()),
            () -> assertEquals("centimeters", item.getDimensions().getName()),
            () -> assertEquals("cm", item.getDimensions().getSymbol()),
            () -> assertEquals(0, BigDecimal.ONE.compareTo(item.getDimensions().getDepth())),
            () -> assertEquals(0, BigDecimal.ONE.compareTo(item.getDimensions().getWidth())),
            () -> assertEquals(0, BigDecimal.ONE.compareTo(item.getDimensions().getHeight())),
            () -> assertEquals("kilograms", item.getWeight().getName()),
            () -> assertEquals("kg", item.getWeight().getSymbol()),
            () -> assertEquals(0, BigDecimal.ONE.compareTo(item.getWeight().getValue())));
      } else {
        assertAll(() -> assertEquals("Item 2", item.getName()),
            () -> assertEquals("inches", item.getDimensions().getName()),
            () -> assertEquals("\"", item.getDimensions().getSymbol()),
            () -> assertEquals(0, BigDecimal.TEN.compareTo(item.getDimensions().getDepth())),
            () -> assertEquals(0, BigDecimal.TEN.compareTo(item.getDimensions().getWidth())),
            () -> assertEquals(0, BigDecimal.TEN.compareTo(item.getDimensions().getHeight())),
            () -> assertEquals("pounds", item.getWeight().getName()),
            () -> assertEquals("lb", item.getWeight().getSymbol()),
            () -> assertEquals(0, BigDecimal.TEN.compareTo(item.getWeight().getValue())));
      }
    }
  }
}
