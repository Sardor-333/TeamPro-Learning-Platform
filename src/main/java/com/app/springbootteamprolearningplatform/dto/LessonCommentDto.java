package com.app.springbootteamprolearningplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class LessonCommentDto {
    UUID id;
    UUID userId;
    String userFullName;
    String body;
    String postedAt;
    String attachment;
}
