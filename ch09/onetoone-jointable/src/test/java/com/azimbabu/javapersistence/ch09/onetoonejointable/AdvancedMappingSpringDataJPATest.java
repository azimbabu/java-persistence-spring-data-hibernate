package com.azimbabu.javapersistence.ch09.onetoonejointable;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.azimbabu.javapersistence.ch09.onetoonejointable.configuration.SpringDataConfiguration;
import com.azimbabu.javapersistence.ch09.onetoonejointable.model.Item;
import com.azimbabu.javapersistence.ch09.onetoonejointable.model.Shipment;
import com.azimbabu.javapersistence.ch09.onetoonejointable.repository.ItemRepository;
import com.azimbabu.javapersistence.ch09.onetoonejointable.repository.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class AdvancedMappingSpringDataJPATest {

  @Autowired
  private ShipmentRepository shipmentRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Test
  void testStoreLoadEntities() {
    Shipment shipment = new Shipment();
    shipmentRepository.save(shipment);

    Item item = new Item("Foo");
    itemRepository.save(item);

    Shipment auctionShipment = new Shipment(item);
    shipmentRepository.save(auctionShipment);

    Item itemFound = itemRepository.findById(item.getId()).get();
    Shipment shipmentFound = shipmentRepository.findShipmentWithItem(shipment.getId());
    Shipment auctionShipmentFound = shipmentRepository.findShipmentWithItem(
        auctionShipment.getId());

    assertAll(() -> assertEquals(item.getName(), itemFound.getName()),
        () -> assertNull(shipmentFound.getAuction()),
        () -> assertEquals(item.getId(), auctionShipmentFound.getAuction().getId()),
        () -> assertEquals(item.getName(), auctionShipmentFound.getAuction().getName()),
        () -> assertEquals(auctionShipment.getShipmentState(),
            auctionShipmentFound.getShipmentState()));
  }
}
