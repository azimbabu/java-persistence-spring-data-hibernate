package com.azimbabu.javapersistence.ch07.mappinginheritancesingletable.repository;

import com.azimbabu.javapersistence.ch07.mappinginheritancesingletable.model.BankAccount;
import java.util.List;

public interface BankAccountRepository extends BillingDetailsRepository<BankAccount, Long> {

  List<BankAccount> findBySwift(String swift);
}
