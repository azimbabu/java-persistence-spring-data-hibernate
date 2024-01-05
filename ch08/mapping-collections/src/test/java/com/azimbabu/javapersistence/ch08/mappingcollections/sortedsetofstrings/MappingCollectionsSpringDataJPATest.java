package com.azimbabu.javapersistence.ch08.mappingcollections.sortedsetofstrings;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch08.mappingcollections.sortedsetofstrings.configuration.SpringDataConfiguration;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class MappingCollectionsSpringDataJPATest {
  @Autowired
  private ItemRepository itemRepository;
  @Test
  void storeLoadEntities() {
    Item item = new Item("Foo");

    item.addImage("background.jpg");
    item.addImage("foreground.jpg");
    item.addImage("landscape.jpg");
    item.addImage("portrait.jpg");

    itemRepository.save(item);

    Item itemFound = itemRepository.findItemWithImages(item.getId());
    List<Item> itemsFound = itemRepository.findAll();
    SortedSet<String> images = itemRepository.findImagesNative(item.getId());

    assertAll(() -> assertEquals(4, itemFound.getImages().size()),
        () -> assertEquals(1, itemsFound.size()),
        () -> assertEquals(4, images.size()),
        () -> assertEquals("background.jpg", itemFound.getImages().first()),
        () -> assertEquals("portrait.jpg", itemFound.getImages().last()));
  }
}
