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

class FindUsersSortingAndPagingTest extends SpringDataMongoDBApplicationTests {

  @Test
  void testOrder() {
    User user1 = userRepository.findFirstByOrderByUsernameAsc().get();
    User user2 = userRepository.findTopByOrderByRegistrationDateDesc().get();
    Page<User> userPage = userRepository.findAll(PageRequest.of(1, 3));
    List<User> users = userRepository.findFirst2ByLevel(2, Sort.by("registrationDate"));

    assertAll(() -> assertEquals("beth", user1.getUsername()),
        () -> assertEquals("julius", user2.getUsername()),
        () -> assertEquals(2, users.size()),
        () -> assertEquals("beth", users.get(0).getUsername()),
        () -> assertEquals("marion", users.get(1).getUsername()),
        () -> assertEquals(3, userPage.getSize()));
  }

  @Test
  void testFindByLevel() {
    TypedSort<User> userTypedSort = Sort.sort(User.class);
    List<User> users = userRepository.findByLevel(3, userTypedSort.by(User::getRegistrationDate).descending());
    assertAll(() -> assertEquals(2, users.size()),
        () -> assertEquals("james", users.get(0).getUsername()));
  }

  @Test
  void testFindByActive() {
    List<User> users = userRepository.findByActive(true,
        PageRequest.of(1, 4, Sort.by("registrationDate")));
    assertAll(() -> assertEquals(4, users.size()),
        () -> assertEquals("burk", users.get(0).getUsername()));
  }
}
