package com.azimbabu.javapersistence.ch11.transactions5.springdata.repository;

import com.azimbabu.javapersistence.ch11.transactions5.springdata.concurrency.Item;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

  Optional<Item> findByName(String name);
}
