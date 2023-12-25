package com.azimbabu.javapersistence.ch07.mappinginheritancemixed.repository;

import com.azimbabu.javapersistence.ch07.mappinginheritancemixed.model.CreditCard;
import java.util.List;

public interface CreditCardRepository extends BillingDetailsRepository<CreditCard, Long> {
  List<CreditCard> findByCardNumber(String cardNumber);
}
