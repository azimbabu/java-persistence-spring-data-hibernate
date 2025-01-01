package com.azimbabu.javapersistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.hibernateogm.model.User;
import java.util.List;
import org.junit.jupiter.api.Test;

public class FindUsersUsingQueriesTest extends SpringDataJdbcApplicationTests {
  @Test
  void testFindAll() {
    List<User> users = userRepository.findAll();
    assertEquals(10, users.size());
  }
}
