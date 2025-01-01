package com.azimbabu.javapersistence.hibernateogm.model;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Document
@CompoundIndexes({
    @CompoundIndex(name = "username_email", def = "{'username': 1, 'email': 1}"),
    @CompoundIndex(name = "lastName_firstName", def = "{'lastName': 1, 'firstName': 1}")
})
public class User {
  @Id
  @Getter
  private String id;

  @Getter
  @Setter
  @Indexed(direction = IndexDirection.ASCENDING)
  private String username;

  @Getter
  @Setter
  private String firstName;

  @Getter
  @Setter
  private String lastName;

  @Getter
  @Setter
  private LocalDate registrationDate;

  @Getter
  @Setter
  private String email;

  @Getter
  @Setter
  private int level;

  @Getter
  @Setter
  private boolean active;

  @Getter
  @Setter
  @Transient
  private String password;

  @Getter
  @Setter
  private String ip;

  public User(String username, String firstName, String lastName) {
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
  }

//  @PersistenceConstructor
  @PersistenceCreator
  public User(String username, String firstName, String lastName, @Value("#root.ip ?: '192.168.1.100'") String ip) {
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.ip = ip;
  }
}
