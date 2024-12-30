package com.azimbabu.javapersistence.template;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.azimbabu.javapersistence.model.User;
import com.mongodb.client.result.UpdateResult;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

class UpdateMultiTest extends SpringDataMongoDBApplicationTests {

  @Test
  void testUpdateMulti() {
    Query query = new Query();
    query.addCriteria(Criteria.where("level").is(1));

    Update update = new Update();
    update.set("level", 2);
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, User.class);

    List<User> users = mongoTemplate.find(query, User.class);

    assertAll(() -> assertEquals(2, updateResult.getMatchedCount()),
        () -> assertEquals(2, updateResult.getModifiedCount()),
        () -> assertEquals(0, users.size()));
  }
}
