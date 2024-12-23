package com.azimbabu.javapersistence.repository;

import com.azimbabu.javapersistence.model.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {

}
