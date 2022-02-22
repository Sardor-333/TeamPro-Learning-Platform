package com.app.service;

import com.app.dto.UserDto;
import com.app.model.Attachment;
import com.app.model.Role;
import com.app.model.User;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
                Role role = new Role("USER");
                List<Role> roles = new ArrayList<>(List.of(role));
                user.setRoles(roles);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}
