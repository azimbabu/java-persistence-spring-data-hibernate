package com.azimbabu.javapersistence.ch08.mappingcollections.embeddablesetofstrings;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch08.mappingcollections.embeddablesetofstrings.configuration.SpringDataConfiguration;
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
  private UserRepository userRepository;

  @Test
  void storeLoadEntities() {
    User john = new User("John");
    Address address = new Address("Flowers Street", "01246", "Boston");
    address.addContact("John Smith");
    address.addContact("Jane Smith");
    john.setAddress(address);

    userRepository.save(john);

    List<User> users = userRepository.findAll();

    assertAll(() -> assertEquals(1, users.size()),
        () -> assertEquals(address.getCity(), users.get(0).getAddress().getCity()),
        () -> assertEquals(address.getStreet(), users.get(0).getAddress().getStreet()),
        () -> assertEquals(address.getZipcode(), users.get(0).getAddress().getZipcode()),
        () -> assertEquals(address.getContacts(), users.get(0).getAddress().getContacts()));
  }
}
