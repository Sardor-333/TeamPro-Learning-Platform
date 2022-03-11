package com.app.springbootteamprolearningplatform.service;

import com.app.springbootteamprolearningplatform.dto.CourseCommentDto;
import com.app.springbootteamprolearningplatform.dto.CourseDto;
import com.app.springbootteamprolearningplatform.model.*;
import com.app.springbootteamprolearningplatform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CourseService {
    public final int limit = 3;
    private CourseRepository courseRepository;
    private CourseRateRepository courseRateRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private CourseCommentService courseCommentService;
    private RoleRepository roleRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository,
                         CategoryRepository categoryRepository,
                         UserRepository userRepository,
                         CourseCommentService courseCommentService,
                         RoleRepository roleRepository,
                         CourseRateRepository courseRateRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.courseCommentService = courseCommentService;
        this.roleRepository = roleRepository;
        this.courseRateRepository = courseRateRepository;
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
        Attachment attachment = saveFileToDb(courseDto.getImg());
        course.setAttachment(attachment);
        courseRepository.save(course);
    }

    public Course getById(UUID id) {
        return courseRepository.findById(id).orElse(null);
    }

    public void deleteCourse(UUID courseId) {
        courseRepository.deleteById(courseId);
    }

    private Course initializeCourse(CourseDto courseDto, HttpSession session) {
        Course course = new Course(courseDto.getName(), courseDto.getDescription());
        course.setId(courseDto.getId());

        UUID mainAuthorId = UUID.fromString(session.getAttribute("userId").toString());
        User mainAuthor = userRepository.getById(mainAuthorId);
        UUID[] authorIds = courseDto.getAuthorIds();

        List<User> authors = new ArrayList(Arrays.asList(mainAuthor));
        if (authorIds != null) {
            for (int i = 0; i < authorIds.length; ++i) {
                UUID authorId = authorIds[i];
                if (authorId != null) {
                    User anotherAuthor = userRepository.findById(authorId).orElse(null);
                    authors.add(anotherAuthor);
                }
            }
        }
        course.setAuthors(authors);
        if (courseDto.getCategoryId() != null) {
            course.setCategory(categoryRepository.findById(courseDto.getCategoryId()).orElse(null));
        }
        return course;
    }

    private Attachment saveFileToDb(MultipartFile multipartFile) {
        try {
            if (multipartFile != null) {
                Attachment attachment = new Attachment();
                attachment.setBytes(multipartFile.getBytes());
                return attachment;
            } else {
                return null;
            }
        } catch (IOException var3) {
            return null;
        }
    }

    public void rateCourse(UUID courseId, UUID userId, Integer rank) {
        Course course = courseRepository.getById(courseId);
        User user = userRepository.getById(userId);
        CourseRate vote = courseRateRepository.findByCourseIdAndUserId(courseId, userId);
        if(vote == null) vote = new CourseRate(course, user, rank);
        else vote.setRank(rank);
        courseRateRepository.save(vote);
    }

    public Integer getCourseRate(UUID courseId) {
        Double rate = courseRepository.findCourseRate(courseId);
        return Math.toIntExact(Math.round(rate));
    }

    public List<CourseCommentDto> getCourseCommentDtos(UUID courseId) {
        List<CourseComment> courseComments = courseCommentService.findCourseComments(courseId);

        List<CourseCommentDto> courseCommentDtoList = new ArrayList<>();
        for (CourseComment courseComment : courseComments) {
            CourseCommentDto courseCommentDto = new CourseCommentDto();
            courseCommentDto.setBase64(courseComment.getBase64());
            courseCommentDto.setUserId(courseComment.getUser().getId());
            courseCommentDto.setUserFirstName(courseComment.getUser().getFirstName());
            courseCommentDto.setUserLastName(courseComment.getUser().getLastName());

            courseCommentDto.setId(courseComment.getId());
            courseCommentDto.setBody(courseComment.getBody());
            courseCommentDto.setPostedTime(getPostedFormat(courseComment.getPostedAt()));

            courseCommentDtoList.add(courseCommentDto);
        }
        return courseCommentDtoList;
    }

    public String getPostedFormat(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a")) : null;
    }

    public int beginPage(int page) {
        return page > 4 ? page - 2 : 3;
    }

    public int pageCount() {
        int categoryCount = Integer.parseInt(Long.valueOf(courseRepository.count()).toString());
        return categoryCount % limit == 0 ? categoryCount / limit : categoryCount / limit + 1;
    }

    public int endPage(int page) {
        int pageCount = this.pageCount();
        return Math.min(page + 2, pageCount - 2);
    }

    public List<Integer> getPageList(int beginPage, int endPage) {
        List<Integer> pageList = new ArrayList<>();
        for (int i = beginPage; i <= endPage; ++i) {
            pageList.add(i);
        }
        return pageList;
    }

    public List<CourseDto> getCoursesPageable(User user, int page) {
        List<Course> courses = courseRepository.findAll(PageRequest.of(page-1, limit)).get().toList();
        return courseDtoFactory(user, courses);
    }



    public List<CourseDto> getCoursesByCategory(UUID categoryId, User user) {
        List<Course> courses = courseRepository.findAllByCategoryId(categoryId);
        return courseDtoFactory(user, courses);
    }

    private List<CourseDto> courseDtoFactory(User user, List<Course> courses) {
        List<CourseDto> userCourseDtoList = new ArrayList<>();
        for (Course course : courses) {
            Double voteDouble = courseRepository.findCourseRate(course.getId());
            if (voteDouble==null) voteDouble = 0.0;
            int courseRate = (int)Math.round(voteDouble);
            boolean status = false;
            for (Course userCourse : user.getUserCourses()) {
                if (course.getId().equals(userCourse.getId())) {
                    status = true;
                    break;
                }
            }
            CourseDto courseDto = new CourseDto(course.getId(), course.getName(), course.getDescription(),
                    course.getPrice(), status, course.getBase64Encode(), courseRate);
            userCourseDtoList.add(courseDto);
        }
        return userCourseDtoList;
    }

    public List<Category> getCategoriesWithInfo() {
        List<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            category.setCoursesCount(courseRepository.countAllByCategoryId(category.getId()));
        }
        return categories;
    }

    public Integer getCourseRate(UUID courseId, UUID userId) {
        CourseRate rate = courseRateRepository.findByCourseIdAndUserId(courseId, userId);
        if (rate==null) {
            return 0;
        }else return rate.getRank();
    }
}
