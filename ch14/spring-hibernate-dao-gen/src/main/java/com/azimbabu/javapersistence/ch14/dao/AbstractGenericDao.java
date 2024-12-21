package com.azimbabu.javapersistence.ch14.dao;

import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public abstract class AbstractGenericDao<T> implements GenericDao<T> {

  @Autowired
  protected SessionFactory sessionFactory;

  protected Class<T> clazz;

  public AbstractGenericDao(Class<T> clazz) {
    this.clazz = clazz;
  }

  @Override
  public T getById(long id) {
    return sessionFactory.getCurrentSession().createQuery("select e from " + clazz.getName() +
            " e where e.id = :id", clazz)
        .setParameter("id", id)
        .getSingleResult();
  }

  @Override
  public List<T> getAll() {
    return sessionFactory.getCurrentSession().createQuery("from " + clazz.getName(), clazz)
        .getResultList();
  }

  @Override
  public void insert(T entity) {
    sessionFactory.getCurrentSession().persist(entity);
  }

  @Override
  public void delete(T entity) {
    sessionFactory.getCurrentSession().remove(entity);
  }

  @Override
  public void update(long id, String propertyName, Object propertyValue) {
    sessionFactory.getCurrentSession().createQuery(
            "UPDATE " + clazz.getName() + " e set e." + propertyName +
                " = :propertyValue where e.id = :id")
        .setParameter("propertyValue", propertyValue)
        .setParameter("id", id)
        .executeUpdate();
    sessionFactory.getCurrentSession().refresh(sessionFactory.getCurrentSession().find(clazz, id));
  }

  @Override
  public List<T> findByProperty(String propertyName, Object propertyValue) {
    return sessionFactory.getCurrentSession()
        .createQuery("select e from " + clazz.getName() + " e where e." +
            propertyName + " = :propertyValue", clazz)
        .setParameter("propertyValue", propertyValue)
        .getResultList();
  }
}