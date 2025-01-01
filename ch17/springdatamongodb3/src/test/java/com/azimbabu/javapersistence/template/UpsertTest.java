package com.azimbabu.javapersistence.template;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.hibernateogm.model.User;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

class UpsertTest extends SpringDataMongoDBApplicationTests {

  @Test
  void testUpsert() {
    Query query = new Query();
    query.addCriteria(Criteria.where("level").is(1));

    Update update = new Update();
    update.set("level", 2);
    UpdateResult updateResult = mongoTemplate.upsert(query, update, User.class);

    assertAll(() -> assertEquals(1, updateResult.getMatchedCount()),
        () -> assertEquals(1, updateResult.getModifiedCount()));
  }
}
