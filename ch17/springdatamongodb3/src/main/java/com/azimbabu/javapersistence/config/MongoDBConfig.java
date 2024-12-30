package com.azimbabu.javapersistence.config;

import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

public class MongoDBConfig extends AbstractMongoClientConfiguration {

  @Override
  protected String getDatabaseName() {
    return "test";
  }
}
