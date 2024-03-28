package com.azimbabu.javapersistence.ch09.onetomanyembeddablejointable.repository;

import com.azimbabu.javapersistence.ch09.onetomanyembeddablejointable.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
