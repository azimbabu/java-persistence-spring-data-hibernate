package com.azimbabu.javapersistence.template;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.azimbabu.javapersistence.hibernateogm.model.User;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

class FindUsersTest extends SpringDataMongoDBApplicationTests {

  @Test
  void testFindAll() {
    List<User> users = mongoTemplate.find(new Query(), User.class);
    assertEquals(10, users.size());
  }

  @Test
  void testFindUser() {
    Query query = new Query();
    query.addCriteria(Criteria.where("username").is("beth"));

    List<User> users = mongoTemplate.find(query, User.class);
    assertAll(() -> assertEquals(1, users.size()),
        () -> assertNotNull(users.get(0).getId()),
        () -> assertEquals("beth", users.get(0).getUsername()));
  }

  @Test
  void testFindAllByOrderByUsernameAsc() {
    Query query = new Query();
    query.with(Sort.by(Direction.ASC, "username"));

    List<User> users = mongoTemplate.find(query, User.class);
    assertAll(() -> assertEquals(10, users.size()),
        () -> assertEquals("beth", users.get(0).getUsername()),
        () -> assertEquals("stephanie", users.get(users.size() - 1).getUsername()));
  }

  @Test
  void testFindByRegistrationDateBetween() {
    Query query = new Query();
    query.addCriteria(Criteria.where("registrationDate").gte(LocalDate.of(2020, Month.JULY, 1))
        .lt(LocalDate.of(2020, Month.DECEMBER, 31)));

    List<User> users = mongoTemplate.find(query, User.class);
    assertEquals(4, users.size());
  }

  @Test
  void testFindByUsernameEmail() {
    Query query1 = new Query();
    query1.addCriteria(Criteria.where("username").is("mike").andOperator(Criteria.where("email").is("mike@somedomain.com")));
    List<User> users1 = mongoTemplate.find(query1, User.class);
    assertEquals(1, users1.size());

    Query query2 = new Query(new Criteria().orOperator(Criteria.where("username").is("mike"), Criteria.where("email").is("beth@somedomain.com")));
    List<User> users2 = mongoTemplate.find(query2, User.class);
    assertEquals(2, users2.size());

    Query query3 = new Query(new Criteria().orOperator(Criteria.where("username").is("mike"), Criteria.where("email").is("beth@somedomain.com")));
    query3.addCriteria(Criteria.where("username").is("mike").andOperator(Criteria.where("email").is("beth@somedomain.com")));
    List<User> users3 = mongoTemplate.find(query3, User.class);
    assertEquals(0, users3.size());

    Query query4 = new Query(new Criteria().orOperator(Criteria.where("username").is("beth"),
        Criteria.where("email").is("beth@somedomain.com")));
    List<User> users4 = mongoTemplate.find(query4, User.class);
    assertEquals(1, users4.size());

    Query query5 = new Query();
    query5.addCriteria(new Criteria().andOperator(Criteria.where("username").is("mike"), Criteria.where("email").is("mike@somedomain.com")));
    List<User> users5 = mongoTemplate.find(query5, User.class);
    assertEquals(1, users5.size());
  }

  @Test
  void testFindByUsernameIgnoreCase() {
    Query query = new Query();
    Criteria regex = Criteria.where("username").regex("MIKE", "i");
    query.addCriteria(regex);
    List<User> users = mongoTemplate.find(query, User.class);

    assertAll(() -> assertEquals(1, users.size()),
        () -> assertEquals("mike", users.get(0).getUsername()));
  }

  @Test
  void testFindByLevelOrderByUsernameDesc() {
    Query query = new Query();
    query.addCriteria(Criteria.where("level").is(1)).with(Sort.by(Direction.DESC, "username"));
    List<User> users = mongoTemplate.find(query, User.class);

    assertAll(() -> assertEquals(2, users.size()),
        () -> assertEquals("john", users.get(0).getUsername()),
        () -> assertEquals("burk", users.get(users.size()-1).getUsername()));
  }

  @Test
  void testFindByLevelGreaterThanEqual() {
    Query query = new Query();
    query.addCriteria(Criteria.where("level").gte(3));
    List<User> users = mongoTemplate.find(query, User.class);

    assertEquals(5, users.size());
  }

  @Test
  void testFindByUsername() {
    Query query1 = new Query();
    Criteria criteria1 = Criteria.where("username").regex(".*ar.*");
    query1.addCriteria(criteria1);
    List<User> usersContaining = mongoTemplate.find(query1, User.class);
    assertEquals(2, usersContaining.size());

    Query query2 = new Query();
    Criteria criteria2 = Criteria.where("username").regex("^b.*");
    query2.addCriteria(criteria2);
    List<User> usersStarting = mongoTemplate.find(query2, User.class);
    assertEquals(2, usersStarting.size());

    Query query3 = new Query();
    Criteria criteria3 = Criteria.where("username").regex("ie$");
    query3.addCriteria(criteria3);
    List<User> usersEnding = mongoTemplate.find(query3, User.class);
    assertEquals(2, usersEnding.size());
  }

  @Test
  void testFindByActive() {
    Query queryActive = new Query().addCriteria(Criteria.where("active").is(Boolean.TRUE));
    List<User> usersActive = mongoTemplate.find(queryActive, User.class);
    assertEquals(8, usersActive.size());

    Query queryInactive = new Query().addCriteria(Criteria.where("active").is(Boolean.FALSE));
    List<User> usersInActive = mongoTemplate.find(queryInactive, User.class);
    assertEquals(2, usersInActive.size());
  }

  @Test
  void testFindByRegistrationDateInNotIn() {
    LocalDate date1 = LocalDate.of(2020, Month.JANUARY, 18);
    LocalDate date2 = LocalDate.of(2021, Month.JANUARY, 5);
    List<LocalDate> dates = List.of(date1, date2);

    Query query1 = new Query();
    query1.addCriteria(Criteria.where("registrationDate").in(dates));
    List<User> users1 = mongoTemplate.find(query1, User.class);
    assertEquals(3, users1.size());

    Query query2 = new Query();
    query2.addCriteria(Criteria.where("registrationDate").not().in(dates));
    List<User> users2 = mongoTemplate.find(query2, User.class);
    assertEquals(7, users2.size());
  }

  @Test
  void findByLastName() {
    Query query = new Query();
    query.addCriteria(Criteria.where("lastName").is("Smith"));
    List<User> users = mongoTemplate.find(query, User.class);
    assertEquals(2, users.size());
  }
}
