package com.azimbabu.javapersistence.ch09.manytomanybidirectional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch09.manytomanybidirectional.model.Category;
import com.azimbabu.javapersistence.ch09.manytomanybidirectional.model.Item;
import com.azimbabu.javapersistence.ch09.manytomanybidirectional.repository.CategoryRepository;
import com.azimbabu.javapersistence.ch09.manytomanybidirectional.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Transactional
  public void storeLoadEntities() {
    Category someCategory = new Category("Some Category");
    Category otherCategory = new Category("Other Category");

    Item someItem = new Item("Some Item");
    Item otherItem = new Item("Other Item");

    someCategory.addItem(someItem);
    someItem.addCategory(someCategory);

    someCategory.addItem(otherItem);
    otherItem.addCategory(someCategory);

    otherCategory.addItem(someItem);
    someItem.addCategory(otherCategory);

    categoryRepository.save(someCategory);
    categoryRepository.save(otherCategory);

    Category someCategoryFound = categoryRepository.findById(someCategory.getId()).get();
    Category otherCategoryFound = categoryRepository.findById(otherCategory.getId()).get();

    Item someItemFound = itemRepository.findById(someItem.getId()).get();
    Item otherItemFound = itemRepository.findById(otherItem.getId()).get();

    assertAll(() -> assertEquals(2, someCategoryFound.getItems().size()),
        () -> assertEquals(2, someItemFound.getCategories().size()),
        () -> assertEquals(1, otherCategoryFound.getItems().size()),
        () -> assertEquals(1, otherItemFound.getCategories().size()),
        () -> assertEquals(someItemFound, otherCategoryFound.getItems().iterator().next()),
        () -> assertEquals(someCategoryFound, otherItemFound.getCategories().iterator().next()));
  }
}
