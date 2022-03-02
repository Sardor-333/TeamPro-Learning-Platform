package com.app.springbootteamprolearningplatform.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseCommentDto {
    UUID id;
    UUID userId;
    String base64;
    String body;
    String userFirstName;
    String userLastName;
    String postedTime;
}
