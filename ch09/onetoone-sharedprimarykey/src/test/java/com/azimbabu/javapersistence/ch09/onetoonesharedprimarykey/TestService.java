package com.azimbabu.javapersistence.ch09.onetoonesharedprimarykey;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch09.onetoonesharedprimarykey.model.Address;
import com.azimbabu.javapersistence.ch09.onetoonesharedprimarykey.model.User;
import com.azimbabu.javapersistence.ch09.onetoonesharedprimarykey.repository.AddressRepository;
import com.azimbabu.javapersistence.ch09.onetoonesharedprimarykey.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AddressRepository addressRepository;

  @Transactional
  public void storeLoadEntities() {
    Address address = new Address("Flowers Street", "01246", "Boston");
    addressRepository.save(address);

    User john = new User(address.getId(), "John Smith");
    john.setShippingAddress(address);
    userRepository.save(john);

    User userFound = userRepository.findById(john.getId()).get();
    Address addressFound = addressRepository.findById(address.getId()).get();

    assertAll(() -> assertEquals(address.getStreet(), userFound.getShippingAddress().getStreet()),
        () -> assertEquals(address.getZipcode(), userFound.getShippingAddress().getZipcode()),
        () -> assertEquals(address.getCity(), userFound.getShippingAddress().getCity()),
        () -> assertEquals(address.getStreet(), addressFound.getStreet()),
        () -> assertEquals(address.getZipcode(), addressFound.getZipcode()),
        () -> assertEquals(address.getCity(), addressFound.getCity()));
  }
}
