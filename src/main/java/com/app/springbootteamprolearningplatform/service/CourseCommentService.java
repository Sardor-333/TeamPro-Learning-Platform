package com.app.springbootteamprolearningplatform.service;

import com.app.springbootteamprolearningplatform.model.Course;
import com.app.springbootteamprolearningplatform.model.CourseComment;
import com.app.springbootteamprolearningplatform.model.User;
import com.app.springbootteamprolearningplatform.repository.CourseCommentRepository;
import com.app.springbootteamprolearningplatform.repository.CourseRepository;
import com.app.springbootteamprolearningplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseCommentService {
    private CourseCommentRepository courseCommentRepository;
    private UserRepository userRepository;
    private CourseRepository courseRepository;

    @Autowired
    public CourseCommentService(CourseCommentRepository courseCommentRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.courseCommentRepository = courseCommentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public UUID deleteComment(UUID commentId, HttpServletRequest request) {
        Optional<CourseComment> commentOptional = courseCommentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            CourseComment courseComment = commentOptional.get();
            UUID courseId = courseComment.getCourse().getId();

            UUID userId = UUID.fromString(request.getSession().getAttribute("userId").toString());
            User user = userRepository.findById(userId).orElse(null);

            if (user.getId().equals(courseComment.getUser().getId())) {
                courseCommentRepository.delete(courseComment);
            }
            return courseId;
        }
        return null;
    }

    public void save(UUID courseId, UUID userId, String commentBody) {
        Course course = courseRepository.findById(courseId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        CourseComment comment = new CourseComment(course, user, commentBody, LocalDateTime.now());
        courseCommentRepository.save(comment);
    }

    public List<CourseComment> findCourseComments(UUID courseId) {
        return courseCommentRepository.findAllByCourseId(courseId);
    }
}
