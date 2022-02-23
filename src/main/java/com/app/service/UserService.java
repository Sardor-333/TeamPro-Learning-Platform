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
                if (photo != null) {
                    Attachment attachment = null;
                    try {
                        attachment = new Attachment();
                        attachment.setBytes(photo.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    user.setAttachment(attachment);
                }
                Role role = userRepository.getRole("USER");
                if (role==null) {
                    role = new Role("USER");
                }
                List<Role> roles = new ArrayList<>(List.of(role));
                user.setRoles(roles);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public String login(String email, String password, HttpServletRequest req, Model model) {
        try {
            User user = userRepository.getByEmail(email);
            if (user != null && user.getPassword() != null && user.getPassword().equals(password)) {
                HttpSession session = req.getSession();
                session.setAttribute("userId", user.getId());
                if(user.getRoles().size()==1) {
                    session.setAttribute("role", user.getRoles().get(0));
                    return "redirect:/courses";
                }else {
                    model.addAttribute("user", user);
                    return "select-role";
                }
            }
            else {
                model.addAttribute("msg", "email or password error");
            }
        }catch (Exception e){}
        return null;
    }

    public String setRole(String role, HttpServletRequest req) {
        HttpSession session1 = req.getSession(false);
        String userId = (String)session1.getAttribute("userId");
        if (userId != null && userId.length()>2) {
            HttpSession session = req.getSession();
            session.setAttribute("role",role);
            return "redirect:/courses";
        }
        return "redirect:/auth/login";
    }
}
