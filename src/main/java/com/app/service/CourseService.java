package com.app.service;

import com.app.dto.CourseDto;
import com.app.model.*;
import com.app.repository.CategoryRepository;
import com.app.repository.CourseRepository;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
        Role role = (Role) session.getAttribute("role");
        if (role.getName().equals("MENTOR")) {
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
        Course course = initializeCourse(courseDto, session);
        Attachment attachment = saveFileToDb(courseDto.getImg());
        if (attachment != null) {
            userRepository.saveObj(attachment);
            course.setAttachment(attachment);
        }
        courseRepository.saveOrUpdate(course);
    }

    public Course getById(UUID id) {
        return courseRepository.getById(id);
    }

    public Course deleteCourse(UUID courseId) {
        return courseRepository.deleteById(courseId);
    }

    private Course initializeCourse(CourseDto courseDto, HttpSession session) {
        Course course = new Course(courseDto.getName(), courseDto.getDescription());
        course.setId(courseDto.getId());
        UUID mainAuthorId = UUID.fromString(session.getAttribute("userId").toString());
        User mainAuthor = userRepository.getById(mainAuthorId);
        List<User> authors = new ArrayList<>(Arrays.asList(mainAuthor));
        UUID[] authorIds = courseDto.getAuthorIds();
        if (authorIds != null) {
            for (UUID authorId : authorIds) {
                if (authorId != null) {
                    User anotherAuthor = userRepository.getById(authorId);
                    authors.add(anotherAuthor);
                }
            }
        }
        course.setAuthors(authors);
        if (courseDto.getCategoryId() != null) {
            Category category = categoryRepository.getById(courseDto.getCategoryId());
            course.setCategory(category);
        }
        return course;
    } // todo debug

    private Attachment saveFileToDb(MultipartFile multipartFile) {
        try {
            if (multipartFile != null) {
                Attachment attachment = new Attachment();
                attachment.setBytes(multipartFile.getBytes());
                return attachment;
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public void rateCourse(UUID courseId, UUID userId, Integer rank) {
        Course course = courseRepository.getById(courseId);
        User user = userRepository.getById(userId);

        CourseVote courseVote = new CourseVote(course, user, rank);
        userRepository.saveObj(courseVote);
    }

    public Integer getCourseRate(UUID courseId) {
        return courseRepository.getCourseRate(courseId);
    }
}
