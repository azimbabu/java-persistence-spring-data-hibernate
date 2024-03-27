package com.azimbabu.javapersistence.ch09.onetomanyembeddable.repository;

import com.azimbabu.javapersistence.ch09.onetomanyembeddable.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

}
