package com.azimbabu.javapersistence.ch05.mapping.configuration;

import com.azimbabu.javapersistence.ch05.mapping.CENamingStrategy;
import jakarta.persistence.EntityManagerFactory;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@EnableJpaRepositories("com.azimbabu.javapersistence.ch05.mapping.repository")
public class SpringDataConfiguration {

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(com.mysql.cj.jdbc.Driver.class.getName());
    dataSource.setUrl("jdbc:mysql://localhost:3306/CH05_MAPPING?serverTimezone=UTC");
    dataSource.setUsername("root");
    dataSource.setPassword("abcd1234");
    return dataSource;
  }

  @Bean
  public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setDatabase(Database.MYSQL);
    jpaVendorAdapter.setShowSql(true);
    return jpaVendorAdapter;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setDataSource(dataSource());

    Properties properties = new Properties();
    properties.put("hibernate.hbm2ddl.auto", "create");
    properties.put("hibernate.physical_naming_strategy", CENamingStrategy.class.getName());

    entityManagerFactoryBean.setJpaProperties(properties);
    entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
    entityManagerFactoryBean.setPackagesToScan("com.azimbabu.javapersistence.ch05.mapping");
    return entityManagerFactoryBean;
  }
}
