package com.azimbabu.javapersistence.event;

import com.azimbabu.javapersistence.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Service;

@Service
public class RepositoryEventListener extends AbstractRepositoryEventListener<User> {

  private static final Logger logger = LogManager.getLogger(RepositoryEventListener.class);

  @Override
  public void onBeforeCreate(User user) {
    logger.info("Before create user = {}", user);
  }

  @Override
  public void onAfterCreate(User user) {
    logger.info("After create user = {}", user);
  }

  @Override
  public void onBeforeSave(User user) {
    logger.info("Before save user = {}", user);
  }

  @Override
  public void onAfterSave(User user) {
    logger.info("After save user = {}", user);
  }

  @Override
  public void onBeforeDelete(User user) {
    logger.info("Before delete user = {}", user);
  }

  @Override
  public void onAfterDelete(User user) {
    logger.info("After delete user = {}", user);
  }
}
