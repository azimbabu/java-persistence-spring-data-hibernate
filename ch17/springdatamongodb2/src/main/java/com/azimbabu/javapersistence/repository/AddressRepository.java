package com.azimbabu.javapersistence.repository;

import com.azimbabu.javapersistence.hibernateogm.model.Address;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AddressRepository extends MongoRepository<Address, String> {

}
