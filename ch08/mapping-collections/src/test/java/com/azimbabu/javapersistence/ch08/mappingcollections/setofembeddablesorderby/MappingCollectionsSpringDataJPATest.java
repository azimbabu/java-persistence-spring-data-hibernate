package com.azimbabu.javapersistence.ch08.mappingcollections.setofembeddablesorderby;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch08.mappingcollections.setofembeddablesorderby.configuration.SpringDataConfiguration;
import java.util.ArrayList;
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

    item.addImage(new Image("background.jpg", 640, 480));
    item.addImage(new Image("foreground.jpg", 640, 480));
    item.addImage(new Image("landscape.jpg", 640, 480));
    item.addImage(new Image("portrait.jpg", 640, 480));

    itemRepository.save(item);

    Item itemFound = itemRepository.findItemWithImages(item.getId());
    List<Image> imagesFound = new ArrayList<>(itemFound.getImages());
    List<Item> items = itemRepository.findAll();
    Set<String> images = itemRepository.findImagesNative(item.getId());

    assertAll(() -> assertEquals(4, itemFound.getImages().size()),
        () -> assertEquals(1, items.size()),
        () -> assertEquals(4, images.size()),
        () -> assertEquals("portrait.jpg", imagesFound.get(0).getFilename()),
        () -> assertEquals("background.jpg", imagesFound.get(imagesFound.size()-1).getFilename()));
  }
}
