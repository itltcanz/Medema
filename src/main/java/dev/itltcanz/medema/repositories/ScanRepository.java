package dev.itltcanz.medema.repositories;

import dev.itltcanz.medema.entity.Scan;
import dev.itltcanz.medema.logic.Page;
import dev.itltcanz.medema.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("unused")
public class ScanRepository {
    public static void save(Scan scan) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(scan);
        session.getTransaction().commit();
        session.close();
    }

    public static Scan findScanByTime(LocalDateTime time) {
        Session session = HibernateUtil.getSession();
        Query<Scan> query = session.createQuery("FROM Scan WHERE time = :time", Scan.class);
        query.setParameter("time", time);
        Scan scan = query.uniqueResult();
        session.close();
        return scan;
    }

    public static Scan findScanByMetalAndTime(byte metal, LocalDateTime time) {
        Session session = HibernateUtil.getSession();
        Query<Scan> query = session.createQuery("FROM Scan WHERE metal = :metal AND time = :time", Scan.class);
        query.setParameter("metal", metal);
        query.setParameter("time", time);
        Scan scan = query.uniqueResult();
        session.close();
        return scan;
    }

    public static List<Scan> findScansByTimeBetweenOrderByTimeDesc(LocalDateTime start, LocalDateTime end, Page page) {
        Session session = HibernateUtil.getSession();
        Query<Scan> query = session.createQuery("FROM Scan WHERE time BETWEEN :start AND :end ORDER BY time DESC", Scan.class);
        query.setParameter("start", start);
        query.setParameter("end", end);
        query.setFirstResult(page.getNumber() * page.getSize());
        query.setMaxResults(page.getSize());
        List<Scan> scans = query.list();
        session.close();
        return scans;
    }

    public static List<Scan> findScansByTimeBetweenOrderByTimeDesc(LocalDateTime start, LocalDateTime end) {
        Session session = HibernateUtil.getSession();
        Query<Scan> query = session.createQuery("FROM Scan WHERE time BETWEEN :start AND :end ORDER BY time DESC", Scan.class);
        query.setParameter("start", start);
        query.setParameter("end", end);
        List<Scan> scans = query.list();
        session.close();
        return scans;
    }

    public static List<Scan> findAllByOrderByDateTimeDesc(Page page) {
        Session session = HibernateUtil.getSession();
        Query<Scan> query = session.createQuery("FROM Scan ORDER BY time DESC", Scan.class);
        query.setFirstResult(page.getNumber() * page.getSize());
        query.setMaxResults(page.getSize());
        List<Scan> scans = query.list();
        session.close();
        return scans;
    }

    public static List<Scan> findScansByModuleIdOrModuleLocation(String id, String location) {
        Session session = HibernateUtil.getSession();
        Query<Scan> query = session.createQuery("FROM Scan WHERE module.id = :id OR module.location = :location", Scan.class);
        query.setParameter("id", id);
        query.setParameter("location", location);
        List<Scan> scans = query.list();
        session.close();
        return scans;
    }

    public static List<Scan> findScansByModuleIdOrModuleLocationOrMetal(String id, String location, int metal) {
        Session session = HibernateUtil.getSession();
        Query<Scan> query = session.createQuery("FROM Scan WHERE module.id = :id OR module.location = :location OR metal = :metal", Scan.class);
        query.setParameter("id", id);
        query.setParameter("location", location);
        query.setParameter("metal", metal);
        List<Scan> scans = query.list();
        session.close();
        return scans;
    }

    public static List<Scan> findScansByModuleIdOrModuleLocationAndTimeBetween(String id, String location, LocalDateTime start, LocalDateTime end) {
        Session session = HibernateUtil.getSession();
        Query<Scan> query = session.createQuery("FROM Scan WHERE (module.id = :id OR module.location = :location) AND time BETWEEN :fromDateTime AND :toDateTime", Scan.class);
        query.setParameter("id", id);
        query.setParameter("location", location);
        query.setParameter("start", start);
        query.setParameter("end", end);
        List<Scan> scans = query.list();
        session.close();
        return scans;
    }

    public static List<Scan> findScansByModuleIdOrModuleLocationOrMetalAndTimeBetweenOrderByTimeDesc(String id, String location, int metal, LocalDateTime start, LocalDateTime end) {
        Session session = HibernateUtil.getSession();
        Query<Scan> query = session.createQuery("FROM Scan WHERE (module.id = :id OR module.location = :location OR metal = :metal) AND time BETWEEN :start AND :end ORDER BY time DESC", Scan.class);
        query.setParameter("id", id);
        query.setParameter("location", location);
        query.setParameter("metal", metal);
        query.setParameter("start", start);
        query.setParameter("end", end);
        List<Scan> scans = query.list();
        session.close();
        return scans;
    }
}
