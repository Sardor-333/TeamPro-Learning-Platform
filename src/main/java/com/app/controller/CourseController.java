package com.app.controller;

import com.app.dto.CourseDto;
import com.app.model.Category;
import com.app.model.Course;
import com.app.model.User;
import com.app.repository.CategoryRepository;
import com.app.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private CourseService courseService;

    public CourseController(CourseService courseService, CategoryRepository categoryRepository) {
        this.courseService = courseService;
    }

    @GetMapping
    public String getCourses(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!sessionHasAttribute(session, "userId") && !sessionHasAttribute(session, "role")) {
            model.addAttribute("msg", "Please login first");
            return "login-form";
        }

        List<Course> courses = courseService.getCourses(session);
        model.addAttribute("courses", courses);
        return "courses";
    }

    @GetMapping("/{courseId}")
    public String getCourse(@PathVariable UUID courseId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!sessionHasAttribute(session, "userId") && !sessionHasAttribute(session, "role")) {
            model.addAttribute("msg", "Please login first");
            return "login-form";
        }
        Course course = courseService.getCourse(courseId);
        model.addAttribute("course", course);
        return "course";
    }

    @GetMapping("/add")
    public String getAddForm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!sessionHasAttribute(session, "userId") && !sessionHasAttribute(session, "role")) {
            model.addAttribute("msg", "Please login first");
            return "login-form";
        }

        List<User> authors = courseService.getAuthors();
        List<Category> categories = courseService.getCategories();
        model.addAttribute("authors", authors);
        model.addAttribute("categories", categories);
        return "update-course";
    }

    @PostMapping("/add")
    public String saveCourse(CourseDto courseDto, HttpServletRequest request) {
        courseService.saveCourse(courseDto, request.getSession());
        return "redirect:/courses";
    }

    private boolean sessionHasAttribute(HttpSession httpSession, String value) {
        Object attribute = httpSession.getAttribute(value);
        return attribute != null;
    }
}
