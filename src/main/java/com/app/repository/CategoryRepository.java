package com.app.repository;

import com.app.model.Category;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Transactional
@EnableTransactionManagement
@Component
public class CategoryRepository implements BaseRepository<Category, UUID> {
    private Session session;

    @Autowired
    public CategoryRepository(LocalSessionFactoryBean localSessionFactoryBean) {
        this.session = Objects.requireNonNull(localSessionFactoryBean.getObject()).openSession();
    }

    @Override
    public List<Category> getAll() {
        Query categories = session.createQuery("from categories ");
        return categories.list();
    }

    @Override
    public Category getById(UUID id) {
        return session.get(Category.class, id);
    }

    @Override
    public boolean existsById(UUID id) {
        return session.get(Category.class, id) != null;
    }

    @Override
    public void save(Category elem) {
        try {
            session.clear();
//            Transaction transaction = session.beginTransaction();
            session.save(elem);
//            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Category elem) {
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
    public void saveOrUpdate(Category elem) {
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
    public Category deleteById(UUID id) {
        try {
            Transaction transaction = session.beginTransaction();
            Category delete = getById(id);
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
            Query query = session.createQuery("delete categories ");
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
