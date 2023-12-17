package com.azimbabu.javapersistence.ch07.mappinginheritancemappedsuperclass.repository;

import com.azimbabu.javapersistence.ch07.mappinginheritancemappedsuperclass.model.BillingDetails;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BillingDetailsRepository<T extends BillingDetails, ID> extends
    JpaRepository<T, ID> {

  List<T> findByOwner(String owner);
}
