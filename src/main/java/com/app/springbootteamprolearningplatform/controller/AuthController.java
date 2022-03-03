package com.app.springbootteamprolearningplatform.controller;

import com.app.springbootteamprolearningplatform.dto.UserDto;
import com.app.springbootteamprolearningplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute(name = "user") UserDto user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) return "login-form";
        boolean isRegistered = userService.registerUser(user);
        if (!isRegistered) {
            model.addAttribute("msg", "username or password error!");
            return "login-form";
        }
        return "redirect:/auth/login";
    }

    // LOGIN
    @GetMapping("/login")
    public String getLogin() {
        return "login-form";
    }

    @PostMapping("/login")
    public String login(String email, String password, HttpServletRequest request, Model model) {
        String login = userService.login(email, password, request, model);
        if (login.equals("login")) {
            return "login-form";
        } else if (login.equals("select-role")) {
            return "select-role";
        } else {
            return "redirect:/courses";
        }
    }

    // LOGOUT
    @GetMapping("/logOut")
    public String logOut(HttpServletRequest request) {
        userService.logOut(request);
        return "redirect:/auth/login";
    }


    // SELECT ROLE
    @GetMapping("/role")
    public String setRole(HttpServletRequest req, String role) {
        return userService.setRole(role, req);
    }
}
