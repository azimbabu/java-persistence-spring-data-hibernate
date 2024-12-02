package com.azimbabu.javapersistence.ch13.filtering.interceptor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.type.Type;

public class AuditLogInterceptor extends EmptyInterceptor {

  private Session currentSession;

  private Long currentUserId;

  private Set<Auditable> inserts = new HashSet<>();

  private Set<Auditable> updates = new HashSet<>();

  public void setCurrentSession(Session currentSession) {
    this.currentSession = currentSession;
  }

  public void setCurrentUserId(Long currentUserId) {
    this.currentUserId = currentUserId;
  }

  /**
   * This method is called when an entity instance is made persistent.
   *
   * @param entity The entity instance whose state is being inserted
   * @param id The identifier of the entity
   * @param state The state of the entity which will be inserted
   * @param propertyNames The names of the entity properties.
   * @param types The types of the entity properties
   *
   * @return
   * @throws CallbackException
   */
  @Override
  public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames,
      Type[] types) throws CallbackException {
    if (entity instanceof Auditable auditable) {
      inserts.add(auditable);
    }
    return false; // We didn't modify the state
  }

  /**
   * This method is called when an entity instance is detected as dirty
   * during flushing of the persistence context.
   *
   * @param entity The entity instance detected as being dirty and being flushed
   * @param id The identifier of the entity
   * @param currentState The entity's current state
   * @param previousState The entity's previous (load time) state.
   * @param propertyNames The names of the entity properties
   * @param types The types of the entity properties
   *
   * @return
   * @throws CallbackException
   */
  @Override
  public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState,
      Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException {
    if (entity instanceof Auditable auditable) {
      updates.add(auditable);
    }
    return false; // We didn't modify the currentState
  }

  /**
   * This method is called after flushing of the persistence context is complete.
   * Here, you write the audit log records for all insertions and updates you
   * collected earlier.
   *
   * @param entities The entities that were flushed.
   *
   * @throws CallbackException
   */
  @Override
  public void postFlush(Iterator<Object> entities) throws CallbackException {
    /*
       You are not allowed to access the original persistence context, the
       <code>Session</code> that is currently executing this interceptor.
       The <code>Session</code> is in a fragile state during interceptor calls.
       Hibernate allows you to create a new <code>Session</code> that
       inherits some information from the original <code>Session</code> with
       the <code>sessionWithOptions()</code> method. Here the new temporary
       <code>Session</code> works with the same transaction and database
       connection as the original <code>Session</code>.
    */
    try (Session tempSession = currentSession.sessionWithOptions()
        .connection()
        .openSession()) {
      /*
       You store a new <code>AuditLogRecord</code> for each insertion and
       update using the temporary <code>Session</code>.
     */
      for (Auditable entity : inserts) {
        tempSession.persist(new AuditLogRecord("insert", entity, currentUserId));
      }
      for (Auditable entity : updates) {
        tempSession.persist(new AuditLogRecord("update", entity, currentUserId));
      }

    /*
       You flush and close the temporary <code>Session</code>
       independently from the original <code>Session</code>.
     */
      tempSession.flush();
    } finally {
      inserts.clear();
      updates.clear();
    }
  }
}
