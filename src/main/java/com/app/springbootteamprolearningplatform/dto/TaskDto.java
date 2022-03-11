package com.app.springbootteamprolearningplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    String question;
    String firstName;
    String lastName;
    MultipartFile task;

    public TaskDto(String question, MultipartFile task) {
        this.question = question;
        this.task = task;
    }
}
