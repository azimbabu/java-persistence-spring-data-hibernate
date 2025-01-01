package com.azimbabu.javapersistence;

import com.azimbabu.javapersistence.beans.CsvDataLoader;
import com.azimbabu.javapersistence.hibernateogm.model.Address;
import com.azimbabu.javapersistence.hibernateogm.model.Auction;
import com.azimbabu.javapersistence.repository.UserRepository;
import java.util.concurrent.atomic.AtomicInteger;
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
    AtomicInteger number = new AtomicInteger(1);
    return args -> auction.getUsers().forEach(user -> {
      user.setAddress(generateAddress(String.valueOf(number.getAndIncrement())));
      userRepository.save(user);
    });
  }

  private Address generateAddress(String number) {
    return new Address(number + " Flowers Streets", "12345", "Boston", "MA");
  }
}
