package com.azimbabu.javapersistence.ch09.manytomanylinkentity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch09.manytomanylinkentity.model.CategorizedItem;
import com.azimbabu.javapersistence.ch09.manytomanylinkentity.model.Category;
import com.azimbabu.javapersistence.ch09.manytomanylinkentity.model.Item;
import com.azimbabu.javapersistence.ch09.manytomanylinkentity.repository.CategorizedItemRepository;
import com.azimbabu.javapersistence.ch09.manytomanylinkentity.repository.CategoryRepository;
import com.azimbabu.javapersistence.ch09.manytomanylinkentity.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private CategorizedItemRepository categorizedItemRepository;

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

    CategorizedItem linkOne = new CategorizedItem("John Smith", someCategory, someItem);
    CategorizedItem linkTwo = new CategorizedItem("John Smith", someCategory, otherItem);
    CategorizedItem linkThree = new CategorizedItem("John Smith", otherCategory, someItem);

    categorizedItemRepository.save(linkOne);
    categorizedItemRepository.save(linkTwo);
    categorizedItemRepository.save(linkThree);

    Category someCategoryFound = categoryRepository.findById(someCategory.getId()).get();
    Category otherCategoryFound = categoryRepository.findById(otherCategory.getId()).get();

    Item someItemFound = itemRepository.findById(someItem.getId()).get();
    Item otherItemFound = itemRepository.findById(otherItem.getId()).get();

    assertAll(() -> assertEquals(2, someCategoryFound.getCategorizedItems().size()),
        () -> assertEquals(2, someItemFound.getCategorizedItems().size()),
        () -> assertEquals(1, otherCategoryFound.getCategorizedItems().size()),
        () -> assertEquals(1, otherItemFound.getCategorizedItems().size()),
        () -> assertEquals(someItemFound,
            otherCategoryFound.getCategorizedItems().iterator().next().getItem()),
        () -> assertEquals(someCategoryFound,
            otherItemFound.getCategorizedItems().iterator().next().getCategory()));
  }
}
