package com.azimbabu.javapersistence.ch11.transactions5.springdata;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.azimbabu.javapersistence.ch11.transactions5.springdata.configuration.SpringDataConfiguration;
import com.azimbabu.javapersistence.ch11.transactions5.springdata.exception.DuplicateItemNameException;
import com.azimbabu.javapersistence.ch11.transactions5.springdata.repository.ItemRepository;
import com.azimbabu.javapersistence.ch11.transactions5.springdata.repository.LogRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.IllegalTransactionStateException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class TransactionPropagationTest {

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private LogRepository logRepository;

  @BeforeEach
  void clean() {
    itemRepository.deleteAll();
    logRepository.deleteAll();
  }

  @Test
  void notSupported() {
    assertAll(() -> assertThrows(RuntimeException.class, () -> itemRepository.addLogs()),
        () -> assertEquals(1, logRepository.findAll().size()),
        () -> assertEquals("check from not supported 1",
            logRepository.findAll().get(0).getMessage()));
    logRepository.showLogs();
  }

  @Test
  void supports() {
    assertAll(
        () -> assertThrows(RuntimeException.class, () -> logRepository.addSeparateLogsSupports()),
        () -> assertEquals(1, logRepository.findAll().size()),
        () -> assertEquals("check from supports 1",
            logRepository.findAll().get(0).getMessage())
    );
    logRepository.showLogs();
  }

  @Test
  void mandatory() {
    IllegalTransactionStateException ex = assertThrows(
        IllegalTransactionStateException.class, () -> itemRepository.checkDuplicateName("Item 1"));
    assertEquals(
        "No existing transaction found for transaction marked with propagation 'mandatory'",
        ex.getMessage());
  }

  @Test
  void never() {
    itemRepository.addItem("Item1", LocalDateTime.of(2024, 01, 31, 10, 25, 30));
    logRepository.showLogs();

    IllegalTransactionStateException ex = assertThrows(
        IllegalTransactionStateException.class, () -> itemRepository.showLogs());

    assertEquals("Existing transaction found for transaction marked with propagation 'never'",
        ex.getMessage());
  }

  @Test
  void requiresNew() {
    itemRepository.addItem("Item1", LocalDateTime.of(2024, 01, 25, 10, 25, 30));
    itemRepository.addItem("Item2", LocalDateTime.of(2024, 01, 26, 11, 26, 31));
    itemRepository.addItem("Item3", LocalDateTime.of(2024, 01, 27, 9, 28, 28));

    DuplicateItemNameException ex = assertThrows(DuplicateItemNameException.class,
        () -> itemRepository.addItem("Item2", LocalDateTime.of(2024, 02, 26, 9, 28, 28)));

    assertAll(
        () -> assertEquals("Item with name Item2 already exists", ex.getMessage()),
        () -> assertEquals(4, logRepository.findAll().size()),
        () -> assertEquals(3, itemRepository.findAll().size())
    );

    System.out.println("Logs: ");
    logRepository.findAll().forEach(System.out::println);

    System.out.println("List of added items: ");
    itemRepository.findAll().forEach(System.out::println);
  }

  @Test
  void noRollback() {
    itemRepository.addItemNoRollback("Item1", LocalDateTime.of(2024, 01, 25, 10, 25, 30));
    itemRepository.addItemNoRollback("Item2", LocalDateTime.of(2024, 01, 26, 11, 26, 31));
    itemRepository.addItemNoRollback("Item3", LocalDateTime.of(2024, 01, 27, 9, 28, 28));

    DuplicateItemNameException ex = assertThrows(DuplicateItemNameException.class,
        () -> itemRepository.addItemNoRollback("Item2", LocalDateTime.of(2024, 02, 26, 9, 28, 28)));

    assertAll(
        () -> assertEquals("Item with name Item2 already exists", ex.getMessage()),
        () -> assertEquals(4, logRepository.findAll().size()),
        () -> assertEquals(3, itemRepository.findAll().size())
    );

    System.out.println("Logs: ");
    logRepository.findAll().forEach(System.out::println);

    System.out.println("List of added items: ");
    itemRepository.findAll().forEach(System.out::println);
  }
}
