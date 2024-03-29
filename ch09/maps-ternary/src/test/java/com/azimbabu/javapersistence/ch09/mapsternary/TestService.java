package com.azimbabu.javapersistence.ch09.mapsternary;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch09.mapsternary.model.Category;
import com.azimbabu.javapersistence.ch09.mapsternary.model.Item;
import com.azimbabu.javapersistence.ch09.mapsternary.model.User;
import com.azimbabu.javapersistence.ch09.mapsternary.repository.CategoryRepository;
import com.azimbabu.javapersistence.ch09.mapsternary.repository.ItemRepository;
import com.azimbabu.javapersistence.ch09.mapsternary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private UserRepository userRepository;

  @Transactional
  public void storeLoadEntities() {
    Category someCategory = new Category("Some Category");
    Category otherCategory = new Category("Other Category");

    categoryRepository.save(someCategory);
    categoryRepository.save(otherCategory);

    Item someItem = new Item("Some Item");
    Item otherItem = new Item("Other Item");

    itemRepository.save(someItem);
    itemRepository.save(otherItem);

    User someUser = new User("John Smith");
    userRepository.save(someUser);

    someCategory.addItem(someItem, someUser);
    someCategory.addItem(otherItem, someUser);
    otherCategory.addItem(someItem, someUser);

    Category someCategoryFound = categoryRepository.findById(someCategory.getId()).get();
    Category otherCategoryFound = categoryRepository.findById(otherCategory.getId()).get();

    Item someItemFound = itemRepository.findById(someItem.getId()).get();

    User someUserFound = userRepository.findById(someUser.getId()).get();

    assertAll(() -> assertEquals(2, someCategoryFound.getItemAddedBy().size()),
        () -> assertEquals(1, otherCategoryFound.getItemAddedBy().size()),
        () -> assertEquals(someItemFound,
            otherCategoryFound.getItemAddedBy().keySet().iterator().next()),
        () -> assertEquals(someUserFound,
            otherCategoryFound.getItemAddedBy().values().iterator().next()));
  }
}
