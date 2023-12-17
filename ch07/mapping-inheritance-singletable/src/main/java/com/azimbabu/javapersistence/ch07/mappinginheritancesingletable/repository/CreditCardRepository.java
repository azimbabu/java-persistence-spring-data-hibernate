package com.azimbabu.javapersistence.ch07.mappinginheritancesingletable.repository;

import com.azimbabu.javapersistence.ch07.mappinginheritancesingletable.model.CreditCard;
import java.util.List;

public interface CreditCardRepository extends BillingDetailsRepository<CreditCard, Long> {

  List<CreditCard> findByExpYear(String expYear);
}
