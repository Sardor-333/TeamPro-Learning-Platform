package com.app.service;

import com.app.dto.CourseDto;
import com.app.model.Category;
import com.app.model.Course;
import com.app.model.User;
import com.app.repository.CategoryRepository;
import com.app.repository.CourseRepository;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CourseService {
    private CourseRepository courseRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Autowired
    public CourseService(
            CourseRepository courseRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<Course> getCourses(HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role.equals("MENTOR")) {
            UUID authorId = UUID.fromString(session.getAttribute("userId").toString());
            return courseRepository.getAuthorCourses(authorId);
        }
        return courseRepository.getAll();
    }

    public Course getCourse(UUID courseId) {
        return courseRepository.getById(courseId);
    }

    public List<User> getAuthors() {
        return courseRepository.getAuthors();
    }

    public List<Category> getCategories() {
        return categoryRepository.getAll();
    }

    public void saveCourse(CourseDto courseDto, HttpSession session) {
        Course course = new Course(courseDto.getName(), courseDto.getDescription());
        course.setId(courseDto.getId());
        if (courseDto.getCategoryId() != null) {
            course.setCategory(categoryRepository.getById(courseDto.getCategoryId()));
        }
        List<User> authors = new ArrayList<>();
        UUID mainAuthorId = UUID.fromString(session.getAttribute("userId").toString());
        User mainAuthor = userRepository.getById(mainAuthorId);
        authors.add(mainAuthor);
        if (courseDto.getAuthorIds() != null) {
            for (UUID authorId : courseDto.getAuthorIds()) {
                if (authorId != null) {
                    User author = userRepository.getById(authorId);
                    authors.add(author);
                }
            }
        }
        course.setAuthors(authors);
        courseRepository.saveOrUpdate(course);
    }

    public Course getById(UUID id) {
        return courseRepository.getById(id);
    }

    public Course deleteCourse(UUID courseId) {
        return courseRepository.deleteById(courseId);
    }
}
