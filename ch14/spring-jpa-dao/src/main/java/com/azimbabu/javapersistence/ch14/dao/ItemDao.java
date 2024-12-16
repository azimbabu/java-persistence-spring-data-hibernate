package com.azimbabu.javapersistence.ch14.dao;

import com.azimbabu.javapersistence.ch14.Item;
import java.util.List;

public interface ItemDao {

  Item getBydId(long id);

  List<Item> getAll();

  void insert(Item item);

  void update(long id, String name);

  void delete(Item item);

  Item findByName(String name);
}
