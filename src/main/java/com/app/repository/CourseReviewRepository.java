package com.app.repository;

import com.app.model.CourseReview;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CourseReviewRepository implements BaseRepository<CourseReview, UUID> {
    private Session session;

    @Autowired
    public CourseReviewRepository(LocalSessionFactoryBean localSessionFactoryBean) {
        this.session = localSessionFactoryBean.getObject().openSession();
    }

    @Override
    public List<CourseReview> getAll() {
        return session.createQuery("from course_reviews ").list();
    }

    @Override
    public CourseReview getById(UUID id) {
        return session.get(CourseReview.class, id);
    }

    @Override
    public boolean existsById(UUID id) {
        return getById(id) != null;
    }

    @Override
    public void save(CourseReview elem) {
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
    public void update(CourseReview elem) {
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
    public void saveOrUpdate(CourseReview elem) {
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
    public CourseReview deleteById(UUID id) {
        try {
            Transaction transaction = session.beginTransaction();
            CourseReview courseReview = getById(id);
            session.delete(courseReview);
            transaction.commit();
            return courseReview;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void clear() {
        try {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("delete course_reviews ");
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<CourseReview> getCourseReviews(UUID courseId) {
        return session.createQuery("from course_reviews where course.id = '" + courseId + "'").list();
    }
}
