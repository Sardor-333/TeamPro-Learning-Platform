package com.app.repository;

import com.app.model.Lesson;
import com.app.model.LessonReview;
import com.app.model.Video;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Component
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
        Query query = session.createQuery("from lessons where id = '"+id+"'");
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

    public List<Lesson> getLessonsByModuleId(UUID id){
        Query query = session.createQuery("from lessons where module.id = '" + id + "'");
        return query.list();
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

    public void reviewSave(LessonReview comment) {
        Transaction transaction = session.beginTransaction();
        session.save(comment);
        transaction.commit();
    }

    public List<LessonReview> getComments(UUID lessonId){
        Query query = session.createQuery("from lesson_reviews where lesson.id = '" + lessonId + "'");
        return query.list();
    }

    public LessonReview getComment(UUID id) {
        return session.get(LessonReview.class, id);

    }

    public void deleteComment(UUID commentId) {
        Transaction transaction = session.beginTransaction();
        LessonReview comment = getComment(commentId);
        session.delete(comment);
        transaction.commit();
    }

    public void saveVideo(Video video) {
        Transaction transaction = session.beginTransaction();
        session.save(video);
        transaction.commit();
    }

    public List<Video> getVideoByLessonId(UUID id) {
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from videos where lesson.id = '" + id + "'");
        transaction.commit();
        return query.list();
    }

    public Video getVideoByVId(UUID videoId){
        return session.get(Video.class,videoId);
    }

    public UUID deleteVideo(UUID videoId) {
        Transaction transaction = session.beginTransaction();
        Video video = getVideoByVId(videoId);
        session.delete(video);
        transaction.commit();
        return video.getLesson().getId();
    }
}
