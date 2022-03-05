package com.app.springbootteamprolearningplatform.controller;

import com.app.springbootteamprolearningplatform.dto.CourseDto;
import com.app.springbootteamprolearningplatform.model.Category;
import com.app.springbootteamprolearningplatform.model.Course;
import com.app.springbootteamprolearningplatform.model.User;
import com.app.springbootteamprolearningplatform.repository.CategoryRepository;
import com.app.springbootteamprolearningplatform.service.CourseService;
import com.app.springbootteamprolearningplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping({"/courses"})
public class CourseController {
    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public CourseController(CourseService courseService, UserService userService, CategoryRepository categoryRepository) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @GetMapping
    public String getCourses(Model model,
                             HttpServletRequest request,
                             @RequestParam(required = false, defaultValue = "0") Integer page) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) {
            return "login-form";
        } else {
            if (page == null || page < 0) page = 0;
            User user = userService.getUserById((UUID) session.getAttribute("userId"));
            model.addAttribute("userId", user.getId());
            model.addAttribute("currentPage", page);
            model.addAttribute("courses", courseService.getCoursesPageable(user, page));
            model.addAttribute("endPage", courseService.endPage(page));
            model.addAttribute("beginPage", courseService.beginPage(page));
            model.addAttribute("pageCount", courseService.pageCount());
            model.addAttribute("listPage", courseService.getPageList(courseService.beginPage(page), courseService.endPage(page)));
            model.addAttribute("categories", courseService.getCategoriesWithInfo());
            return "blog";
        }
    }

    @GetMapping("/byCategory/{categoryId}")
    public String getCoursesByCategory(@PathVariable UUID categoryId,
                                       @RequestParam(required = false, defaultValue = "0") Integer page,
                                       Model model,
                                       HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) {
            return "login-form";
        } else {
            if (page == null || page < 0) page = 0;
            User user = userService.getUserById((UUID) session.getAttribute("userId"));
            model.addAttribute("userId", user.getId());
            model.addAttribute("currentPage", page);
            model.addAttribute("courses", courseService.getCoursesByCategory(categoryId, user));
            model.addAttribute("endPage", courseService.endPage(page));
            model.addAttribute("beginPage", courseService.beginPage(page));
            model.addAttribute("pageCount", courseService.pageCount());
            model.addAttribute("listPage", courseService.getPageList(courseService.beginPage(page), courseService.endPage(page)));

            model.addAttribute("categories", courseService.getCategoriesWithInfo());
            return "blog";
        }
    }

    @GetMapping({"/{courseId}"})
    public String getCourse(@PathVariable UUID courseId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) {
            return "login-form";
        } else {
            Course course = courseService.getCourse(courseId);

            model.addAttribute("course", course);
            model.addAttribute("img", course.getBase64Encode());
            model.addAttribute("courseRate", courseService.getCourseRate(courseId));
            model.addAttribute("courseComments", courseService.getCourseCommentDtos(courseId));
            model.addAttribute("authors", course.getAuthors().stream().map(user -> user.getLastName() + " " + user.getFirstName()).toList());

            return "course";
        }
    }

    @GetMapping({"/add"})
    public String getAddForm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) {
            return "login-form";
        } else {
            List<User> authors = courseService.getAuthors();
            List<Category> categories = courseService.getCategories();
            model.addAttribute("categories", categories);
            model.addAttribute("authors", authors);
            return "add-course";
        }
    }

    @GetMapping("/buy")
    public String buyCourse(@RequestParam(name = "userId") UUID userId,
                            @RequestParam(name = "courseId") UUID courseId,
                            Model model, RedirectAttributes redirectAttributes) {
        String s = userService.buyCourse(userId, courseId);
        redirectAttributes.addFlashAttribute("msg", s);
        return "redirect:/courses";
    }

    @PostMapping({"/add"})
    public String saveCourse(CourseDto courseDto, HttpServletRequest request) {
        courseService.saveCourse(courseDto, request.getSession());
        return "redirect:/courses";
    }

    @GetMapping({"/update/{courseId}"})
    public String getUpdateForm(@PathVariable UUID courseId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) {
            return "login-form";
        } else {
            model.addAttribute("course", courseService.getById(courseId));
            List<User> authors = courseService.getAuthors();
            model.addAttribute("authors", authors);
            List<Category> categories = courseService.getCategories();
            model.addAttribute("categories", categories);
            return "update-course";
        }
    }

    @GetMapping({"/delete"})
    public String deleteCourse(@RequestParam UUID id) {
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }

    @PostMapping({"/rate/{courseId}"})
    public String rateCourse(@PathVariable UUID courseId, @RequestParam(name = "rank") Integer rank, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) return "login-form";
        else {
            UUID userId = UUID.fromString(session.getAttribute("userId").toString());
            courseService.rateCourse(courseId, userId, rank);
            return "redirect:/courses/" + courseId;
        }
    }

    @ModelAttribute("role")
    public String getRole(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (String) session.getAttribute("role");
    }

    private boolean sessionHasAttributes(HttpSession session, String... values) {
        for (String value : values) {
            if (session.getAttribute(value) == null) return false;
        }
        return true;
    }
}
