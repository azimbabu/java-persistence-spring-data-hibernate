package com.azimbabu.javapersistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.model.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

public class ModifyQueryTest extends SpringDataJdbcApplicationTests {
  @Test
  void testUpdateLevel() {
    int updated = userRepository.updateLevel(5, 4);
    List<User> users = userRepository.findByLevel(4, Sort.by("username"));
    assertAll(() -> assertEquals(1, updated),
        () -> assertEquals(3, users.size()),
        () -> assertEquals("katie", users.get(1).getUsername()));
  }
}
