package com.azimbabu.javapersistence.ch07.mappinginheritancetableperclass;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch07.mappinginheritancetableperclass.configuration.SpringDataConfiguration;
import com.azimbabu.javapersistence.ch07.mappinginheritancetableperclass.model.BankAccount;
import com.azimbabu.javapersistence.ch07.mappinginheritancetableperclass.model.BillingDetails;
import com.azimbabu.javapersistence.ch07.mappinginheritancetableperclass.model.CreditCard;
import com.azimbabu.javapersistence.ch07.mappinginheritancetableperclass.repository.BankAccountRepository;
import com.azimbabu.javapersistence.ch07.mappinginheritancetableperclass.repository.BillingDetailsRepository;
import com.azimbabu.javapersistence.ch07.mappinginheritancetableperclass.repository.CreditCardRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class MappingInheritanceSpringDataJPATest {
  @Autowired
  private CreditCardRepository creditCardRepository;

  @Autowired
  private BankAccountRepository bankAccountRepository;

  @Autowired
  private BillingDetailsRepository billingDetailsRepository;

  @Test
  void storeLoadEntities() {
    // create entities
    CreditCard creditCard = new CreditCard("John Smith", "123456789", "10", "2030");
    creditCardRepository.save(creditCard);

    BankAccount bankAccount = new BankAccount("Mike Johnson", "12345", "Delta Bank", "BANKXY12");
    bankAccountRepository.save(bankAccount);

    // load entities
    List<CreditCard> creditCards = creditCardRepository.findByOwner("John Smith");
    List<BankAccount> bankAccounts = bankAccountRepository.findByOwner("Mike Johnson");
    List<CreditCard> creditCards2 = creditCardRepository.findByExpYear("2030");
    List<BankAccount> bankAccounts2 = bankAccountRepository.findBySwift("BANKXY12");

    assertAll(() -> assertEquals(1, creditCards.size()),
        () -> assertEquals("John Smith", creditCards.get(0).getOwner()),
        () -> assertEquals("123456789", creditCards.get(0).getCardNumber()),
        () -> assertEquals("10", creditCards.get(0).getExpMonth()),
        () -> assertEquals("2030", creditCards.get(0).getExpYear()));

    assertAll(() -> assertEquals(1, bankAccounts.size()),
        () -> assertEquals("Mike Johnson", bankAccounts.get(0).getOwner()),
        () -> assertEquals("12345", bankAccounts.get(0).getAccount()),
        () -> assertEquals("Delta Bank", bankAccounts.get(0).getBankName()),
        () -> assertEquals("BANKXY12", bankAccounts.get(0).getSwift()));

    assertAll(() -> assertEquals(1, creditCards2.size()),
        () -> assertEquals("John Smith", creditCards2.get(0).getOwner()),
        () -> assertEquals("123456789", creditCards2.get(0).getCardNumber()),
        () -> assertEquals("10", creditCards2.get(0).getExpMonth()),
        () -> assertEquals("2030", creditCards2.get(0).getExpYear()));

    assertAll(() -> assertEquals(1, bankAccounts2.size()),
        () -> assertEquals("Mike Johnson", bankAccounts2.get(0).getOwner()),
        () -> assertEquals("12345", bankAccounts2.get(0).getAccount()),
        () -> assertEquals("Delta Bank", bankAccounts2.get(0).getBankName()),
        () -> assertEquals("BANKXY12", bankAccounts2.get(0).getSwift()));

    List<BillingDetails> billingDetails = billingDetailsRepository.findByOwner("John Smith");
    List<BillingDetails> billingDetails2 = billingDetailsRepository.findByOwner("Mike Johnson");
    List<BillingDetails> billingDetails3 = billingDetailsRepository.findAll();

    assertAll(() -> assertEquals(1, billingDetails.size()),
        () -> assertEquals(1, billingDetails2.size()),
        () -> assertEquals(2, billingDetails3.size()));
  }
}
