package com.app.controller;
import com.app.dto.CourseDto;
import com.app.model.Category;
import com.app.model.Course;
import com.app.model.User;
import com.app.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    public String getCourses(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (sessionHasAttribute(session, "userId") && sessionHasAttribute(session, "role")) {
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
        if (sessionHasAttribute(session, "userId") && sessionHasAttribute(session, "role")) {
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
        if (sessionHasAttribute(session, "userId") && sessionHasAttribute(session, "role")) {
            model.addAttribute("msg", "Please login first");
            return "login-form";
        }

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
        if (sessionHasAttribute(session, "userId") && sessionHasAttribute(session, "role")) {
            model.addAttribute("msg", "Please login first");
            return "login-form";
        }

        model.addAttribute("course", courseService.getById(courseId));

        List<User> authors = courseService.getAuthors();
        model.addAttribute("authors", authors);
        List<Category> categories = courseService.getCategories();
        model.addAttribute("categories", categories);

        return "update-course";
    }

    @GetMapping("/delete")
    public String deleteCourse(@RequestParam UUID id) {
        Course deletedCourse = courseService.deleteCourse(id);
        return "redirect:/courses";
    }

    private boolean sessionHasAttribute(HttpSession httpSession, String value) {
        return httpSession.getAttribute(value) == null;
    }
}
