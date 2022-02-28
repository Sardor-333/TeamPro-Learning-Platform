package com.app.controller;

import com.app.model.Course;
import com.app.service.CourseReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/courses/comments")
public class CourseReviewController {
    private CourseReviewService courseReviewService;

    @Autowired
    public CourseReviewController(CourseReviewService courseReviewService) {
        this.courseReviewService = courseReviewService;
    }

    @PostMapping("/leaveComment/{courseId}")
    public String leaveComment(@PathVariable UUID courseId, @RequestParam(value = "comment") String comment, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) return "login-form";
        UUID userId = UUID.fromString(session.getAttribute("userId").toString());
        courseReviewService.save(courseId, userId, comment);
        return "redirect:/courses/" + courseId + "";
    }

    @GetMapping("/delete")
    public String deleteComment(@RequestParam(name = "commentId") UUID commentId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) return "login-form";
        Course course = courseReviewService.deleteComment(commentId, session);
        return "redirect:/courses/" + course.getId() + "";
    }

    private boolean sessionHasAttributes(HttpSession session, String... values) {
        for (String value : values)
            if (session.getAttribute(value) == null) return false;
        return true;
    }
}
