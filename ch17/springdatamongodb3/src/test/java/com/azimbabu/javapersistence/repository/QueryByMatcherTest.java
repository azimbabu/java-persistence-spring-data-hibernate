package com.azimbabu.javapersistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.azimbabu.javapersistence.model.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public class QueryByMatcherTest extends SpringDataMongoDBApplicationTests {

  @Test
  void findByMatcher() {
    ExampleMatcher exampleMatcher = ExampleMatcher.matching()
        .withIgnorePaths("level")
        .withIgnorePaths("active");

    User probe = new User(null, null, "Smith");

    List<User> users = userRepository.findAll(Example.of(probe, exampleMatcher));
    assertThat(users).hasSize(2)
        .extracting(User::getUsername)
        .contains("john", "burk");
  }
}
