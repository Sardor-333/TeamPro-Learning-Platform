package com.app.springbootteamprolearningplatform.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseDto {

    // FROM UI
    UUID[] authorIds;
    UUID categoryId;
    MultipartFile img;

    // FOR DTO
    UUID id;
    String description;
    String name;
    Double price;
    Boolean status;
    String base64;
    List<String> authors;

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

    public CourseDto(UUID id, String name, String description, Double price, Boolean status, String base64) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.base64 = base64;
    }
}
