package com.azimbabu.javapersistence.ch14.configuration;

import com.azimbabu.javapersistence.ch14.Bid;
import com.azimbabu.javapersistence.ch14.DatabaseService;
import com.azimbabu.javapersistence.ch14.Item;
import com.azimbabu.javapersistence.ch14.dao.BidDaoImpl;
import com.azimbabu.javapersistence.ch14.dao.GenericDao;
import com.azimbabu.javapersistence.ch14.dao.ItemDaoImpl;
import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
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
  public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
    return new HibernateTransactionManager(sessionFactory);
  }

  @Bean
  public LocalSessionFactoryBean sessionFactory() {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(dataSource());
    sessionFactory.setPackagesToScan("com.azimbabu.javapersistence.ch14");
    sessionFactory.setHibernateProperties(hibernateProperties());
    return sessionFactory;
  }

  private Properties hibernateProperties() {
    Properties hibernateProperties = new Properties();
    hibernateProperties.setProperty(AvailableSettings.HBM2DDL_AUTO, "create");
    hibernateProperties.setProperty(AvailableSettings.SHOW_SQL, "true");
    hibernateProperties.setProperty(AvailableSettings.DIALECT, org.hibernate.dialect.MySQLDialect.class.getName());
    return null;
  }

  @Bean
  public GenericDao<Item> itemDao() {
    return new ItemDaoImpl();
  }

  @Bean
  public GenericDao<Bid> bidDao() {
    return new BidDaoImpl();
  }
}
