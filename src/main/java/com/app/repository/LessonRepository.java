package com.app.repository;

import com.app.model.Lesson;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Transactional
@EnableTransactionManagement


public class LessonRepository implements BaseRepository<Lesson, UUID> {
    private Session session;

    @Autowired
    public LessonRepository(LocalSessionFactoryBean localSessionFactoryBean) {
        this.session = Objects.requireNonNull(localSessionFactoryBean.getObject()).openSession();
    }

    @Override
    public List<Lesson> getAll() {
        Query lessons = session.createQuery("from lessons");
        return lessons.list();
    }

    @Override
    public Lesson getById(UUID id) {
        Query query = session.createQuery("from lessons where id = " + id);
        if (query.list()!=null && query.list().size()>0) {
            return (Lesson) query.list().get(0);
        }
        return null;
    }

    @Override
    public boolean existsById(UUID id) {
        Lesson lesson = session.get(Lesson.class, id);
        if (lesson != null) {
            return true;
        }
        return false;
    }

    @Override
    public void save(Lesson elem) {
        session.save(elem);
    }

    @Override
    public void update(Lesson elem) {
        session.update(elem);
    }

    @Override
    public void saveOrUpdate(Lesson elem) {
        if (existsById(elem.getId())) {
            session.update(elem);
        }else {
            session.save(elem);
        }
    }

    @Override
    public Lesson deleteById(UUID id) {
        Lesson lesson = getById(id);
        session.delete(lesson);
        return lesson;
    }

    @Override
    public void clear() {
        session.createQuery("delete lessons");
    }
}
