package com.azimbabu.javapersistence;

import com.azimbabu.javapersistence.beans.CsvDataLoader;
import com.azimbabu.javapersistence.hibernateogm.model.Auction;
import com.azimbabu.javapersistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CsvDataLoader.class)
public class Application {

  @Autowired
  private Auction auction;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  ApplicationRunner configureRepository(UserRepository userRepository) {
    return args -> auction.getUsers().forEach(userRepository::save);
  }
}
