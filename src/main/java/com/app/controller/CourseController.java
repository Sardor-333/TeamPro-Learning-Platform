package com.app.springbootteamprolearningplatform.controller;

import com.app.springbootteamprolearningplatform.dto.CourseCommentDto;
import com.app.springbootteamprolearningplatform.dto.CourseDto;
import com.app.springbootteamprolearningplatform.model.Category;
import com.app.springbootteamprolearningplatform.model.Course;
import com.app.springbootteamprolearningplatform.model.User;
import com.app.springbootteamprolearningplatform.service.CourseService;
import com.app.springbootteamprolearningplatform.service.UserService;
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
    private UserService userService;

    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    // GET
    @GetMapping
    public String getCourses(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) return "login-form";

        List<Course> courses = courseService.getCourses(session);
        model.addAttribute("courses", courses);
        return "courses";
    }

    @GetMapping("/{courseId}")
    public String getCourse(@PathVariable UUID courseId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) return "login-form";

        Course course = courseService.getCourse(courseId);
        model.addAttribute("course", course);

        if (course.getAttachment() != null)
            model.addAttribute("img", course.getBase64Encode());

        Double courseRate = courseService.getCourseRate(courseId);
        model.addAttribute("courseRate", courseRate);

        List<CourseCommentDto> courseComments = courseService.getCourseCommentDtos(courseId);
        model.addAttribute("courseComments", courseComments);

        return "course";
    }

    // ADD
    @GetMapping("/add")
    public String getAddForm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) return "login-form";

        List<Category> categories = courseService.getCategories();
        model.addAttribute("categories", categories);

        List<User> authors = userService.getAuthors();
        model.addAttribute("authors", authors);
        return "add-course";
    }

    @PostMapping("/add")
    public String saveCourse(CourseDto courseDto, HttpServletRequest request) {
        courseService.saveCourse(courseDto, request.getSession());
        return "redirect:/courses";
    }


    // UPDATE
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


    // DELETE
    @GetMapping("/delete")
    public String deleteCourse(@RequestParam UUID id) {
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }


    // RATE
    @PostMapping("/rate/{courseId}")
    public String rateCourse(@PathVariable UUID courseId, @RequestParam(name = "rank") Integer rank, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) return "login-form";

        UUID userId = UUID.fromString(session.getAttribute("userId").toString());
        courseService.rateCourse(courseId, userId, rank);
        return "redirect:/course/" + courseId;
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
