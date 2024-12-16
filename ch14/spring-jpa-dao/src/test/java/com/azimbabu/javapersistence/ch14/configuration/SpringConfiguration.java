package com.azimbabu.javapersistence.ch14.configuration;

import com.azimbabu.javapersistence.ch14.DatabaseService;
import com.azimbabu.javapersistence.ch14.Item;
import com.azimbabu.javapersistence.ch14.dao.BidDao;
import com.azimbabu.javapersistence.ch14.dao.BidDaoImpl;
import com.azimbabu.javapersistence.ch14.dao.ItemDao;
import com.azimbabu.javapersistence.ch14.dao.ItemDaoImpl;
import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
public class SpringConfiguration {

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(com.mysql.cj.jdbc.Driver.class.getName());
    dataSource.setUrl("jdbc:mysql://localhost:3306/CH14_SPRING_HIBERNATE?serverTimezone=UTC");
    dataSource.setUsername("root");
    dataSource.setPassword("abcd1234");
    return dataSource;
  }

  @Bean
  public DatabaseService databaseService() {
    return new DatabaseService();
  }

  @Bean
  public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setPersistenceUnitName("ch14.spring-jpa-dao");
    entityManagerFactoryBean.setDataSource(dataSource());
    entityManagerFactoryBean.setPackagesToScan("com.azimbabu.javapersistence.ch14");
    return entityManagerFactoryBean;
  }

  @Bean
  public ItemDao itemDao() {
    return new ItemDaoImpl();
  }

  @Bean
  public BidDao bidDao() {
    return new BidDaoImpl();
  }
}
