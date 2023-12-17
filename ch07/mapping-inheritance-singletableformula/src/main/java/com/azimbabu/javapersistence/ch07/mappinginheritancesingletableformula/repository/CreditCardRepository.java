package com.azimbabu.javapersistence.ch07.mappinginheritancesingletableformula.repository;

import com.azimbabu.javapersistence.ch07.mappinginheritancesingletableformula.model.CreditCard;
import java.util.List;

public interface CreditCardRepository extends BillingDetailsRepository<CreditCard, Long> {

  List<CreditCard> findByExpYear(String expYear);
}
