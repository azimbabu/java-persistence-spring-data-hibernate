package com.azimbabu.javapersistence;

import com.azimbabu.javapersistence.hibernateogm.model.User;
import com.azimbabu.javapersistence.repository.UserRepository;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
abstract class SpringDataMongoDBApplicationTests {

  @Autowired
  UserRepository userRepository;

  @BeforeAll
  void beforeAll() {
    userRepository.saveAll(generateUsers());
  }

  private List<User> generateUsers() {
    List<User> users = new ArrayList<>();

    User john = new User("john", "John", "Smith");
    john.setRegistrationDate(LocalDate.of(2020, Month.APRIL, 13));
    john.setEmail("john@somedomain.com");
    john.setLevel(1);
    john.setActive(true);
    john.setPassword("password1");

    User mike = new User("mike", "Mike", "Nolan");
    mike.setRegistrationDate(LocalDate.of(2020, Month.JANUARY, 18));
    mike.setEmail("mike@somedomain.com");
    mike.setLevel(3);
    mike.setActive(true);
    mike.setPassword("password2");

    User james = new User("james", "James", "Woods");
    james.setRegistrationDate(LocalDate.of(2020, Month.MARCH, 11));
    james.setEmail("james@someotherdomain.com");
    james.setLevel(3);
    james.setActive(false);
    james.setPassword("password3");

    User katie = new User("katie", "Katie", "Sposato");
    katie.setRegistrationDate(LocalDate.of(2021, Month.JANUARY, 5));
    katie.setEmail("katie@somedomain.com");
    katie.setLevel(5);
    katie.setActive(true);
    katie.setPassword("password4");

    User beth = new User("beth", "Beth", "Gerrard");
    beth.setRegistrationDate(LocalDate.of(2020, Month.AUGUST, 3));
    beth.setEmail("beth@somedomain.com");
    beth.setLevel(2);
    beth.setActive(true);
    beth.setPassword("password5");

    User julius = new User("julius", "Julius", "Graves");
    julius.setRegistrationDate(LocalDate.of(2021, Month.FEBRUARY, 9));
    julius.setEmail("julius@someotherdomain.com");
    julius.setLevel(4);
    julius.setActive(true);
    julius.setPassword("password6");

    User darren = new User("darren", "Darren", "Perkins");
    darren.setRegistrationDate(LocalDate.of(2020, Month.DECEMBER, 11));
    darren.setEmail("darren@somedomain.com");
    darren.setLevel(2);
    darren.setActive(true);
    darren.setPassword("password7");

    User marion = new User("marion", "Marion", "Sherman");
    marion.setRegistrationDate(LocalDate.of(2020, Month.SEPTEMBER, 23));
    marion.setEmail("marion@someotherdomain.com");
    marion.setLevel(2);
    marion.setActive(false);
    marion.setPassword("password8");

    User stephanie = new User("stephanie", "Stephanie", "Christensen");
    stephanie.setRegistrationDate(LocalDate.of(2020, Month.JANUARY, 18));
    stephanie.setEmail("stephanie@someotherdomain.com");
    stephanie.setLevel(4);
    stephanie.setActive(true);
    stephanie.setPassword("password9");

    User burk = new User("burk", "Burk", "Smith");
    burk.setRegistrationDate(LocalDate.of(2020, Month.NOVEMBER, 28));
    burk.setEmail("burk@somedomain.com");
    burk.setLevel(1);
    burk.setActive(true);
    burk.setPassword("password10");

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

  @AfterAll
  void afterAll() {
    userRepository.deleteAll();
  }
}
