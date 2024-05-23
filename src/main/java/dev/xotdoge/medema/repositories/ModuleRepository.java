package dev.xotdoge.medema.repositories;

import dev.xotdoge.medema.entity.Module;
import dev.xotdoge.medema.config.HibernateUtil;
import org.hibernate.Session;

@SuppressWarnings("unused")
public class ModuleRepository {

    public static void save(Module module) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(module);
        session.getTransaction().commit();
        session.close();
    }
    public static Module findModuleById(String id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        Module module = session.get(Module.class, id);
        session.getTransaction().commit();
        session.close();
        return module;
    }
}
