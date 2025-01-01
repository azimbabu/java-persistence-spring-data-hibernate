package com.azimbabu.javapersistence.hibernateogm.model;

import org.springframework.data.relational.core.mapping.Table;

@Table("USER_ADDRESSES")
public class UserAddress {
  private Long addressId;

  public UserAddress(Long addressId) {
    this.addressId = addressId;
  }

  public Long getAddressId() {
    return addressId;
  }
}
