package com.app.springbootteamprolearningplatform.dto;

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
public class CourseDto {
    UUID id;
    String name;
    String description;
    UUID[] authorIds;
    UUID categoryId;
    MultipartFile img;
    Double price;
    Boolean status;

    public CourseDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CourseDto(String name, String description, UUID[] authorIds, UUID categoryId) {
        this.name = name;
        this.description = description;
        this.authorIds = authorIds;
        this.categoryId = categoryId;
    }

    public CourseDto(UUID id, String name, String description, Double price, Boolean status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
    }
}
