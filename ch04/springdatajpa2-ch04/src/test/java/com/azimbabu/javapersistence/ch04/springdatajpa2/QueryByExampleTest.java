package com.azimbabu.javapersistence.ch04.springdatajpa2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch04.springdatajpa2.model.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;

public class QueryByExampleTest extends SpringDataJpaApplicationTests {

  @Test
  void testEmailWithQueryByExample() {
    User user = new User();
    user.setEmail("@someotherdomain.com");

    ExampleMatcher exampleMatcher = ExampleMatcher.matching()
        .withIgnorePaths("level", "active")
        .withMatcher("email", matcher -> matcher.endsWith());

    Example<User> example = Example.of(user, exampleMatcher);
    List<User> users = userRepository.findAll(example);
    assertEquals(4, users.size());
  }

  @Test
  void testUsernameWithQueryByExample() {
    User user = new User();
    user.setUsername("J");

    ExampleMatcher exampleMatcher = ExampleMatcher.matching()
        .withIgnorePaths("level", "active")
        .withStringMatcher(StringMatcher.STARTING)
        .withIgnoreCase();

    Example<User> example = Example.of(user, exampleMatcher);
    List<User> users = userRepository.findAll(example);
    assertEquals(3, users.size());
  }

}
