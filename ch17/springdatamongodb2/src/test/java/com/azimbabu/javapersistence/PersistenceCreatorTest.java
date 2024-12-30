package com.azimbabu.javapersistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.azimbabu.javapersistence.model.User;
import java.util.List;
import org.junit.jupiter.api.Test;

class PersistenceCreatorTest extends SpringDataMongoDBApplicationTests {

  @Test
  void testPersistenceCreator() {
    List<User> users = userRepository.findAll();
    assertAll(() -> assertEquals(10, users.size()),
        () -> users.forEach(user -> {
          assertNotNull(user.getId());
          assertNotNull(user.getUsername());
          assertNotNull(user.getFirstName());
          assertNotNull(user.getLastName());
          assertEquals("192.168.1.100", user.getIp());
        }));
  }
}
