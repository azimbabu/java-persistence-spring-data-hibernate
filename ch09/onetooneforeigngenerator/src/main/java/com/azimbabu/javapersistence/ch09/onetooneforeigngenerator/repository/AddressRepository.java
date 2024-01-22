package com.azimbabu.javapersistence.ch09.onetooneforeigngenerator.repository;

import com.azimbabu.javapersistence.ch09.onetooneforeigngenerator.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
