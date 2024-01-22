package com.azimbabu.javapersistence.ch09.onetooneforeignkey.repository;

import com.azimbabu.javapersistence.ch09.onetooneforeignkey.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
