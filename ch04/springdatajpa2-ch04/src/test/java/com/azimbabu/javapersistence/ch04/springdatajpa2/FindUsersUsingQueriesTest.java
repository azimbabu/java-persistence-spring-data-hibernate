package com.azimbabu.javapersistence.ch04.springdatajpa2;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch04.springdatajpa2.model.User;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.Test;

public class FindUsersUsingQueriesTest extends SpringDataJpaApplicationTests {

  @Test
  void testFindAll() {
    List<User> users = userRepository.findAll();
    assertEquals(10, users.size());
  }

  @Test
  void testFindUser() {
    User beth = userRepository.findByUsername("beth");
    assertEquals("beth", beth.getUsername());
  }

  @Test
  void testFindAllByOrderByUsernameAsc() {
    List<User> users = userRepository.findAllByOrderByUsernameAsc();
    assertAll(() -> assertEquals(10, users.size()),
        () -> assertEquals("beth", users.get(0).getUsername()),
        () -> assertEquals("stephanie", users.get(users.size() - 1).getUsername()));
  }

  @Test
  void testFindByRegistrationDateBetween() {
    List<User> users = userRepository.findByRegistrationDateBetween(
        LocalDate.of(2020, Month.JULY, 1), LocalDate.of(2020, Month.DECEMBER, 31));
    assertEquals(4, users.size());
  }

  @Test
  void testFindByUsernameAndEmail() {
    List<User> users1 = userRepository.findByUsernameAndEmail("mike", "mike@somedomain.com");
    List<User> users2 = userRepository.findByUsernameAndEmail("mike", "beth@somedomain.com");
    assertEquals(1, users1.size());
    assertEquals(0, users2.size());
  }

  @Test
  void testFindByUsernameOrEmail() {
    List<User> users1 = userRepository.findByUsernameOrEmail("mike", "beth@somedomain.com");
    List<User> users2 = userRepository.findByUsernameOrEmail("beth", "mike@somedomain.com");
    List<User> users3 = userRepository.findByUsernameOrEmail("mike", "mike@somedomain.com");
    List<User> users4 = userRepository.findByUsernameOrEmail("beth", "beth@somedomain.com");
    assertEquals(2, users1.size());
    assertEquals(2, users2.size());
    assertEquals(1, users3.size());
    assertEquals(1, users4.size());
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
        () -> assertEquals("burk", users.get(users.size() - 1).getUsername()));
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
    assertEquals(8, activeUsers.size());
    assertEquals(2, inactiveUsers.size());
  }

  @Test
  void testFindByRegistrationDateIn() {
    List<User> users = userRepository.findByRegistrationDateIn(
        List.of(LocalDate.of(2020, Month.JANUARY, 18), LocalDate.of(2021, Month.JANUARY, 5)));
    assertEquals(3, users.size());
  }

  @Test
  void testFindByRegistrationDateNotIn() {
    List<User> users = userRepository.findByRegistrationDateNotIn(
        List.of(LocalDate.of(2020, Month.JANUARY, 18), LocalDate.of(2021, Month.JANUARY, 5)));
    assertEquals(7, users.size());
  }
}
