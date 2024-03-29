package com.azimbabu.javapersistence.ch09.manytomanyternary;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch09.manytomanyternary.model.CategorizedItem;
import com.azimbabu.javapersistence.ch09.manytomanyternary.model.Category;
import com.azimbabu.javapersistence.ch09.manytomanyternary.model.Item;
import com.azimbabu.javapersistence.ch09.manytomanyternary.model.User;
import com.azimbabu.javapersistence.ch09.manytomanyternary.repository.CategoryRepository;
import com.azimbabu.javapersistence.ch09.manytomanyternary.repository.ItemRepository;
import com.azimbabu.javapersistence.ch09.manytomanyternary.repository.UserRepository;
import java.util.List;
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

    CategorizedItem linkOne = new CategorizedItem(someUser, someItem);
    someCategory.addCategorizedItem(linkOne);

    CategorizedItem linkTwo = new CategorizedItem(someUser, otherItem);
    someCategory.addCategorizedItem(linkTwo);

    CategorizedItem linkThree = new CategorizedItem(someUser, someItem);
    otherCategory.addCategorizedItem(linkThree);

    Category someCategoryFound = categoryRepository.findById(someCategory.getId()).get();
    Category otherCategoryFound = categoryRepository.findById(otherCategory.getId()).get();

    Item someItemFound = itemRepository.findById(someItem.getId()).get();
    Item otherItemFound = itemRepository.findById(otherItem.getId()).get();

    List<Category> someItemFoundCategories = categoryRepository.findCategoryWithCategorizedItems(
        someItemFound);
    List<Category> otherItemFoundCategories = categoryRepository.findCategoryWithCategorizedItems(
        otherItemFound);

    User somUserFound = userRepository.findById(someUser.getId()).get();

    assertAll(() -> assertEquals(2, someCategoryFound.getCategorizedItems().size()),
        () -> assertEquals(1, otherCategoryFound.getCategorizedItems().size()),
        () -> assertEquals(someItemFound,
            otherCategoryFound.getCategorizedItems().iterator().next().getItem()),
        () -> assertEquals(somUserFound,
            otherCategoryFound.getCategorizedItems().iterator().next().getAddedBy()),
        () -> assertEquals(2, someItemFoundCategories.size()),
        () -> assertEquals(1, otherItemFoundCategories.size()));
  }
}
