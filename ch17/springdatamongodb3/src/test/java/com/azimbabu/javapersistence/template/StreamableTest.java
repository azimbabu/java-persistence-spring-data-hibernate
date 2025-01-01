package com.azimbabu.javapersistence.template;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.hibernateogm.model.User;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

class StreamableTest extends SpringDataMongoDBApplicationTests {

  @Test
  void testStreamable() {
    Query query = new Query();
    query.addCriteria(new Criteria().orOperator(Criteria.where("email").regex(".*someother.*"),
        Criteria.where("level").is(2)));
    try(Stream<User> userStream = mongoTemplate.stream(query, User.class).distinct()) {
      assertEquals(6, userStream.count());
    }
  }
}
