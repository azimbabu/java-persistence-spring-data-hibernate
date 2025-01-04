package com.azimbabu.javapersistence.springtesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.springtesting.model.User;
import com.azimbabu.javapersistence.springtesting.repository.UserRepository;
import com.azimbabu.javapersistence.springtesting.service.UserService;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
@Transactional
public class TransactionalTest {

  @Value("${numUsers:10}")
  private int numUsers;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @BeforeAll
  static void beforeAll() {
    System.out.println("beforeAll, transaction active = " + TransactionSynchronizationManager.isActualTransactionActive());
  }

  @BeforeEach
  void beforeEach() {
    System.out.println("beforeEach, transaction active = " + TransactionSynchronizationManager.isActualTransactionActive());
  }

  @RepeatedTest(2)
  void storeRetrieve() {
    List<User> users = UserHelper.buildUsers(numUsers);
    userRepository.saveAll(users);
    assertEquals(numUsers, users.size());

    userService.saveTransactionally(users.get(0));

    System.out.println("end of method, transaction active = " + TransactionSynchronizationManager.isActualTransactionActive());
  }

  @AfterEach
  void afterEach() {
    System.out.println("afterEach, transaction active = " + TransactionSynchronizationManager.isActualTransactionActive());
  }

  @AfterAll
  static void afterAll() {
    System.out.println("afterAll, transaction active = " + TransactionSynchronizationManager.isActualTransactionActive());
  }
}
