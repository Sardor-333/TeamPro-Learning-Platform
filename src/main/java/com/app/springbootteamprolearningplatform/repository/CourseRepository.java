package com.app.springbootteamprolearningplatform.repository;

import com.app.springbootteamprolearningplatform.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    // find specific user courses if it has the role of mentor
    @Query(value =
            "" +
                    "from courses c " +
                    "join c.authors a " +
                    "join a.roles r " +
                    "where a.id = ?1 and r.name = 'MENTOR'")
    List<Course> findAuthorCourses(UUID authorId);

    // find specific course rate
    @Query(value =
            "select avg(vote.rank) as rate from courses course " +
                    "join course.votes vote " +
                    "where course.id = ?1")
    Double findCourseRate(UUID courseId);
}
