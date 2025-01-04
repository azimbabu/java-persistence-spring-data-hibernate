package com.azimbabu.javapersistence.springtesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import com.azimbabu.javapersistence.springtesting.model.Log;
import com.azimbabu.javapersistence.springtesting.model.User;
import com.azimbabu.javapersistence.springtesting.repository.LogRepository;
import com.azimbabu.javapersistence.springtesting.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
@Transactional
public class TransactionsManagementTest {

  @Value("${numUsers:10}")
  private int numUsers;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private LogRepository logRepository;

  @BeforeTransaction
  void beforeTransaction() {
    assumeFalse(TransactionSynchronizationManager.isActualTransactionActive());
//    logRepository.save(new Log("@BeforeTransaction"));
  }

  @RepeatedTest(2)
  void storeUpdateRetrieve() {
    assumeTrue(TransactionSynchronizationManager.isActualTransactionActive());

    List<User> users = UserHelper.buildUsers(numUsers);
    userRepository.saveAll(users);

    for (User user : users) {
      user.setName("Updated " + user.getName());
    }

    userRepository.saveAll(users);

    assertEquals(numUsers, users.size());
  }

  @AfterTransaction
  void afterTransaction() {
    assumeFalse(TransactionSynchronizationManager.isActualTransactionActive());
//    logRepository.save(new Log("@AfterTransaction"));
  }
}
