package com.azimbabu.javapersistence.springtesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.springtesting.model.User;
import com.azimbabu.javapersistence.springtesting.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("prod")
public class SpringProfilesTest {

  @Value("${numUsers:10}")
  private int numUsers;

  @Autowired
  private UserRepository userRepository;

  @Test
  void storeUpdateRetrieve() {
    List<User> users = UserHelper.buildUsers(numUsers);
    userRepository.saveAll(users);
    assertEquals(numUsers, users.size());
  }
}
