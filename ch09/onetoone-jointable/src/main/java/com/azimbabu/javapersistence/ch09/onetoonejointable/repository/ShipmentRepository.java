package com.azimbabu.javapersistence.ch09.onetoonejointable.repository;

import com.azimbabu.javapersistence.ch09.onetoonejointable.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

  @Query("select s from Shipment s left join fetch s.auction where s.id = :id")
  Shipment findShipmentWithItem(@Param("id") Long id);
}
