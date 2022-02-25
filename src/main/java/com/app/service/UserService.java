package com.app.service;

import com.app.dto.UserDto;
import com.app.model.Attachment;
import com.app.model.Role;
import com.app.model.User;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(UserDto userDto) {
        if (!userRepository.existsByEmail(userDto.getEmail())) {
            if (userRepository.isValidEmail(userDto.getEmail())
                    && userRepository.isValidPassword(userDto.getPassword())
                    && userDto.getPassword().equals(userDto.getConfirmPassword())) {
                User user = new User(
                        userDto.getFirstName(),
                        userDto.getLastName(),
                        userDto.getEmail(),
                        userDto.getPassword(),
                        userDto.getBio()
                );

                MultipartFile photo = userDto.getPhoto();
                if (photo.getSize() != 0) {
                    try {
                        Attachment attachment = new Attachment();
                        attachment.setBytes(photo.getBytes());
                        userRepository.saveObj(attachment);
                        user.setAttachment(attachment);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Role role = userRepository.getRole("USER");
                if (role == null) {
                    role = new Role("USER");
                    userRepository.saveRole(role);
                }
                List<Role> roles = new ArrayList<>(List.of(role));
                user.setRoles(roles);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public String login(String email, String password,
                        HttpServletRequest req, Model model) {
        try {
            User user = userRepository.getByEmail(email);
            if (user != null && user.getPassword().equals(password)) {
                HttpSession session = req.getSession();
                session.setAttribute("userId", user.getId());
                if (user.getRoles().size() == 1) {
                    session.setAttribute("userRoles", user.getRoles().get(0));
                    return "courses";
                } else {
                    model.addAttribute("roles",userRepository.getRoles(user.getId()));
                    return "select-role";
                }
            }
            model.addAttribute("msg", "email or password error");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "login";
    }

    public String setRole(String role, HttpServletRequest req) {
        HttpSession session1 = req.getSession(false);
        Object userId = session1.getAttribute("userId");
        if (userId != null) {
            HttpSession session = req.getSession();
            session.setAttribute("role", role);
            return "redirect:/courses";
        }
        return "redirect:/auth/login";
    }
}
