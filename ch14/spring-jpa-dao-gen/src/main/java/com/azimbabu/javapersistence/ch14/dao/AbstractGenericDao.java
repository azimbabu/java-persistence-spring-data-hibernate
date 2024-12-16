package com.azimbabu.javapersistence.ch14.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public abstract class AbstractGenericDao<T> implements GenericDao<T> {

  @PersistenceContext(type = PersistenceContextType.EXTENDED)
  protected EntityManager em;

  protected Class<T> clazz;

  public AbstractGenericDao(Class<T> clazz) {
    this.clazz = clazz;
  }

  @Override
  public T getById(long id) {
    return em.createQuery("select e from " + clazz.getName() + " e where e.id = :id", clazz)
        .setParameter("id", id)
        .getSingleResult();
  }

  @Override
  public List<T> getAll() {
    return em.createQuery("from " + clazz.getName(), clazz)
        .getResultList();
  }

  @Override
  public void insert(T entity) {
    em.persist(entity);
  }

  @Override
  public void delete(T entity) {
    em.remove(entity);
  }

  @Override
  public void update(long id, String propertyName, Object propertyValue) {
    em.createQuery("UPDATE " + clazz.getName() + " e set e." + propertyName +
        " = :propertyValue where e.id = :id")
        .setParameter("propertyValue", propertyValue)
        .setParameter("id", id)
        .executeUpdate();
    em.refresh(em.find(clazz, id));
  }

  @Override
  public List<T> findByProperty(String propertyName, Object propertyValue) {
    return em.createQuery("select e from " + clazz.getName() + " e where e." +
        propertyName + " = :propertyValue", clazz)
        .setParameter("propertyValue", propertyValue)
        .getResultList();
  }
}
