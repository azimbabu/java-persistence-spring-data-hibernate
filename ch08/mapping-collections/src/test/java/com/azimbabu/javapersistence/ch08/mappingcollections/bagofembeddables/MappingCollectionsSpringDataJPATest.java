package com.azimbabu.javapersistence.ch08.mappingcollections.bagofembeddables;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch08.mappingcollections.bagofembeddables.configuration.SpringDataConfiguration;
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

    item.addImage(new Image("Background", "background.jpg", 640, 480));
    item.addImage(new Image("Foreground","foreground.jpg", 640, 480));
    item.addImage(new Image("Landscape","landscape.jpg", 640, 480));
    item.addImage(new Image("Portrait", "portrait.jpg", 640, 480));

    itemRepository.save(item);

    Item itemFound = itemRepository.findItemWithImages(item.getId());
    List<Item> items = itemRepository.findAll();
    Collection<String> images = itemRepository.findImagesNative(item.getId());

    assertAll(() -> assertEquals(4, itemFound.getImages().size()),
        () -> assertEquals(1, items.size()),
        () -> assertEquals(4, images.size()));
  }
}
