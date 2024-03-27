package com.azimbabu.javapersistence.ch09.onetomanyembeddable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch09.onetomanyembeddable.model.Address;
import com.azimbabu.javapersistence.ch09.onetomanyembeddable.model.Shipment;
import com.azimbabu.javapersistence.ch09.onetomanyembeddable.model.User;
import com.azimbabu.javapersistence.ch09.onetomanyembeddable.repository.ShipmentRepository;
import com.azimbabu.javapersistence.ch09.onetomanyembeddable.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ShipmentRepository shipmentRepository;

  @Transactional
  public void storeLoadEntities() {
    User user = new User("John Smith");
    Address deliveryAddress = new Address("Flowers Street", "01246", "Boston");
    user.setShippingAddress(deliveryAddress);
    userRepository.save(user);

    Shipment firstShipment = new Shipment();
    deliveryAddress.addDelivery(firstShipment);
    shipmentRepository.save(firstShipment);

    Shipment secondShipment = new Shipment();
    deliveryAddress.addDelivery(secondShipment);
    shipmentRepository.save(secondShipment);

    User userFound = userRepository.findById(user.getId()).get();
    assertEquals(2, userFound.getShippingAddress().getDeliveries().size());
  }
}
