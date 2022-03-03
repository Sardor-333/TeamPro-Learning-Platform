package com.app.springbootteamprolearningplatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
