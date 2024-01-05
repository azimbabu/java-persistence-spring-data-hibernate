package com.azimbabu.javapersistence.ch08.mappingcollections.mapofstrings;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch08.mappingcollections.mapofstrings.configuration.SpringDataConfiguration;
import java.util.Collection;
import java.util.List;
import java.util.Set;
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
    item.addImage("background.jpg", "Background");
    item.addImage("foreground.jpg", "Foreground");
    item.addImage("landscape.jpg", "Landscape");
    item.addImage("portrait.jpg", "Portrait");

    itemRepository.save(item);

    Item itemFound = itemRepository.findItemWithImages(item.getId());
    List<Item> itemsFound = itemRepository.findAll();
    Set<String> images = itemRepository.findImagesNative(item.getId());

    assertAll(() -> assertEquals(4, itemFound.getImages().size()),
        () -> assertEquals(1, itemsFound.size()),
        () -> assertEquals(4, images.size()));
  }
}
