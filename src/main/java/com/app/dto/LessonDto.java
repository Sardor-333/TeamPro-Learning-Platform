package com.app.springbootteamprolearningplatform.dto;

import com.app.springbootteamprolearningplatform.model.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonDto {
    UUID id;
    String theme;
    Module module;
    List<String> tasks;
    List<Video> videos;

    public LessonDto(String theme, Module module, List<String> tasks, List<Video> videos) {
        this.theme = theme;
        this.module = module;
        this.tasks = tasks;
        this.videos = videos;
    }
}
