package com.azimbabu.javapersistence.ch07.mappinginheritancetableperclass.repository;

import com.azimbabu.javapersistence.ch07.mappinginheritancetableperclass.model.BankAccount;
import java.util.List;

public interface BankAccountRepository extends BillingDetailsRepository<BankAccount, Long> {

  List<BankAccount> findBySwift(String swift);
}
