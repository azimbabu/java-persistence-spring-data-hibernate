package com.azimbabu.javapersistence.ch05.mapping;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class CENamingStrategy extends PhysicalNamingStrategyStandardImpl {

  @Override
  public Identifier toPhysicalTypeName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
    return new Identifier("CE_" + logicalName.getText(), logicalName.isQuoted());
  }
}
