package com.azimbabu.javapersistence.springtesting.listener;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class DatabaseOperationsListener implements TestExecutionListener {

  @Override
  public void beforeTestClass(TestContext testContext) throws Exception {
    System.out.println("beforeTestClass, transaction active = " + TransactionSynchronizationManager.isActualTransactionActive());
  }

  @Override
  public void afterTestClass(TestContext testContext) throws Exception {
    System.out.println("afterTestClass, transaction active = " + TransactionSynchronizationManager.isActualTransactionActive());
  }

  @Override
  public void beforeTestMethod(TestContext testContext) throws Exception {
    System.out.println("beforeTestMethod, transaction active = " + TransactionSynchronizationManager.isActualTransactionActive());
  }

  @Override
  public void afterTestMethod(TestContext testContext) throws Exception {
    System.out.println("afterTestMethod, transaction active = " + TransactionSynchronizationManager.isActualTransactionActive());
  }

  @Override
  public void beforeTestExecution(TestContext testContext) throws Exception {
    System.out.println("beforeTestExecution, transaction active = " + TransactionSynchronizationManager.isActualTransactionActive());
  }

  @Override
  public void afterTestExecution(TestContext testContext) throws Exception {
    System.out.println("afterTestExecution, transaction active = " + TransactionSynchronizationManager.isActualTransactionActive());
  }

  @Override
  public void prepareTestInstance(TestContext testContext) throws Exception {
    System.out.println("prepareTestInstance, transaction active = " + TransactionSynchronizationManager.isActualTransactionActive());
  }
}
