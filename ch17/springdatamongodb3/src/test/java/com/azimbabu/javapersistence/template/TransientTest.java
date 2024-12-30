package com.azimbabu.javapersistence.template;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.azimbabu.javapersistence.model.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Query;

class TransientTest extends SpringDataMongoDBApplicationTests {

  @Test
  void testTransientField() {
    List<User> users = mongoTemplate.find(new Query(), User.class);
    assertAll(() -> assertEquals(10, users.size()),
        () -> users.forEach(user -> {
          assertNotNull(user.getId());
          assertNotNull(user.getUsername());
          assertNull(user.getPassword());
        }));
  }
}
