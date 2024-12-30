package com.azimbabu.javapersistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.azimbabu.javapersistence.model.User;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class AddressTest extends SpringDataMongoDBApplicationTests {

  @Test
  void testAddress() {
    List<User> users = userRepository.findAll();
    assertAll(() -> assertEquals(10, users.size()),
        () -> users.forEach(user -> {
          assertNotNull(user.getId());
          assertNotNull(user.getUsername());
          assertNotNull(user.getFirstName());
          assertNotNull(user.getLastName());
          assertEquals("192.168.1.100", user.getIp());
          assertNull(user.getPassword());
        }));

    List<User> userWithAddresses = users.stream().filter(user -> user.getAddress() != null)
        .collect(Collectors.toList());
    assertAll(() -> assertEquals(4, userWithAddresses.size()),
        () -> userWithAddresses.forEach(user -> {
          assertNotNull(user.getAddress());
        }));
  }
}
