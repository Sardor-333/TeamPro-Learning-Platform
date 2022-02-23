package com.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @GetMapping
    public String getCourses(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (!sessionHasAttribute(session, "userId") && !sessionHasAttribute(session, "role")) {
            return "login-form";
        }
        return "";
    }

    private boolean sessionHasAttribute(HttpSession httpSession, String value) {
        Object attribute = httpSession.getAttribute(value);
        return attribute != null;
    }
}
