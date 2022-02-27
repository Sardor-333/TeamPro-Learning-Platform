package com.app.controller;

import com.app.dto.CourseDto;
import com.app.dto.CourseReviewDto;
import com.app.model.Category;
import com.app.model.Course;
import com.app.model.User;
import com.app.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public String getCourses(Model model, HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") int page) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) return "login-form";
        if(page>1) courseService.page = page;
//        List<Course> courses = courseService.getCourses(session);
//        model.addAttribute("courses", courses);
        model.addAttribute("currentPage", page);
        model.addAttribute("courses",courseService.getCoursesL(page, courseService.limit ));
        model.addAttribute("endPage", courseService.endPage(page));
        model.addAttribute("beginPage", courseService.beginPage(page));
        model.addAttribute("pageCount", courseService.pageCount());
        model.addAttribute("listPage", courseService.getPageList(courseService.beginPage(page), courseService.endPage(page)));
        return "courses";
    }

    @GetMapping("/{courseId}")
    public String getCourse(@PathVariable UUID courseId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) return "login-form";

        Course course = courseService.getCourse(courseId);
        model.addAttribute("course", course);

        if (course.getAttachment() != null)
            model.addAttribute("img", getBase64Encode(course.getAttachment().getBytes()));

        Integer courseRate = courseService.getCourseRate(courseId);
        model.addAttribute("courseRate", courseRate);

        List<CourseReviewDto> courseReviews = courseService.getCourseReviewDtos(courseId);
        model.addAttribute("courseComments", courseReviews);

        return "course";
    }

    @GetMapping("/add")
    public String getAddForm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) return "login-form";

        List<User> authors = courseService.getAuthors();
        List<Category> categories = courseService.getCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("authors", authors);
        return "add-course";
    }

    @PostMapping("/add")
    public String saveCourse(CourseDto courseDto, HttpServletRequest request) {
        courseService.saveCourse(courseDto, request.getSession());
        return "redirect:/courses";
    }

    @GetMapping("/update/{courseId}")
    public String getUpdateForm(@PathVariable UUID courseId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) return "login-form";

        model.addAttribute("course", courseService.getById(courseId));

        List<User> authors = courseService.getAuthors();
        model.addAttribute("authors", authors);
        List<Category> categories = courseService.getCategories();
        model.addAttribute("categories", categories);

        return "update-course";
    }

    @GetMapping("/delete")
    public String deleteCourse(@RequestParam UUID id) {
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }

    @PostMapping("/rate/{courseId}")
    public String rateCourse(@PathVariable UUID courseId, @RequestParam(name = "rank") Integer rank, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) return "login-form";

        UUID userId = UUID.fromString(session.getAttribute("userId").toString());
        courseService.rateCourse(courseId, userId, rank);
        return "redirect:/course/" + courseId;
    }

    @PostMapping("/addComment/{courseId}")
    public String leaveComment(@PathVariable UUID courseId, @RequestParam(name = "comment") String comment, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) return "login-form";
        UUID userId = UUID.fromString(session.getAttribute("userId").toString());
        courseService.leaveComment(courseId, userId, comment);
        return "redirect:/courses/" + courseId;
    }

    private String getBase64Encode(byte[] bytes) {
        try {
            byte[] encode = Base64.getEncoder().encode(bytes);
            return new String(encode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private boolean sessionHasAttributes(HttpSession session, String... values) {
        for (String value : values)
            if (session.getAttribute(value) == null) return false;
        return true;
    }

    @ModelAttribute(value = "role")
    public String getRole(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (String) session.getAttribute("role");
    }
}
