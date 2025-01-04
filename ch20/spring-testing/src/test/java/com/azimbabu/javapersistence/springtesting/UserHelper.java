package com.azimbabu.javapersistence.springtesting;

import com.azimbabu.javapersistence.springtesting.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserHelper {

  public static List<User> buildUsers(int numUsers) {
    List<User> users = new ArrayList<>();
    for (int i=0; i < numUsers; i++) {
      users.add(new User("User" + i));
    }
    return users;
  }
}
