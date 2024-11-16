package com.azimbabu.javapersistence.ch11.transactions5.springdata.repository;

import com.azimbabu.javapersistence.ch11.transactions5.springdata.concurrency.Item;
import com.azimbabu.javapersistence.ch11.transactions5.springdata.concurrency.Log;
import com.azimbabu.javapersistence.ch11.transactions5.springdata.exception.DuplicateItemNameException;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ItemRepositoryImpl implements ItemRepositoryCustom {

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private LogRepository logRepository;

  @Override
  @Transactional
  public void addItem(String name, LocalDateTime createdAt) {
    logRepository.log("Adding item with name " + name);
    checkDuplicateName(name);
    itemRepository.save(new Item(name, createdAt));
  }

  @Override
  @Transactional(propagation = Propagation.MANDATORY)
  public void checkDuplicateName(String name) {
    if (itemRepository.findAll().stream().map(item -> item.getName())
        .filter(itemName -> itemName.equals(name)).count() > 0) {
      throw new DuplicateItemNameException("Item with name " + name + " already exists");
    }
  }

  @Override
  @Transactional
  public void addLogs() {
    logRepository.addSeparateLogsNotSupported();
  }

  @Override
  @Transactional
  public void showLogs() {
    logRepository.showLogs();
  }

  @Override
  @Transactional(noRollbackFor = DuplicateItemNameException.class)
  public void addItemNoRollback(String name, LocalDateTime createdAt) {
    logRepository.save(new Log("Adding log in method with no rollback for item " + name));
    checkDuplicateName(name);
    itemRepository.save(new Item(name, createdAt));
  }
}
