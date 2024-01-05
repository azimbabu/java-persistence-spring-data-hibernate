package com.azimbabu.javapersistence.ch08.mappingcollections.bagofstringsorderby;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch08.mappingcollections.bagofstringsorderby.configuration.SpringDataConfiguration;
import java.util.ArrayList;
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
    List<String> imagesFound = new ArrayList<>(itemFound.getImages());
    List<Item> itemsFound = itemRepository.findAll();
    Collection<String> images = itemRepository.findImagesNative(item.getId());

    assertAll(() -> assertEquals(5, itemFound.getImages().size()),
        () -> assertEquals(1, itemsFound.size()),
        () -> assertEquals(5, images.size()),
        () -> assertEquals("portrait.jpg", imagesFound.get(0)),
        () -> assertEquals("background.jpg", imagesFound.get(imagesFound.size()-1)));
  }
}
