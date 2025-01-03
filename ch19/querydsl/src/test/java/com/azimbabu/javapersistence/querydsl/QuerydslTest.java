package com.azimbabu.javapersistence.querydsl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.azimbabu.javapersistence.querydsl.config.SpringDataConfiguration;
import com.azimbabu.javapersistence.querydsl.model.QBid;
import com.azimbabu.javapersistence.querydsl.model.QUser;
import com.azimbabu.javapersistence.querydsl.model.User;
import com.azimbabu.javapersistence.querydsl.repository.UserRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes = SpringDataConfiguration.class)
class QuerydslTest {

  @Autowired
  private UserRepository userRepository;

  private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ch19.querydsl");

  private EntityManager entityManager;

  private JPAQueryFactory queryFactory;

  @BeforeAll
  void beforeAll() {
    userRepository.saveAll(UserGenerator.generateUsers());
  }

  @AfterAll
  void afterAll() {
    userRepository.deleteAll();
  }

  @BeforeEach
  void setUp() {
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    queryFactory = new JPAQueryFactory(entityManager);
  }

  @AfterEach
  void tearDown() {
    entityManager.getTransaction().commit();
    entityManager.close();
  }

  @Test
  void testFindByUsername() {
    User fetchedUser = queryFactory.selectFrom(QUser.user)
        .where(QUser.user.username.eq("john"))
        .fetchOne();

    assertAll(() -> assertNotNull(fetchedUser),
        () -> assertEquals("john", fetchedUser.getUsername()),
        () -> assertEquals("John", fetchedUser.getFirstName()),
        () -> assertEquals("Smith", fetchedUser.getLastName()),
        () -> assertEquals(2, fetchedUser.getBids().size()));
  }

  @Test
  void testByLevelAndActive() {
    List<User> users = queryFactory.selectFrom(QUser.user)
        .where(QUser.user.level.eq(3).and(QUser.user.active.eq(true)))
        .fetch();
    assertEquals(1, users.size());
  }

  @Test
  void testOrderByUsername() {
    List<User> users = queryFactory.selectFrom(QUser.user)
        .orderBy(QUser.user.username.asc())
        .fetch();
    assertAll(() -> assertEquals(10, users.size()),
        () -> assertEquals("beth", users.get(0).getUsername()),
        () -> assertEquals("burk", users.get(1).getUsername()),
        () -> assertEquals("mike", users.get(8).getUsername()),
        () -> assertEquals("stephanie", users.get(9).getUsername()));
  }

  @Test
  void testGroupByBidAmount() {
    NumberPath<Long> count = Expressions.numberPath(Long.class, "bids");
    List<Tuple> userBidsGroupByAmount = queryFactory.select(QBid.bid.amount, QBid.bid.id.count().as(count))
        .from(QBid.bid)
        .groupBy(QBid.bid.amount)
        .orderBy(count.desc())
        .fetch();
    assertAll(() -> assertEquals(new BigDecimal("120.00"), userBidsGroupByAmount.get(0).get(QBid.bid.amount)),
        () -> assertEquals(2, userBidsGroupByAmount.get(0).get(count)));
  }

  @Test
  void testAggregateBidAmount() {
    BigDecimal maxAmount = queryFactory.from(QBid.bid).select(QBid.bid.amount.max()).fetchOne();
    assertEquals(new BigDecimal("120.00"), maxAmount);

    BigDecimal minAmount = queryFactory.from(QBid.bid).select(QBid.bid.amount.min()).fetchOne();
    assertEquals(new BigDecimal("100.00"), minAmount);

    Double avgAmount = queryFactory.from(QBid.bid).select(QBid.bid.amount.avg()).fetchOne();
    assertEquals(112.6, avgAmount);
  }

  @Test
  void testSubquery() {
    JPQLQuery<Long> subQuery1 = JPAExpressions.select(QBid.bid.user.id)
        .from(QBid.bid)
        .where(QBid.bid.amount.eq(new BigDecimal("120.00")));

    List<User> users = queryFactory.selectFrom(QUser.user)
        .where(QUser.user.id.in(subQuery1))
        .fetch();

    JPQLQuery<Long> subquery2 = JPAExpressions.select(QBid.bid.user.id)
        .from(QBid.bid)
        .where(QBid.bid.amount.eq(new BigDecimal("105.00")));
    List<User> otherUsers = queryFactory.selectFrom(QUser.user)
        .where(QUser.user.id.in(subquery2))
        .fetch();

    assertAll(() -> assertEquals(2, users.size()),
        () -> assertEquals(1, otherUsers.size()),
        () -> assertEquals("burk", otherUsers.get(0).getUsername()));
  }

  @Test
  void testJoin() {
    List<User> users = queryFactory.selectFrom(QUser.user)
        .innerJoin(QUser.user.bids, QBid.bid)
        .on(QBid.bid.amount.eq(new BigDecimal("120.00")))
        .fetch();

    List<User> otherUsers = queryFactory.selectFrom(QUser.user)
        .innerJoin(QUser.user.bids, QBid.bid)
        .on(QBid.bid.amount.eq(new BigDecimal("105.00")))
        .fetch();

    assertAll(() -> assertEquals(2, users.size()),
        () -> assertEquals(1, otherUsers.size()),
        () -> assertEquals("burk", otherUsers.get(0).getUsername()));
  }

  @Test
  void testUpdate() {
    long updateCount = queryFactory.update(QUser.user)
        .where(QUser.user.username.eq("john"))
        .set(QUser.user.email, "john@someotherdomain.com")
        .execute();
    assertEquals(1L, updateCount);

    entityManager.getTransaction().commit();

    entityManager.getTransaction().begin();

    String updatedEmail = queryFactory.select(QUser.user.email)
        .from(QUser.user)
        .where(QUser.user.username.eq("john"))
        .fetchOne();
    assertEquals("john@someotherdomain.com", updatedEmail);
  }

  @Test
  void testDelete() {
    // http://querydsl.com/static/querydsl/latest/reference/html/
    // Section 2.1.11 on DELETE queries using JPA:
    // "DML clauses in JPA don't take JPA level cascade rules into account
    // and don't provide fine-grained second level cache interaction."
    // Therefore, the cascade attribute of the bids @OneToMany annotation
    // in class User is ignored, so it becomes necessary to select user
    // Burk and manually delete his bids before deleting it through a
    // QueryDSL delete query.
    User burk = (User) queryFactory.from(QUser.user)
        .where(QUser.user.username.eq("burk"))
        .fetchOne();
    assertNotNull(burk);

    long deletedBidsCount = queryFactory.delete(QBid.bid)
        .where(QBid.bid.user.eq(burk))
        .execute();
    assertEquals(1L, deletedBidsCount);

    // End of the bug workaround

    long deletedUserCount = queryFactory.delete(QUser.user)
        .where(QUser.user.username.eq("burk"))
        .execute();
    assertEquals(1L, deletedUserCount);

    // If user Burk were to be deleted through the standard
    // UserRepository delete() method, the @OneToMany cascade
    // attribute would be properly taken into account and no
    // manual handling of his bids would be needed.
    //userRepository.delete(burk);

    entityManager.getTransaction().commit();

    entityManager.getTransaction().begin();

    User fetchedUser = (User) queryFactory.from(QUser.user)
        .where(QUser.user.username.eq("burk"))
        .fetchOne();
    assertNull(fetchedUser);
  }
}
