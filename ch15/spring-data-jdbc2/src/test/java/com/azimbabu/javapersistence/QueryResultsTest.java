package com.azimbabu.javapersistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.model.User;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QueryResultsTest extends SpringDataJdbcApplicationTests {

  @Test
  void testStreamable() {
    try (Stream<User> result = userRepository.findByEmailContaining("someother")
        .and(userRepository.findByLevel(2))
        .stream()
        .distinct()) {
      assertEquals(6, result.count());
    }
  }

  @Test
  void testNumberOfUsersByActivity() {
    int numActiveUsers = userRepository.findNumberOfUsersByActivity(true);
    int numInactiveUsers = userRepository.findNumberOfUsersByActivity(false);
    assertAll(() -> assertEquals(8, numActiveUsers),
        () -> assertEquals(2, numInactiveUsers));
  }

  @Test
  void testUsersByLevelAndActivity() {
    List<User> users1 = userRepository.findByLevelAndActive(1, true);
    List<User> users2 = userRepository.findByLevelAndActive(2, true);
    List<User> users3 = userRepository.findByLevelAndActive(2, false);
    assertAll(() -> assertEquals(2, users1.size()),
        () -> assertEquals(2, users2.size()),
        () -> assertEquals(1, users3.size()));
  }
}
