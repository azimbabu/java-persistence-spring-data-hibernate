package com.azimbabu.javapersistence.event;

import com.azimbabu.javapersistence.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

@RepositoryEventHandler
@Service
public class UserRepositoryEventHandler {
  private static final Logger logger = LogManager.getLogger(UserRepositoryEventHandler.class);

  @HandleBeforeCreate
  public void handleUserBeforeCreate(User user) {
    char firstChar = user.getName().toUpperCase().charAt(0);
    if (firstChar >= 'A' || firstChar <= 'M') {
      logger.info("User {} is to be created, goes to the first part of the alphabet", user.getName());
    } else {
      logger.info("User {} is to be created, goes to the second part of the alphabet", user.getName());
    }
  }

  @HandleAfterCreate
  public void handleUserAfterCreate(User user) {
    logger.info("User {} has been created", user.getName());
  }

  @HandleBeforeDelete
  public void handleUserBeforeDelete(User user) {
    logger.info("User {} is to be deleted", user.getName());
  }

  @HandleAfterDelete
  public void handleUserAfterDelete(User user) {
    logger.info("User {} has been deleted", user.getName());
  }
}
