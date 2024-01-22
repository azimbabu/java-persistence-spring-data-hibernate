package com.azimbabu.javapersistence.ch09.onetooneforeignkey;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch09.onetooneforeignkey.configuration.SpringDataConfiguration;
import com.azimbabu.javapersistence.ch09.onetooneforeignkey.model.Address;
import com.azimbabu.javapersistence.ch09.onetooneforeignkey.model.User;
import com.azimbabu.javapersistence.ch09.onetooneforeignkey.repository.AddressRepository;
import com.azimbabu.javapersistence.ch09.onetooneforeignkey.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class AdvancedMappingSpringDataJPATest {

  @Autowired
  private AddressRepository addressRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  void testStoreLoadEntities() {
    User john = new User("John Smith");
    Address address = new Address("Flowers Street", "01246", "Boston");
    john.setShippingAddress(address);

    userRepository.save(john);

    User userFound = userRepository.findUserWithAddress(john.getId());
    Address addressFound = addressRepository.findById(address.getId()).get();

    assertAll(() -> assertEquals(address.getStreet(), userFound.getShippingAddress().getStreet()),
        () -> assertEquals(address.getZipcode(), userFound.getShippingAddress().getZipcode()),
        () -> assertEquals(address.getCity(), userFound.getShippingAddress().getCity()),
        () -> assertEquals(address.getStreet(), addressFound.getStreet()),
        () -> assertEquals(address.getZipcode(), addressFound.getZipcode()),
        () -> assertEquals(address.getCity(), addressFound.getCity()));
  }
}
