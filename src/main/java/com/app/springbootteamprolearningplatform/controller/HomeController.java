package com.app.springbootteamprolearningplatform.controller;

import com.app.springbootteamprolearningplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String getHome() {
        return "index";
    }

    @GetMapping("/about")
    public String getAbout() {
        return "about";
    }

    @GetMapping("/team")
    public String getTeam() {
        return "team";
    }

    @GetMapping("/contact")
    public String getContact() {
        return "contact";
    }

    @GetMapping("/blog")
    public String getBlog() {
        return "blog";
    }

    @GetMapping("/chat")
    public String getchat() {
        return "chat";
    }

    @GetMapping("/77")
    public String get77() {
        return "77";
    }
}
