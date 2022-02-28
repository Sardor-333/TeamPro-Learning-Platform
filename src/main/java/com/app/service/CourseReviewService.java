package com.app.service;

import com.app.model.Course;
import com.app.model.CourseReview;
import com.app.model.User;
import com.app.repository.CourseRepository;
import com.app.repository.CourseReviewRepository;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class CourseReviewService {
    CourseReviewRepository courseReviewRepository;
    UserRepository userRepository;
    CourseRepository courseRepository;

    @Autowired
    public CourseReviewService(CourseReviewRepository courseReviewRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.courseReviewRepository = courseReviewRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public Course deleteComment(UUID commentId, HttpSession session) {
        User sessionUser = userRepository.getById(UUID.fromString(session.getAttribute("userId").toString()));
        CourseReview targetComment = courseReviewRepository.getById(commentId);
        if (targetComment.getUser().getId().equals(sessionUser.getId())) {
            Course commentCourse = targetComment.getCourse();
            courseReviewRepository.deleteById(commentId);
            return commentCourse;
        }
        return null;
    }

    public List<CourseReview> getAll() {
        return courseReviewRepository.getAll();
    }

    public CourseReview getById(UUID id) {
        return courseReviewRepository.getById(id);
    }

    public void save(UUID courseId, UUID userId, String comment) {
        Course course = courseRepository.getById(courseId);
        User user = userRepository.getById(userId);
        CourseReview review = new CourseReview(course, user, comment, LocalDateTime.now());
        courseReviewRepository.save(review);
    }

    public CourseReview deleteById(UUID id) {
        return courseReviewRepository.deleteById(id);
    }

    public List<CourseReview> getCourseReviews(UUID courseId) {
        return courseReviewRepository.getCourseReviews(courseId);
    }
}