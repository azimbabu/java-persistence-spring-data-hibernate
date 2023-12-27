package com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.configuration.SpringDataConfiguration;
import com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.model.BankAccount;
import com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.model.BillingDetails;
import com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.model.CreditCard;
import com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.model.User;
import com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.repository.BankAccountRepository;
import com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.repository.BillingDetailsRepository;
import com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.repository.CreditCardRepository;
import com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class MappingInheritanceSpringDataJPATest {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BillingDetailsRepository billingDetailsRepository;

  @Autowired
  private CreditCardRepository creditCardRepository;

  @Autowired
  private BankAccountRepository bankAccountRepository;

  @Test
  void storeLoadEntities() {
    // create entities
    CreditCard creditCard1 = new CreditCard("John Smith", "123456789", "10", "2030");
    BankAccount bankAccount1 = new BankAccount("Jane Smith", "345678901", "Chase", "12345");

    CreditCard creditCard2 = new CreditCard("John Doe", "234567890", "11", "2031");
    BankAccount bankAccount2 = new BankAccount("Jane Doe", "4567890123", "Bank of America",
        "23451");

    User user1 = new User("Jack Smith");
    creditCard1.setUser(user1);
    user1.addBillingDetails(creditCard1);

    bankAccount1.setUser(user1);
    user1.addBillingDetails(bankAccount1);

    User user2 = new User("Jack Doe");
    creditCard2.setUser(user2);
    user2.addBillingDetails(creditCard2);

    bankAccount2.setUser(user2);
    user2.addBillingDetails(bankAccount2);

    // save entities
    userRepository.save(user1);
    creditCardRepository.save(creditCard1);
    bankAccountRepository.save(bankAccount1);

    userRepository.save(user2);
    billingDetailsRepository.save(creditCard2);
    billingDetailsRepository.save(bankAccount2);

    Map<Long, User> userMap = new HashMap<>();
    userMap.put(user1.getId(), user1);
    userMap.put(user2.getId(), user2);

    // load entities
    List<User> users = userRepository.findAll();
    assertEquals(2, users.size());

    for (User userActual : users) {
      User userExpected = userMap.get(userActual.getId());
      assertEquals(userExpected.getUsername(), userActual.getUsername());
      assertEquals(userExpected.getBillingDetails().size(), userActual.getBillingDetails().size());
    }
  }
}
