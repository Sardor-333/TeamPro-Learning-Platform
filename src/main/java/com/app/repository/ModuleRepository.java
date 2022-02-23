package com.app.repository;

import com.app.model.Module;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class ModuleRepository implements BaseRepository<Module, UUID> {
    private Session session;

    @Autowired
    public ModuleRepository(LocalSessionFactoryBean localSessionFactoryBean) {
        this.session = Objects.requireNonNull(localSessionFactoryBean.getObject()).openSession();
    }

    public List<Module> getByCourseId(UUID id) {
        Query modules = session.createQuery("from modules where course.id =" + id);
        return modules.list();
    }

    @Override
    public List<Module> getAll() {
        Query modules = session.createQuery("from modules");
        return modules.list();
    }

    @Override
    public Module getById(UUID id) {
        return session.get(Module.class, id);
    }

    @Override
    public boolean existsById(UUID id) {
        return session.get(Module.class, id) != null;
    }

    @Override
    public void save(Module elem) {
        try {
            session.clear();
            Transaction transaction = session.beginTransaction();
            session.save(elem);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Module elem) {
        try {
            session.clear();
            Transaction transaction = session.beginTransaction();
            session.update(elem);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveOrUpdate(Module elem) {
        try {
            session.clear();
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(elem);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Module deleteById(UUID id) {
        try {
            Transaction transaction = session.beginTransaction();
            Module delete = getById(id);
            session.delete(delete);
            transaction.commit();
            return delete;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void clear() {
        try {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("delete modules");
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
