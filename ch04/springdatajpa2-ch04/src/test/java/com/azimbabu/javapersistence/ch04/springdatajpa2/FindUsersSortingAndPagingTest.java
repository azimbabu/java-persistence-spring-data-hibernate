package com.azimbabu.javapersistence.ch04.springdatajpa2;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch04.springdatajpa2.model.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class FindUsersSortingAndPagingTest extends SpringDataJpaApplicationTests {

  @Test
  void testFindFirstByOrderByUsernameAsc() {
    User user = userRepository.findFirstByOrderByUsernameAsc();
    assertEquals("beth", user.getUsername());
  }

  @Test
  void testFindTopByOrderByRegistrationDateDesc() {
    User user = userRepository.findTopByOrderByRegistrationDateDesc();
    assertEquals("julius", user.getUsername());
  }

  @Test
  void testFindAll() {
    Page<User> userPage = userRepository.findAll(PageRequest.of(1, 3));
    assertEquals(3, userPage.getSize());
  }

  @Test
  void testFindFirst2ByLevel() {
    List<User> users = userRepository.findFirst2ByLevel(2, Sort.by("registrationDate"));
    assertAll(() -> assertEquals(2, users.size()),
        () -> assertEquals("beth", users.get(0).getUsername()),
        () -> assertEquals("marion", users.get(1).getUsername()));
  }

  @Test
  void testFindByLevel() {
    Sort.TypedSort<User> userTypedSort = Sort.sort(User.class);
    List<User> users = userRepository.findByLevel(3,
        userTypedSort.by(User::getRegistrationDate).descending());
    assertAll(() -> assertEquals(2, users.size()),
        () -> assertEquals("james", users.get(0).getUsername()),
        () -> assertEquals("mike", users.get(1).getUsername()));
  }

  @Test
  void testFindByActive() {
    List<User> users = userRepository.findByActive(true,
        PageRequest.of(1, 4, Sort.by("registrationDate")));
    assertAll(() -> assertEquals(4, users.size()),
        () -> assertEquals("burk", users.get(0).getUsername()));
  }
}
