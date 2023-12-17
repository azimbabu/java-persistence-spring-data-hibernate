package com.azimbabu.javapersistence.ch07.mappinginheritancetableperclass.repository;

import com.azimbabu.javapersistence.ch07.mappinginheritancetableperclass.model.CreditCard;
import java.util.List;

public interface CreditCardRepository extends BillingDetailsRepository<CreditCard, Long> {

  List<CreditCard> findByExpYear(String expYear);
}
