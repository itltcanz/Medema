package dev.itltcanz.medema.repositories;

import static dev.itltcanz.medema.util.NumberUtil.isNumber;

import com.google.inject.Inject;
import dev.itltcanz.medema.model.entity.Scan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.query.Page;

@SuppressWarnings("unused")
public class ScanRepository extends BaseRepository<Scan, Long> {

  @Inject
  public ScanRepository(EntityManagerFactory emf) {
    super(Scan.class, emf);
  }

  public Scan findScanByTime(LocalDateTime time) {
    try (EntityManager em = emf.createEntityManager()) {
      return em.createQuery("SELECT s FROM Scan s WHERE s.time = :time", Scan.class)
          .setParameter("time", time)
          .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public boolean existsByDetectorAndTime(String detectorId, LocalDateTime time) {
    String jpql = "SELECT COUNT(s) FROM Scan s WHERE s.detector.id = :detectorId AND s.time = :time";

    try (EntityManager em = emf.createEntityManager()) {
      Long count = em.createQuery(jpql, Long.class)
          .setParameter("detectorId", detectorId)
          .setParameter("time", time)
          .getSingleResult();

      return count > 0;
    }
  }

  public List<Scan> findScansWithFilter(LocalDateTime start, LocalDateTime end,
      String param, Page page) {
    String jpql = createTextQuery(start, end, param);
    return executeQuery(jpql, start, end, param, page);
  }

  private String createTextQuery(LocalDateTime start, LocalDateTime end, String param) {
    StringBuilder jpql = new StringBuilder("SELECT s FROM Scan s WHERE 1=1");
    if (start != null) {
      jpql.append(" AND s.time > :start");
    }
    if (end != null) {
      jpql.append(" AND s.time < :end");
    }

    if (param != null && !param.isBlank()) {
      jpql.append(" AND (");
      if (isNumber(param)) {
        jpql.append("s.metal = :paramNum)");
      } else {
        jpql.append("s.detector.id = :paramStr OR s.location.name = :paramStr)");
      }
    }
    jpql.append(" ORDER BY s.time DESC");
    return jpql.toString();
  }

  private List<Scan> executeQuery(String jpql, LocalDateTime start, LocalDateTime end, String param,
      Page page) {
    try (EntityManager em = emf.createEntityManager()) {
      TypedQuery<Scan> query = em.createQuery(jpql, Scan.class);

      if (start != null) {
        query.setParameter("start", start);
      }
      if (end != null) {
        query.setParameter("end", end);
      }
      if (param != null && !param.isBlank()) {
        if (isNumber(param)) {
          query.setParameter("paramNum", Byte.valueOf(param));
        } else {
          query.setParameter("paramStr", param);
        }
      }

      if (page != null) {
        query.setFirstResult(page.getNumber() * page.getSize());
        query.setMaxResults(page.getSize());
      }

      return query.getResultList();
    }
  }

}