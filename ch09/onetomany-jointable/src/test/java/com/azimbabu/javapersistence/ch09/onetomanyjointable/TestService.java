package com.azimbabu.javapersistence.ch09.onetomanyjointable;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.azimbabu.javapersistence.ch09.onetomanyjointable.model.Item;
import com.azimbabu.javapersistence.ch09.onetomanyjointable.model.User;
import com.azimbabu.javapersistence.ch09.onetomanyjointable.repository.ItemRepository;
import com.azimbabu.javapersistence.ch09.onetomanyjointable.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Transactional
  public void storeLoadEntities() {
    Item someItem = new Item("Some Item");
    itemRepository.save(someItem);

    Item otherItem = new Item("Other Item");
    itemRepository.save(otherItem);

    User someUser = new User("John Smith");
    someUser.buyItem(someItem);   // Link
    someItem.setBuyer(someUser);  // Link
    someUser.buyItem(otherItem);  // Link
    otherItem.setBuyer(someUser); // Link
    userRepository.save(someUser);

    Item unsoldItem = new Item("Unsold Item");
    itemRepository.save(unsoldItem);

    Item someItemFound = itemRepository.findById(someItem.getId()).get();
    Item unsoldItemFound = itemRepository.findById(unsoldItem.getId()).get();

    assertAll(() -> assertEquals("John Smith", someItemFound.getBuyer().getUsername()),
        () -> assertTrue(someItemFound.getBuyer().getBoughtItems().contains(someItemFound)),
        () -> assertNull(unsoldItemFound.getBuyer()));
  }
}
