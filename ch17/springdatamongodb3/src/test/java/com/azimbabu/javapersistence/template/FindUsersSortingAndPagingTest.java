package com.azimbabu.javapersistence.template;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.model.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

class FindUsersSortingAndPagingTest extends SpringDataMongoDBApplicationTests {

  @Test
  void testOrder() {
    Query query1 = new Query();
    query1.with(Sort.by(Direction.ASC, "username"));
    User user1 = mongoTemplate.find(query1, User.class).get(0);
    assertEquals("beth", user1.getUsername());

    Query query2 = new Query();
    query2.with(Sort.by(Direction.DESC, "registrationDate"));
    User user2 = mongoTemplate.find(query2, User.class).get(0);
    assertEquals("julius", user2.getUsername());

    Query query3 = new Query();
    query3.addCriteria(Criteria.where("level").is(2));
    query3.limit(2);
    List<User> users = mongoTemplate.find(query3, User.class);
    assertEquals(2, users.size());

    Pageable pageRequest = PageRequest.of(1, 3);
    Query query4 = new Query();
    query4.with(pageRequest);
    List<User> usersPage = mongoTemplate.find(query4, User.class);
    assertAll(() -> assertEquals(3, usersPage.size()),
        () -> assertEquals("katie", usersPage.get(0).getUsername()),
        () -> assertEquals("beth", usersPage.get(1).getUsername()));
  }

  @Test
  void testFindByLevel() {
    Query query = new Query();
    query.addCriteria(Criteria.where("level").is(3));
    query.with(Sort.by(Direction.DESC, "registrationDate"));
    List<User> users = mongoTemplate.find(query, User.class);

    assertAll(() -> assertEquals(2, users.size()),
        () -> assertEquals("james", users.get(0).getUsername()));
  }

  @Test
  void testFindByActive() {
    Pageable pageRequest = PageRequest.of(1, 4);
    Query query = new Query();
    query.addCriteria(Criteria.where("active").is(Boolean.TRUE));
    query.with(pageRequest);
    query.with(Sort.by(Direction.ASC, "registrationDate"));
    List<User> users = mongoTemplate.find(query, User.class);

    assertAll(() -> assertEquals(4, users.size()),
        () -> assertEquals("burk", users.get(0).getUsername()));
  }
}
