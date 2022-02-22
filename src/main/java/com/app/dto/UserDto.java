package com.app.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    UUID id;
    String firstName;
    String lastName;
    String bio;
    String email;
    String password;
    String confirmPassword;
    MultipartFile photo;

    public UserDto(String firstName, String lastName, String bio, String email, String password, String confirmPassword, MultipartFile photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.photo = photo;
    }

    public UserDto(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
