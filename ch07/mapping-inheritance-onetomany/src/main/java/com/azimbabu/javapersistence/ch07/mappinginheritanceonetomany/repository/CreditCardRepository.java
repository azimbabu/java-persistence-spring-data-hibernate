package com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.repository;

import com.azimbabu.javapersistence.ch07.mappinginheritanceonetomany.model.CreditCard;
import java.util.List;

public interface CreditCardRepository extends BillingDetailsRepository<CreditCard, Long> {

  List<CreditCard> findByCardNumber(String cardNumber);
}
