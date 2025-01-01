package com.azimbabu.javapersistence.template;

import com.azimbabu.javapersistence.UserGenerator;
import com.azimbabu.javapersistence.hibernateogm.model.Address;
import com.azimbabu.javapersistence.hibernateogm.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
abstract class SpringDataMongoDBApplicationTests {

  @Autowired
  MongoTemplate mongoTemplate;

  @BeforeAll
  void beforeAll() {
    mongoTemplate.insert(UserGenerator.address);
    mongoTemplate.insert(UserGenerator.generateUsers(), User.class);
  }

  @AfterAll
  void afterAll() {
    mongoTemplate.remove(new Query(), User.class);
    mongoTemplate.remove(new Query(), Address.class);
  }
}
