package com.app.repository;

import com.app.model.Category;
import com.app.model.Course;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Transactional
@EnableTransactionManagement
public class CourseRepository<T, I> implements BaseRepository<Course, UUID> {
    private Session session;

    @Autowired
    public CourseRepository(LocalSessionFactoryBean localSessionFactoryBean) {
        this.session = Objects.requireNonNull(localSessionFactoryBean.getObject()).openSession();
    }

    @Override
    public List<Course> getAll() {
        Query courses = session.createQuery("from courses ");
        return courses.list();
    }

    @Override
    public Course getById(UUID id) {
        return session.get(Course.class, id);
    }

    @Override
    public boolean existsById(UUID id) {
        return session.get(Course.class, id) != null;
    }

    @Override
    public void save(Course elem) {
       session.save(elem);
    }

    @Override
    public void update(Course elem) {
       session.update(elem);
    }

    @Override
    public void saveOrUpdate(Course elem) {
      existsById(elem.getId());
    }

    @Override
    public Course deleteById(UUID id) {
        Course course = getById(id);
        session.delete(course);
        return course;
    }

    @Override
    public void clear() {
      session.createQuery("delete  courses ");
    }
}
