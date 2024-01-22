package com.azimbabu.javapersistence.ch09.onetoonesharedprimarykey.repository;

import com.azimbabu.javapersistence.ch09.onetoonesharedprimarykey.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
