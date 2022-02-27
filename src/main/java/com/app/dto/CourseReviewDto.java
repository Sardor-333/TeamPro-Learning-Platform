package com.app.dto;

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
public class CourseReviewDto {
    UUID id;
    String base64;
    String body;
    String userFirstName;
    String userLastName;
    String postedTime;

    public CourseReviewDto(String base64, String body, String userFirstName, String userLastName, String postedTime) {
        this.base64 = base64;
        this.body = body;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.postedTime = postedTime;
    }
}
