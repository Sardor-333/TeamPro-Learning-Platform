package com.app.springbootteamprolearningplatform.service;

import com.app.springbootteamprolearningplatform.dto.CourseCommentDto;
import com.app.springbootteamprolearningplatform.dto.CourseDto;
import com.app.springbootteamprolearningplatform.model.*;
import com.app.springbootteamprolearningplatform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;
    private final CourseVoteRepository courseVoteRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public CourseService(
            CourseRepository courseRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository,
            AttachmentRepository attachmentRepository,
            CourseVoteRepository courseVoteRepository,
            RoleRepository roleRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
        this.courseVoteRepository = courseVoteRepository;
        this.roleRepository = roleRepository;
    }

    public List<Course> getCourses(HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role.equals("MENTOR")) {
            UUID authorId = UUID.fromString(session.getAttribute("userId").toString());
            return courseRepository.findAuthorCourses(authorId);
        }
        return courseRepository.findAll();
    }

    public Course getCourse(UUID courseId) {
        return courseRepository.findById(courseId).orElse(null);
    }

    public List<User> getAuthors() {
        return userRepository.findAllByRoles(roleRepository.findByName("MENTOR").orElse(null));
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public void saveCourse(CourseDto courseDto, HttpSession session) {
        Course course = initializeCourse(courseDto, session);
        course.setAttachment(saveFileToDb(courseDto.getImg()));
        courseRepository.save(course);
    }

    public Course getById(UUID id) {
        return courseRepository.getById(id);
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
        Course course = courseRepository.findById(courseId).get();
        User user = userRepository.findById(userId).get();
        CourseVote courseVote = new CourseVote(course, user, rank);
        courseVoteRepository.save(courseVote);
    }

    public Double getCourseRate(UUID courseId) {
        return courseRepository.findCourseRate(courseId);
    }

    public List<CourseCommentDto> getCourseCommentDtos(UUID courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        List<CourseComment> courseComments = Objects.requireNonNull(course).getComments();

        if (courseComments != null) {
            List<CourseCommentDto> courseCommentDtoList = new ArrayList<>();
            for (CourseComment cr : courseComments) {
                CourseCommentDto courseCommentDto = new CourseCommentDto();
                if (cr.getUser().getAttachment() != null)
                    courseCommentDto.setBase64(getBase64Encode(cr.getUser().getAttachment().getBytes()));
                courseCommentDto.setUserId(cr.getUser().getId());
                courseCommentDto.setId(cr.getId());
                courseCommentDto.setBody(cr.getBody());
                courseCommentDto.setPostedTime(getPostedFormat(cr.getPostedAt()));
                courseCommentDto.setUserFirstName(cr.getUser().getFirstName());
                courseCommentDto.setUserLastName(cr.getUser().getLastName());

                courseCommentDtoList.add(courseCommentDto);
            }
            return courseCommentDtoList;
        }
        return null;
    }

    private String getBase64Encode(byte[] bytes) {
        byte[] encode = Base64.getEncoder().encode(bytes);
        return new String(encode, StandardCharsets.UTF_8);
    }

    public String getPostedFormat(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a"));
        }
        return null;
    }

    public void deleteCourse(UUID id) {
        courseRepository.deleteById(id);
    }
}
