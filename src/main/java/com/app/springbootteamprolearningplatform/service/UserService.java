package com.app.springbootteamprolearningplatform.service;

import com.app.springbootteamprolearningplatform.dto.UserDto;
import com.app.springbootteamprolearningplatform.model.Attachment;
import com.app.springbootteamprolearningplatform.model.Role;
import com.app.springbootteamprolearningplatform.model.User;
import com.app.springbootteamprolearningplatform.repository.RoleRepository;
import com.app.springbootteamprolearningplatform.repository.UserRepository;
import com.app.springbootteamprolearningplatform.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public boolean registerUser(UserDto userDto) {
        if (
                !userRepository.existsByEmail(userDto.getEmail())
                        && Util.isValidEmail(userDto.getEmail())
                        && userDto.getPassword().equals(userDto.getConfirmPassword())) {

            User user = new User(
                    userDto.getFirstName(),
                    userDto.getLastName(),
                    userDto.getEmail(),
                    userDto.getPassword(),
                    userDto.getBio(),
                    getAttachment(userDto.getPhoto())
            );

            Role role = roleRepository.findByName("USER").orElse(null);
            if (role == null) {
                role = new Role("USER");
                roleRepository.save(role);
            }
            user.setRoles(List.of(role));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public String login(String email, String password,
                        HttpServletRequest req, Model model) {
        try {
            Optional<User> studentOptional = userRepository.findByEmail(email);
            if (studentOptional.isPresent()) {
                User user = studentOptional.get();
                if (user.getPassword().equals(password)) {
                    HttpSession session = req.getSession();
                    session.setAttribute("userId", user.getId());
                    if (user.getRoles().size() == 1) {
                        session.setAttribute("role", user.getRoles().get(0).getName());
                        return "courses";
                    } else {
                        List<Role> userRoles = roleRepository.findAllByUsers(user);
                        model.addAttribute("roles", userRoles);
                        return "select-role";
                    }
                }
            }
            model.addAttribute("msg", "email or password error");
        } catch (Exception e) {
            return "login";
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

    public void logOut(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
    }

    public List<User> getAuthors() {
        return userRepository.findAllByRoles(List.of(Objects.requireNonNull(roleRepository.findByName("MENTOR").orElse(null))));
    }

    private Attachment getAttachment(MultipartFile multipartFile) {
        try {
            if (multipartFile != null && multipartFile.getSize() != 0) {
                Attachment attachment = new Attachment();
                attachment.setBytes(multipartFile.getBytes());
                return attachment;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
