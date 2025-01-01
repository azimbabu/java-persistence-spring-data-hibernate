package com.azimbabu.javapersistence.repository;

import com.azimbabu.javapersistence.hibernateogm.model.Address;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {
  @Query("SELECT COUNT(*) FROM ADDRESSES WHERE USER_ID = :USER_ID")
  int countByUserId(@Param("USER_ID") Long userId);
}
