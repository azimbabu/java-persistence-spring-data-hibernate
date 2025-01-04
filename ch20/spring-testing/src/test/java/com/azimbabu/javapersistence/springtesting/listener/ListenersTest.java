package com.azimbabu.javapersistence.springtesting.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.springtesting.UserHelper;
import com.azimbabu.javapersistence.springtesting.model.User;
import com.azimbabu.javapersistence.springtesting.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestExecutionListeners(value = {DatabaseOperationsListener.class}, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public class ListenersTest {

  @Value("${numUsers:10}")
  private int numUsers;

  @Autowired
  private UserRepository userRepository;

  @BeforeAll
  static void beforeAll() {
    System.out.println("@BeforeAll");
  }

  @BeforeEach
  void beforeEach() {
    System.out.println("@BeforeEach");
  }

  @Test
  void storeUpdateRetrieve() {
    TestContextManager testContextManager = new TestContextManager(getClass());
    System.out.println("testContextManager.getTestExecutionListeners().size() = " + testContextManager.getTestExecutionListeners().size());
    List<User> users = UserHelper.buildUsers(numUsers);
    userRepository.saveAll(users);
    assertEquals(numUsers, users.size());
  }

  @AfterEach
  void afterEach() {
    System.out.println("@AfterEach");
  }

  @AfterAll
  static void afterAll() {
    System.out.println("@AfterAll");
  }
}
