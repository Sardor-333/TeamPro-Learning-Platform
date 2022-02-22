package com.app.repository;

import com.app.model.User;
import org.apache.commons.validator.routines.EmailValidator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class UserRepository implements BaseRepository<User, UUID> {
    private Session session;

    public UserRepository(LocalSessionFactoryBean localSessionFactoryBean) {
        this.session = Objects.requireNonNull(localSessionFactoryBean.getObject()).openSession();
    }

    @Override
    public List<User> getAll() {
        Query users = session.createQuery("from users");
        return users.list();
    }

    @Override
    public User getById(UUID id) {
        return session.get(User.class, id);
    }

    @Override
    public boolean existsById(UUID id) {
        return getById(id) != null;
    }

    @Override
    public void save(User elem) {
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
    public void update(User elem) {
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
    public void saveOrUpdate(User elem) {
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
    public User deleteById(UUID id) {
        try {
            Transaction transaction = session.beginTransaction();
            User user = getById(id);
            session.delete(user);
            transaction.commit();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void clear() {
        try {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("delete users ");
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean existsByEmail(String email) {
        Query query = session.createQuery("from users where email = '" + email + "'");
        return query.list() != null;
    }

    public boolean isValidEmail(String email) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        return emailValidator.isValid(email);
    }
}
