package com.azimbabu.javapersistence.springtesting.service;

import com.azimbabu.javapersistence.springtesting.model.User;
import com.azimbabu.javapersistence.springtesting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

//  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Transactional
  public void saveTransactionally(User user) {
    userRepository.save(user);
  }
}
