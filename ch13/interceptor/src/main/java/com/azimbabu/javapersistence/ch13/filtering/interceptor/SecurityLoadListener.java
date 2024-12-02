package com.azimbabu.javapersistence.ch13.filtering.interceptor;

import org.hibernate.HibernateException;
import org.hibernate.event.internal.DefaultLoadEventListener;
import org.hibernate.event.spi.LoadEvent;

public class SecurityLoadListener extends DefaultLoadEventListener {

  @Override
  public void onLoad(LoadEvent event, LoadType loadType) throws HibernateException {
    boolean authorized = MySecurity.isAuthorized(event.getEntityClassName(), event.getEntityId());
    if (!authorized) {
      throw new MySecurityException("Unauthorized access");
    }
    System.out.println("Loading entity: " + event.getEntityClassName() + " with id: " + event.getEntityId());
    super.onLoad(event, loadType);
  }

  public static class MySecurity {
    static boolean isAuthorized(String entityName, Object entityId) {
      return true;
    }
  }

  public static class MySecurityException extends RuntimeException {

    public MySecurityException(String message) {
      super(message);
    }
  }
}
