package com.azimbabu.javapersistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.model.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.TypedSort;

public class FindUsersSortingAndPagingTest extends SpringDataJdbcApplicationTests {

  @Test
  void testFindFirstByOrderByUsernameAsc() {
    User user = userRepository.findFirstByOrderByUsernameAsc().get();
    assertEquals("beth", user.getUsername());
  }

  @Test
  void testFindTopByOrderByRegistrationDateAsc() {
    User user = userRepository.findTopByOrderByRegistrationDateDesc().get();
    assertEquals("julius", user.getUsername());
  }
  
  @Test
  void testFindAll() {
    Page<User> userPage = userRepository.findAll(PageRequest.of(1, 3));
    assertAll(() -> assertEquals(3, userPage.getSize()),
        () -> assertEquals(4, userPage.getTotalPages()),
        () -> assertEquals(10, userPage.getTotalElements()));
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
    TypedSort<User> userSort = Sort.sort(User.class);
    List<User> users = userRepository.findByLevel(3,
        userSort.by(User::getRegistrationDate).descending());
    assertAll(() -> assertEquals(2, users.size()),
        () -> assertEquals("james", users.get(0).getUsername()),
        () -> assertEquals("mike", users.get(1).getUsername()));
  }

  @Test
  void findByActive() {
    List<User> users = userRepository.findByActive(true,
        PageRequest.of(1, 4, Sort.by("registrationDate")));
    assertAll(() -> assertEquals(4, users.size()),
        () -> assertEquals("burk", users.get(0).getUsername()));
  }
}
