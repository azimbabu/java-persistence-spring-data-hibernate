package com.azimbabu.javapersistence.ch07.mappinginheritancemixed.repository;

import com.azimbabu.javapersistence.ch07.mappinginheritancemixed.model.BillingDetails;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingDetailsRepository<T extends BillingDetails, ID> extends JpaRepository<T, ID> {
  List<T> findByOwner(String owner);
}
