package com.azimbabu.javapersistence.springtesting;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.springtesting.model.User;
import com.azimbabu.javapersistence.springtesting.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SaveRetrieveUserTest {

  @Autowired
  private UserRepository userRepository;

//  @Test
  @RepeatedTest(2)
  void saveRetrieve() {
    userRepository.save(new User("User1"));
    List<User> users = userRepository.findAll();

    assertAll(() -> assertEquals(1, users.size()),
        () -> assertEquals("User1", users.get(0).getName()));
  }
}
