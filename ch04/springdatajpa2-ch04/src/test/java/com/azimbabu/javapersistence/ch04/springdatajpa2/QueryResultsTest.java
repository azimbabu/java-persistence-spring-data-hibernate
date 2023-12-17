package com.azimbabu.javapersistence.ch04.springdatajpa2;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch04.springdatajpa2.model.User;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

public class QueryResultsTest extends SpringDataJpaApplicationTests {

  @Test
  void testStreamable() {
    try (Stream<User> userStream = userRepository.findByEmailContaining("someother")
        .and(userRepository.findByLevel(2))
        .stream()
        .distinct()) {
      assertEquals(6, userStream.count());
    }
  }

  @Test
  void testFindNumberOfUsersByActivity() {
    int numActive = userRepository.findNumberOfUsersByActivity(true);
    int numInactive = userRepository.findNumberOfUsersByActivity(false);
    assertAll(() -> assertEquals(8, numActive),
        () -> assertEquals(2, numInactive));
  }

  @Test
  void testFindByLevelAndActive() {
    List<User> level1ActiveUsers = userRepository.findByLevelAndActive(1, true);
    List<User> level1InactiveUsers = userRepository.findByLevelAndActive(1, false);
    List<User> level2ActiveUsers = userRepository.findByLevelAndActive(2, true);
    List<User> level2InactiveUsers = userRepository.findByLevelAndActive(2, false);
    assertAll(() -> assertEquals(2, level1ActiveUsers.size()),
        () -> assertEquals(0, level1InactiveUsers.size()),
        () -> assertEquals(2, level2ActiveUsers.size()),
        () -> assertEquals(1, level2InactiveUsers.size()));
  }

  @Test
  void testFindNumberOfUsersByActivityNative() {
    int numActive = userRepository.findNumberOfUsersByActivityNative(true);
    int numInactive = userRepository.findNumberOfUsersByActivityNative(false);
    assertAll(() -> assertEquals(8, numActive),
        () -> assertEquals(2, numInactive));
  }

  @Test
  void testFindByAsArrayAndSort() {
    List<Object[]> users1 = userRepository.findByAsArrayAndSort("ar",
        Sort.by("username"));
    List<Object[]> users2 = userRepository.findByAsArrayAndSort("ar",
        Sort.by("email_length").descending());
    List<Object[]> users3 = userRepository.findByAsArrayAndSort("ar",
        JpaSort.unsafe("LENGTH(u.email)"));

    assertAll(() -> assertEquals(2, users1.size()),
        () -> assertEquals("darren", users1.get(0)[0]),
        () -> assertEquals(21, users1.get(0)[1]),
        () -> assertEquals(2, users2.size()),
        () -> assertEquals("marion", users2.get(0)[0]),
        () -> assertEquals(26, users2.get(0)[1]),
        () -> assertEquals(2, users3.size()),
        () -> assertEquals("darren", users3.get(0)[0]),
        () -> assertEquals(21, users3.get(0)[1]));
  }
}
