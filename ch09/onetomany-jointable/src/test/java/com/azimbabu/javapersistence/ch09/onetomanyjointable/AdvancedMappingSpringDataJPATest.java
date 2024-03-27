package com.azimbabu.javapersistence.ch09.onetomanyjointable;

import com.azimbabu.javapersistence.ch09.onetomanyjointable.configuration.SpringDataConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDataConfiguration.class})
public class AdvancedMappingSpringDataJPATest {

  @Autowired
  private TestService testService;

  @Test
  void testStoreLoadEntities() {
    testService.storeLoadEntities();
  }
}
