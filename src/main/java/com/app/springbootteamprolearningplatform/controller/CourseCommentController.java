package com.app.springbootteamprolearningplatform.controller;

import com.app.springbootteamprolearningplatform.service.CourseCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/courses/comments")
public class CourseCommentController {
    private CourseCommentService courseCommentService;

    @Autowired
    public CourseCommentController(CourseCommentService courseCommentService) {
        this.courseCommentService = courseCommentService;
    }

    @PostMapping("/add/{courseId}")
    public String leaveComment(@PathVariable UUID courseId, @RequestParam(value = "comment") String comment, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (!sessionHasAttributes(session, "userId", "role")) return "login-form";

        UUID userId = UUID.fromString(session.getAttribute("userId").toString());
        courseCommentService.save(courseId, userId, comment);
        return "redirect:/courses/" + courseId;
    }

    @GetMapping("/delete")
    public String deleteComment(@RequestParam(name = "commentId") UUID commentId) {
        UUID courseId = courseCommentService.deleteComment(commentId);
        if (courseId == null) {
            return "redirect:/courses";
        }
        return "redirect:/courses/" + courseId;
    }

    private boolean sessionHasAttributes(HttpSession session, String... values) {
        for (String value : values)
            if (session.getAttribute(value) == null) return false;
        return true;
    }
}
