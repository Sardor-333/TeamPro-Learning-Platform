package com.app.service;

import com.app.dto.CourseDto;
import com.app.dto.CourseReviewDto;
import com.app.model.*;
import com.app.repository.CategoryRepository;
import com.app.repository.CourseRepository;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class CourseService {
    private CourseRepository courseRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private CourseReviewService courseReviewService;

    public int limit = 3;
    public int page = 1;

    @Autowired
    public CourseService(
            CourseRepository courseRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository,
            CourseReviewService courseReviewService) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.courseReviewService = courseReviewService;
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

    public List<CourseReviewDto> getCourseReviewDtos(UUID courseId) {
        List<CourseReview> courseReviews = courseReviewService.getCourseReviews(courseId);

        if (courseReviews != null) {
            List<CourseReviewDto> courseReviewDtoList = new ArrayList<>();
            for (CourseReview cr : courseReviews) {
                CourseReviewDto courseReviewDto = new CourseReviewDto();
                if (cr.getUser().getAttachment() != null)
                    courseReviewDto.setBase64(getBase64Encode(cr.getUser().getAttachment().getBytes()));
                courseReviewDto.setId(cr.getId());
                courseReviewDto.setBody(cr.getBody());
                courseReviewDto.setPostedTime(getPostedFormat(cr.getPostedAt()));
                courseReviewDto.setUserId(cr.getUser().getId());
                courseReviewDto.setUserFirstName(cr.getUser().getFirstName());
                courseReviewDto.setUserLastName(cr.getUser().getLastName());

                courseReviewDtoList.add(courseReviewDto);
            }
            return courseReviewDtoList;
        }
        return null;
    }

    private String getBase64Encode(byte[] bytes) {
        try {
            byte[] encode = Base64.getEncoder().encode(bytes);
            return new String(encode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public String getPostedFormat(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a"));
        }
        return null;
    }

    public void leaveComment(UUID courseId, UUID userId, String comment) {
        Course course = courseRepository.getById(courseId);
        User user = userRepository.getById(userId);
        CourseReview review = new CourseReview(course, user, comment, LocalDateTime.now());
        userRepository.saveObj(review);
    }


    public int beginPage(int page){
        if (page>4) {
            return page-2;
        }else {
            return 3;
        }
    }

    public int pageCount(){
        int categoryCount = courseRepository.getCourseCount();
        return categoryCount%limit==0? categoryCount/limit : categoryCount/limit+1;
    }

    public int endPage(int page){
        int pageCount = pageCount();
        if (page + 2<pageCount-2) {
            return page+2;
        }else {
            return pageCount-2;
        }
    }

    public List<Integer> getPageList(int beginPage, int endPage) {
        List<Integer> pageList = new ArrayList<>();
        for (int i=beginPage; i <= endPage; i++){
            pageList.add(i);
        }
        return pageList;
    }

    public List<Course> getCoursesL(int page, int limit) {
        return courseRepository.getCoursesL(page,limit);
    }
}
