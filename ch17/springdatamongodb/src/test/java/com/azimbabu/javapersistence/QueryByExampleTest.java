package com.azimbabu.javapersistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.azimbabu.javapersistence.model.User;
import com.azimbabu.javapersistence.repository.UserRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class QueryByExampleTest {

  @Autowired
  UserRepository userRepository;

  @BeforeAll
  void beforeAll() {
    userRepository.save(new User("john", "John", "Smith"));
    userRepository.save(new User("stephanie", "Stephanie", "Christensen"));
    userRepository.save(new User("burk", "Burk", "Smith"));
  }

  @AfterAll
  void afterAll() {
    userRepository.deleteAll();
  }

  @Test
  void findByExample() {
    User probe = new User(null, null, "Smith");
    Example<User> userExample = Example.of(probe);
    List<User> users = userRepository.findAll(userExample);
    assertThat(users).hasSize(2)
        .extracting(User::getUsername)
        .contains("john", "burk");
  }
}
