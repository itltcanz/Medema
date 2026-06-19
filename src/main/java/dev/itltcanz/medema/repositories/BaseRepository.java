package dev.itltcanz.medema.repositories;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("java:S119")
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public abstract class BaseRepository<T, ID> {

  protected final Class<T> entityClass;
  protected final EntityManagerFactory emf;

  protected EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public List<T> findAll() {
    try (EntityManager em = getEntityManager()) {
      String jpql = "SELECT e FROM %s e".formatted(entityClass.getSimpleName());
      return em.createQuery(jpql, entityClass).getResultList();
    }
  }

  public void create(T entity) {
    try (EntityManager em = getEntityManager()) {
      EntityTransaction tx = em.getTransaction();
      try {
        tx.begin();
        em.persist(entity);
        tx.commit();
      } catch (Exception e) {
        if (tx.isActive()) {
          tx.rollback();
        }
        throw e;
      }
    }
  }

  public T update(T entity) {
    try (EntityManager em = getEntityManager()) {
      EntityTransaction tx = em.getTransaction();
      try {
        tx.begin();
        T mergedEntity = em.merge(entity);
        tx.commit();
        return mergedEntity;
      } catch (Exception e) {
        if (tx.isActive()) {
          tx.rollback();
        }
        throw e;
      }
    }
  }


  public void delete(ID id) {
    try (EntityManager em = getEntityManager()) {
      EntityTransaction tx = em.getTransaction();
      try {
        tx.begin();
        T entity = em.find(entityClass, id);
        if (entity != null) {
          em.remove(entity);
        }
        tx.commit();
      } catch (Exception e) {
        if (tx.isActive()) {
          tx.rollback();
        }
        throw e;
      }
    }
  }

  public boolean exists(ID id) {
    try (EntityManager em = getEntityManager()) {
      String jpql = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e WHERE e.id = :id";
      Long count = em.createQuery(jpql, Long.class)
          .setParameter("id", id)
          .getSingleResult();
      return count > 0;
    }
  }
}
