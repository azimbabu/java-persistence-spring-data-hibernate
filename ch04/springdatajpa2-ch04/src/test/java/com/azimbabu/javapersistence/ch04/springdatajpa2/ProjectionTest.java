package com.azimbabu.javapersistence.ch04.springdatajpa2;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.ch04.springdatajpa2.model.Projection;
import com.azimbabu.javapersistence.ch04.springdatajpa2.model.Projection.UserSummary;
import com.azimbabu.javapersistence.ch04.springdatajpa2.model.Projection.UsernameOnly;
import com.azimbabu.javapersistence.ch04.springdatajpa2.model.User;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ProjectionTest extends SpringDataJpaApplicationTests {

  @Test
  void testFindByRegistrationDateAfter() {
    List<UserSummary> userSummaries = userRepository.findByRegistrationDateAfter(
        LocalDate.of(2021, Month.FEBRUARY, 1));
    assertAll(() -> assertEquals(1, userSummaries.size()),
        () -> assertEquals("julius", userSummaries.get(0).getUsername()),
        () -> assertEquals("julius julius@someotherdomain.com", userSummaries.get(0).getInfo()));
  }

  @Test
  void testFindByEmail() {
    List<UsernameOnly> usernames = userRepository.findByEmail("john@somedomain.com");
    assertAll(() -> assertEquals(1, usernames.size()),
        () -> assertEquals("john", usernames.get(0).getUsername()));
  }

  @Test
  void testFindByEmailDynamicProjection() {
    List<UsernameOnly> usernames = userRepository.findByEmail("mike@somedomain.com",
        Projection.UsernameOnly.class);
    List<User> users = userRepository.findByEmail("mike@somedomain.com", User.class);
    assertAll(() -> assertEquals(1, usernames.size()),
        () -> assertEquals("mike", usernames.get(0).getUsername()),
        () -> assertEquals(1, users.size()),
        () -> assertEquals("mike", users.get(0).getUsername()));
  }
}
