package com.app.controller;

import com.app.dto.UserDto;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // register
    @GetMapping("/register")
    public String getRegister() {
        return "register-form";
    }

    @PostMapping("/register")
    public String register(UserDto userDto, Model model) {
        boolean isRegistered = userService.registerUser(userDto);
        if (!isRegistered) {
            model.addAttribute("userDto", userDto);
            return "register-form";
        }
        return "redirect:/auth/login";
    }

    // login
    @GetMapping("/login")
    public String getLogin() {
        return "login-form";
    }

    @PostMapping("/login")
    public String login(String email, String password, HttpServletRequest request, Model model) {
        return userService.login(email, password, request, model);
    }

    @GetMapping("/role/{role}")
    public String setRole(@PathVariable String role, HttpServletRequest req) {
        return userService.setRole(role, req);
    }
}
