package com.app.repository;

import com.app.model.Category;
import com.app.model.Course;
import com.app.model.CourseReview;
import com.app.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class CourseRepository implements BaseRepository<Course, UUID> {
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
        Course course = session.get(Course.class, id);
        return course;
    }

    @Override
    public boolean existsById(UUID id) {
        return getById(id) != null;
    }

    @Override
    public void save(Course elem) {
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
    public void update(Course elem) {
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
    public void saveOrUpdate(Course elem) {
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
    public Course deleteById(UUID id) {
        try {
            Transaction transaction = session.beginTransaction();
            Course course = getById(id);
            session.delete(course);
            transaction.commit();
            return course;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void clear() {
        try {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("delete courses");
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Course> getAuthorCourses(UUID authorId) {
        NativeQuery sqlQuery = session.createSQLQuery("select c.* from courses c\n" +
                "join courses_authors ca on c.id = ca.course_id\n" +
                "join users u on ca.user_id = u.id\n" +
                "join users_roles ur on u.id = ur.user_id\n" +
                "join roles r on ur.role_id = r.id\n" +
                "where r.name = 'MENTOR' and u.id = '" + authorId + "'");
        List list = sqlQuery.list();
        return list;
    }
//    public List<Course> getAuthorCourses(UUID authorId) {
//        Query query = session.createQuery("from courses course join course.authors author where author.id = '" + authorId + "'");
//        List list = query.list();
//        return list;
//    }

    public List<User> getAuthors() {
        Query query = session.createQuery("" +
                "from users u " +
                "join u.roles r " +
                "where r.name = 'MENTOR'");
        List list = query.list();
        return list;
    }

    public Integer getCourseRate(UUID courseId) {
        NativeQuery sqlQuery = session.createSQLQuery("select coalesce(cast(" +
                "(round(avg(rank))) as int), 0) as rate from courses c\n" +
                "join course_votes cv on c.id = cv.course_id\n" +
                "join users u on cv.user_id = u.id\n" +
                "where course_id = '" + courseId + "';\n");

        Integer rate = (Integer) sqlQuery.list().get(0);
        return rate;
    }

    public Integer getCourseCount(){
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("select count(c.id) from courses c");
        Long aLong = (Long)query.uniqueResult();
        transaction.commit();
        return Integer.parseInt(aLong.toString());
    }

    public List<Course> getCoursesL(int page, int limit) {
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from courses");
        query.setFirstResult((page-1)*limit);
        query.setMaxResults(limit);
        List<Course> list = query.list();
        transaction.commit();
        return list;
    }

}
