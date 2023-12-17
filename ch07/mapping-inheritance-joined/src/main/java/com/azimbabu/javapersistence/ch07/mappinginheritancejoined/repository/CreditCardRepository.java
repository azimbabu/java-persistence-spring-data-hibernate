package com.azimbabu.javapersistence.ch07.mappinginheritancejoined.repository;

import com.azimbabu.javapersistence.ch07.mappinginheritancejoined.model.CreditCard;
import java.util.List;

public interface CreditCardRepository extends BillingDetailsRepository<CreditCard, Long> {
  List<CreditCard> findByCardNumber(String cardNumber);
}
