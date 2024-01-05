package com.azimbabu.javapersistence.ch08.mappingcollections.mapofstringsorderby;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch08.mappingcollections.mapofstringsorderby.configuration.SpringDataConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
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
    List<Entry<String, String>> imagesFound = new ArrayList<>(itemFound.getImages().entrySet());
    List<Item> itemsFound = itemRepository.findAll();
    Set<String> images = itemRepository.findImagesNative(item.getId());

    assertAll(() -> assertEquals(4, itemFound.getImages().size()),
        () -> assertEquals(1, itemsFound.size()),
        () -> assertEquals(4, images.size()),
        () -> assertEquals("portrait.jpg", imagesFound.get(0).getKey()),
        () -> assertEquals("Portrait", imagesFound.get(0).getValue()),
        () -> assertEquals("background.jpg", imagesFound.get(imagesFound.size()-1).getKey()),
        () -> assertEquals("Background", imagesFound.get(imagesFound.size()-1).getValue()));
  }
}
