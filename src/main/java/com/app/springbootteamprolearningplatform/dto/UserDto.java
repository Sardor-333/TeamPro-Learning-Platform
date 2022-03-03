package com.app.springbootteamprolearningplatform.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    UUID id;

    @NotBlank(message = "First name must contain characters!")
    String firstName;

    String lastName;

    String bio;

    @NotBlank(message = "Email must contain required characters!")
//    @Email(regexp = "\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b", message = "Email format is invalid!")
    String email;

    @NotBlank(message = "Password must contain characters!")
    @Size(min = 6, message = "Password length must be at least 6!")
    String password;

    @NotBlank(message = "Password confirmation must be the same!")
    @Size(min = 6, message = "Password confirmation must be the same!")
    String confirmPassword;

    MultipartFile photo;
}
