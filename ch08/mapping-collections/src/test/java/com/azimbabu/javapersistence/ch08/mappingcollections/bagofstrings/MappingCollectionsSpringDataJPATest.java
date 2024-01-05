package com.azimbabu.javapersistence.ch08.mappingcollections.bagofstrings;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch08.mappingcollections.bagofstrings.configuration.SpringDataConfiguration;
import java.util.Collection;
import java.util.List;
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
    item.addImage("foreground.jpg");

    itemRepository.save(item);

    Item itemFound = itemRepository.findItemWithImages(item.getId());
    List<Item> itemsFound = itemRepository.findAll();
    Collection<String> images = itemRepository.findImagesNative(item.getId());

    assertAll(() -> assertEquals(5, itemFound.getImages().size()),
        () -> assertEquals(1, itemsFound.size()),
        () -> assertEquals(5, images.size()));
  }
}
