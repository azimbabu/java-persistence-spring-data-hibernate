package com.azimbabu.javapersistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.model.User;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class FindUsersUsingQueriesTest extends SpringDataJdbcApplicationTests {

  @Test
  void testFindAll() {
    List<User> users = userRepository.findAll();
    assertEquals(10, users.size());
  }

  @Test
  void testFindUser() {
    User beth = userRepository.findByUsername("beth").get();
    assertEquals("beth", beth.getUsername());
  }

  @Test
  void testFindAllByOrderByUsernameAsc() {
    List<User> users = userRepository.findAllByOrderByUsernameAsc();
    assertAll(() -> assertEquals(10, users.size()),
        () -> assertEquals("beth", users.get(0).getUsername()),
        () -> assertEquals("stephanie", users.get(users.size()-1).getUsername()));
  }

  @Test
  void testFindByRegistrationDateBetween() {
    List<User> users = userRepository.findByRegistrationDateBetween(
        LocalDate.of(2020, Month.JULY, 1),
        LocalDate.of(2020, Month.DECEMBER, 31));
    assertEquals(4, users.size());
  }

  @Test
  void testFindByUsernameAndEmail() {
    List<User> users1 = userRepository.findByUsernameAndEmail("mike", "mike@somedomain.com");
    assertEquals(1, users1.size());

    List<User> users2 = userRepository.findByUsernameAndEmail("mike", "beth@somedomain.com");
    assertEquals(0, users2.size());
  }

  @Test
  void testFindByUsernameOrEmail() {
    List<User> users1 = userRepository.findByUsernameOrEmail("mike", "beth@somedomain.com");
    assertEquals(2, users1.size());

    List<User> users2 = userRepository.findByUsernameOrEmail("beth", "beth@somedomain.com");
    assertEquals(1, users2.size());
  }

  @Test
  void testFindByUsernameIgnoreCase() {
    List<User> users = userRepository.findByUsernameIgnoreCase("MIKE");
    assertAll(() -> assertEquals(1, users.size()),
        () -> assertEquals("mike", users.get(0).getUsername()));
  }

  @Test
  void testFindByLevelOrderByUsernameDesc() {
    List<User> users = userRepository.findByLevelOrderByUsernameDesc(1);
    assertAll(() -> assertEquals(2, users.size()),
        () -> assertEquals("john", users.get(0).getUsername()),
        () -> assertEquals("burk", users.get(1).getUsername()));
  }

  @Test
  void testFindByLevelGreaterThanEqual() {
    List<User> users = userRepository.findByLevelGreaterThanEqual(3);
    assertEquals(5, users.size());
  }

  @Test
  void testFindByUsernameContaining() {
    List<User> users = userRepository.findByUsernameContaining("ar");
    assertEquals(2, users.size());
  }

  @Test
  void testFindByUsernameLike() {
    List<User> users = userRepository.findByUsernameLike("%ar%");
    assertEquals(2, users.size());
  }

  @Test
  void testFindByUsernameStartingWith() {
    List<User> users = userRepository.findByUsernameStartingWith("b");
    assertEquals(2, users.size());
  }

  @Test
  void testFindByUsernameEndingWith() {
    List<User> users = userRepository.findByUsernameEndingWith("ie");
    assertEquals(2, users.size());
  }
  
  @Test
  void testFindByActive() {
    List<User> activeUsers = userRepository.findByActive(true);
    List<User> inactiveUsers = userRepository.findByActive(false);
    assertAll(() -> assertEquals(8, activeUsers.size()),
        () -> assertEquals(2, inactiveUsers.size()));
  }

  @Test
  void testFindByRegistrationDateIn() {
    List<LocalDate> dates = Arrays.asList(LocalDate.of(2020, Month.JANUARY, 18),
        LocalDate.of(2021, Month.JANUARY, 5));
    List<User> users = userRepository.findByRegistrationDateIn(dates);
    assertEquals(3, users.size());
  }

  @Test
  void testFindByRegistrationDateNotIn() {
    List<LocalDate> dates = Arrays.asList(LocalDate.of(2020, Month.JANUARY, 18),
        LocalDate.of(2021, Month.JANUARY, 5));
    List<User> users = userRepository.findByRegistrationDateNotIn(dates);
    assertEquals(7, users.size());
  }
}
