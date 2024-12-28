package com.azimbabu.javapersistence.beans;

import com.azimbabu.javapersistence.model.Auction;
import com.azimbabu.javapersistence.model.User;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.springframework.context.annotation.Bean;

public class CsvDataLoader {
  @Bean
  public Auction buildAuctionFromCsv() throws IOException {
    Auction auction = new Auction("1234", 20);
    try (BufferedReader reader = new BufferedReader(new FileReader(CsvDataLoader.class.getResource("/user_information.csv").getFile()))) {
      String line;
      do {
        line = reader.readLine();
        if (line != null) {
          User user = new User(line);
          user.setRegistered(false);
          auction.addUser(user);
        }
      } while (line != null);
    }
    return auction;
  }
}
