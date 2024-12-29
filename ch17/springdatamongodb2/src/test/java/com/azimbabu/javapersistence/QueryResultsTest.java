package com.azimbabu.javapersistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.azimbabu.javapersistence.model.User;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class QueryResultsTest extends SpringDataMongoDBApplicationTests {

  @Test
  void testStreamable() {
    try (Stream<User> result = userRepository.findByEmailContaining("someother")
        .and(userRepository.findByLevel(2))
        .stream()
        .distinct()) {
      assertEquals(7, result.count());
    }
  }

  @Test
  void testQueries() {
    int numSmiths = userRepository.findUsersByLastName("Smith").size();
    int numInactives = userRepository.findUsersByActive(false).size();
    int numActives = userRepository.findUsersByActive(true).size();

    assertAll(() -> assertEquals(2, numSmiths),
        () -> assertEquals(2, numInactives),
        () -> assertEquals(8, numActives));
  }

  @Test
  void testRegExpQueries() {
    List<User> users = userRepository.findUsersByRegexpLastName("^S");
    assertEquals(4, users.size());
  }

  @Test
  void testLevelBetween() {
    List<User> users = userRepository.findUsersByLevelBetween(3, 4);
    assertEquals(4, users.size());
  }

  @Test
  void testFindUsernameAndId() {
    List<User> users = userRepository.findUsernameAndId();
    assertAll(() -> assertEquals(10, users.size()),
        () -> users.forEach(user -> {
          assertNotNull(user.getId());
          assertNotNull(user.getUsername());
          assertNull(user.getFirstName());
          assertNull(user.getLastName());
          assertNull(user.getRegistrationDate());
          assertNull(user.getEmail());
          assertEquals(0, user.getLevel());
          assertFalse(user.isActive());
        }));
  }

  @Test
  void testFindUsersExcludeId() {
    List<User> users = userRepository.findUsersExcludeId();
    assertAll(() -> assertEquals(10, users.size()),
        () -> users.forEach(user -> {
          assertNull(user.getId());
          assertNotNull(user.getUsername());
          assertNotNull(user.getFirstName());
          assertNotNull(user.getLastName());
          assertNotNull(user.getRegistrationDate());
          assertNotNull(user.getEmail());
          assertNotEquals(0, user.getLevel());
        }));
  }

  @Test
  void testFindUsersByRegexpLastNameExcludeId() {
    List<User> users = userRepository.findUsersByRegexpLastNameExcludeId(
        "^S");
    assertAll(() -> assertEquals(4, users.size()),
        () -> users.forEach(user -> {
          assertNull(user.getId());
          assertNotNull(user.getUsername());
          assertNotNull(user.getFirstName());
          assertTrue(user.getLastName().startsWith("S"));
          assertNotNull(user.getRegistrationDate());
          assertNotNull(user.getEmail());
          assertNotEquals(0, user.getLevel());
        }));
  }
}
