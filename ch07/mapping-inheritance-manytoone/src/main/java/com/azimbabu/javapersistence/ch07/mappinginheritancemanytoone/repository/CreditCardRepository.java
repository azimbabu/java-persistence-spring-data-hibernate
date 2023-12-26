package com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone.repository;

import com.azimbabu.javapersistence.ch07.mappinginheritancemanytoone.model.CreditCard;
import java.util.List;

public interface CreditCardRepository extends BillingDetailsRepository<CreditCard, Long> {

  List<CreditCard> findByCardNumber(String cardNumber);
}
