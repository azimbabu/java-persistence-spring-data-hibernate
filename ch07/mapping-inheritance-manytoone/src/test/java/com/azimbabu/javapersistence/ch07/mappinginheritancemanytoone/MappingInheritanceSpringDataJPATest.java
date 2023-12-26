package com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone.configuration.SpringDataConfiguration;
import com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone.model.BankAccount;
import com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone.model.CreditCard;
import com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone.model.User;
import com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone.repository.BankAccountRepository;
import com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone.repository.BillingDetailsRepository;
import com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone.repository.CreditCardRepository;
import com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone.repository.UserRepository;
import java.math.BigDecimal;
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
  private CreditCardRepository creditCardRepository;

  @Autowired
  private BankAccountRepository bankAccountRepository;

  @Autowired
  private BillingDetailsRepository billingDetailsRepository;

  @Test
  void storeLoadEntities() {
    // create entities
    CreditCard creditCard1 = new CreditCard("John Smith", "123456789", "10", "2030");
    CreditCard creditCard2 = new CreditCard("John Doe", "234567890", "11", "2031");

    BankAccount bankAccount1 = new BankAccount("Jane Smith", "345678901", "Chase", "12345");
    BankAccount bankAccount2 = new BankAccount("Jane Doe", "4567890123", "Bank of America",
        "23451");

    User user1 = new User("Jack Smith");
    user1.setDefaultBilling(creditCard1);

    User user2 = new User("Sean Smith");
    user2.setDefaultBilling(creditCard1);

    User user3 = new User("Jack Doe");
    user3.setDefaultBilling(creditCard2);

    User user4 = new User("Sean Doe");
    user4.setDefaultBilling(creditCard2);

    User user5 = new User("Mary Smith");
    user5.setDefaultBilling(bankAccount1);

    User user6 = new User("Mona Smith");
    user6.setDefaultBilling(bankAccount1);

    User user7 = new User("Mary Doe");
    user7.setDefaultBilling(bankAccount2);

    User user8 = new User("Mona Doe");
    user8.setDefaultBilling(bankAccount2);

    // save entities
    creditCardRepository.save(creditCard1);
    billingDetailsRepository.save(creditCard2);

    bankAccountRepository.save(bankAccount1);
    billingDetailsRepository.save(bankAccount2);

    // save entities
    userRepository.save(user1);
    userRepository.save(user2);
    userRepository.save(user3);
    userRepository.save(user4);
    userRepository.save(user5);
    userRepository.save(user6);
    userRepository.save(user7);
    userRepository.save(user8);

    Map<Long, User> userByIdMap = new HashMap<>();
    userByIdMap.put(user1.getId(), user1);
    userByIdMap.put(user2.getId(), user2);
    userByIdMap.put(user3.getId(), user3);
    userByIdMap.put(user4.getId(), user4);
    userByIdMap.put(user5.getId(), user5);
    userByIdMap.put(user6.getId(), user6);
    userByIdMap.put(user7.getId(), user7);
    userByIdMap.put(user8.getId(), user8);

    // pay
    user1.getDefaultBilling().pay(BigDecimal.valueOf(101));
    user2.getDefaultBilling().pay(BigDecimal.valueOf(102));
    user3.getDefaultBilling().pay(BigDecimal.valueOf(103));
    user4.getDefaultBilling().pay(BigDecimal.valueOf(104));
    user5.getDefaultBilling().pay(BigDecimal.valueOf(105));
    user6.getDefaultBilling().pay(BigDecimal.valueOf(106));
    user7.getDefaultBilling().pay(BigDecimal.valueOf(107));
    user8.getDefaultBilling().pay(BigDecimal.valueOf(108));

    // load entities
    List<User> users = userRepository.findAll();

    assertEquals(8, users.size());
    for (User user : users) {
      User userExpected = userByIdMap.get(user.getId());
      assertAll(() -> assertEquals(userExpected.getUsername(), user.getUsername()),
          () -> assertEquals(userExpected.getDefaultBilling().getOwner(),
              user.getDefaultBilling().getOwner()));

      if (userExpected.getDefaultBilling() instanceof BankAccount bankAccountExpected) {
        BankAccount bankAccountActual = (BankAccount) user.getDefaultBilling();
        assertAll(
            () -> assertEquals(bankAccountExpected.getAccount(), bankAccountActual.getAccount()),
            () -> assertEquals(bankAccountExpected.getBankName(), bankAccountActual.getBankName()),
            () -> assertEquals(bankAccountExpected.getSwift(), bankAccountActual.getSwift()));
      } else {
        CreditCard creditCardExpected = (CreditCard) userExpected.getDefaultBilling();
        CreditCard creditCardActual = (CreditCard) user.getDefaultBilling();
        assertAll(() -> assertEquals(creditCardExpected.getCardNumber(),
                creditCardActual.getCardNumber()),
            () -> assertEquals(creditCardExpected.getExpMonth(), creditCardActual.getExpMonth()),
            () -> assertEquals(creditCardExpected.getExpYear(), creditCardActual.getExpYear()));
      }
    }
  }
}
