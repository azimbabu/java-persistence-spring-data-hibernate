package com.azimbabu.javapersistence.ch04.springdatajpa2;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch04.springdatajpa2.model.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

public class DeleteQueryTest extends SpringDataJpaApplicationTests {

  @Test
  void testDeleteByLevel() {
    int deleted = userRepository.deleteByLevel(2);
    List<User> users = userRepository.findByLevel(2, Sort.by("username"));
    assertAll(() -> assertEquals(3, deleted),
        () -> assertEquals(0, users.size()));
  }

  @Test
  void testDeleteBulkByLevel() {
    int deleted = userRepository.deleteBulkByLevel(3);
    List<User> users = userRepository.findByLevel(3, Sort.by("username"));
    assertAll(() -> assertEquals(2, deleted),
        () -> assertEquals(0, users.size()));
  }
}
