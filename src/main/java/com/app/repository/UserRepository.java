package com.app.repository;

import com.app.model.Attachment;
import com.app.model.Role;
import com.app.model.User;
import org.apache.commons.validator.routines.EmailValidator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
            Attachment attachment = elem.getAttachment();
            session.save(attachment);
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

    public User getByEmail(String email) {
        try {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("from users where email = '" + email + "' ");
            Optional first = query.list().stream().findFirst();
            transaction.commit();
            return (User) first.orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean existsByEmail(String email) {
        return getByEmail(email) != null;
    }

    public boolean isValidEmail(String email) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        return emailValidator.isValid(email);
    }

    public boolean isValidPassword(String password) {
//        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,50}$";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(password);
//        return matcher.matches();
        return password.length() > 5;
    }

    public Role getRole(String role) {
        try {
            Query query = session.createQuery("from roles where name = '" + role + "' ");
            Optional first = query.list().stream().findFirst();
            return (Role) first.orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public void saveRole(Role elem) {
        try {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(elem);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
