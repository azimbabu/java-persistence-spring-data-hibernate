package com.azimbabu.javapersistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.model.Address;
import com.azimbabu.javapersistence.model.User;
import com.azimbabu.javapersistence.repository.AddressRepository;
import com.azimbabu.javapersistence.repository.UserRepository;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class UserAddressOnetoOneTest {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AddressRepository addressRepository;

  private List<User> users = new ArrayList<>();

  @BeforeEach
  void setUp() {
    userRepository.saveAll(generateUsers());
  }

  private List<User> generateUsers() {
    User john = new User("john", LocalDate.of(2020, Month.APRIL, 13));
    john.setEmail("john@somedomain.com");
    john.setLevel(1);
    john.setActive(true);
    john.setAddress(generateAddress("1"));

    User mike = new User("mike", LocalDate.of(2020, Month.JANUARY, 18));
    mike.setEmail("mike@somedomain.com");
    mike.setLevel(3);
    mike.setActive(true);
    mike.setAddress(generateAddress("2"));

    User james = new User("james", LocalDate.of(2020, Month.MARCH, 11));
    james.setEmail("james@someotherdomain.com");
    james.setLevel(3);
    james.setActive(false);
    james.setAddress(generateAddress("3"));

    User katie = new User("katie", LocalDate.of(2021, Month.JANUARY, 5));
    katie.setEmail("katie@somedomain.com");
    katie.setLevel(5);
    katie.setActive(true);
    katie.setAddress(generateAddress("4"));

    User beth = new User("beth", LocalDate.of(2020, Month.AUGUST, 3));
    beth.setEmail("beth@somedomain.com");
    beth.setLevel(2);
    beth.setActive(true);
    beth.setAddress(generateAddress("5"));

    User julius = new User("julius", LocalDate.of(2021, Month.FEBRUARY, 9));
    julius.setEmail("julius@someotherdomain.com");
    julius.setLevel(4);
    julius.setActive(true);
    julius.setAddress(generateAddress("6"));

    User darren = new User("darren", LocalDate.of(2020, Month.DECEMBER, 11));
    darren.setEmail("darren@somedomain.com");
    darren.setLevel(2);
    darren.setActive(true);
    darren.setAddress(generateAddress("7"));

    User marion = new User("marion", LocalDate.of(2020, Month.SEPTEMBER, 23));
    marion.setEmail("marion@someotherdomain.com");
    marion.setLevel(2);
    marion.setActive(false);
    marion.setAddress(generateAddress("8"));

    User stephanie = new User("stephanie", LocalDate.of(2020, Month.JANUARY, 18));
    stephanie.setEmail("stephanie@someotherdomain.com");
    stephanie.setLevel(4);
    stephanie.setActive(true);
    stephanie.setAddress(generateAddress("9"));

    User burk = new User("burk", LocalDate.of(2020, Month.NOVEMBER, 28));
    burk.setEmail("burk@somedomain.com");
    burk.setLevel(1);
    burk.setActive(true);
    burk.setAddress(generateAddress("10"));

    users.add(john);
    users.add(mike);
    users.add(james);
    users.add(katie);
    users.add(beth);
    users.add(julius);
    users.add(darren);
    users.add(marion);
    users.add(stephanie);
    users.add(burk);

    return users;
  }

  private Address generateAddress(String number) {
    return new Address("New York", number + ", 5th Avenue");
  }

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
  }

  @Test
  void oneToOneTest() {
    assertAll(() -> assertEquals(10, userRepository.count()),
        () -> assertEquals(10, addressRepository.count()));
  }
}
