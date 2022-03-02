package com.app.springbootteamprolearningplatform.service;

import com.app.springbootteamprolearningplatform.dto.LessonCommentDto;
import com.app.springbootteamprolearningplatform.model.Module;
import com.app.springbootteamprolearningplatform.model.*;
import com.app.springbootteamprolearningplatform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class LessonService {

    private UserRepository userRepository;
    private LessonRepository lessonRepository;
    private VideoRepository videoRepository;
    private LessonCommentRepository lessonCommentRepository;
    private ModuleRepository moduleRepository;

    @Autowired
    LessonService(UserRepository userRepository,
                  LessonRepository lessonRepository,
                  VideoRepository videoRepository,
                  LessonCommentRepository lessonCommentRepository,
                  ModuleRepository moduleRepository) {
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.videoRepository = videoRepository;
        this.lessonCommentRepository = lessonCommentRepository;
        this.moduleRepository = moduleRepository;
    }


    public void save(LessonComment comment, HttpServletRequest req, UUID lessonId) {
        UUID userId = UUID.fromString(req.getSession().getAttribute("userId").toString());
        User user = userRepository.findById(userId).orElse(null);
        comment.setUser(user);

        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);
        comment.setLesson(lesson);

        comment.setPostedAt(LocalDateTime.now());
        lessonCommentRepository.save(comment);
    }

    public List<LessonCommentDto> getComment(UUID lessonId) {
        List<LessonComment> comments = lessonRepository.findLessonComments(lessonId);
        List<LessonCommentDto> commentDtoList = new ArrayList<>();
        for (LessonComment comment : comments) {
            User user = userRepository.findById(comment.getUser().getId()).orElse(null);
            String userFullName = user.getFirstName() + " " + user.getLastName();
            String attachment = getPhoto(user.getId());
            String date = comment.getPostedAt().format(DateTimeFormatter.ofPattern("yyyy-MMMM-dd hh:mm:ss a"));
            LessonCommentDto lessonCommentDto = new LessonCommentDto(
                    comment.getId(),
                    user.getId(),
                    userFullName,
                    comment.getBody(),
                    date,
                    attachment
            );
            commentDtoList.add(lessonCommentDto);
        }
        return commentDtoList;
    }

    public String getPhoto(UUID userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getAttachment() != null) {
                byte[] bytes = Base64.getEncoder().encode(user.getAttachment().getBytes());
                try {
                    return new String(bytes, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void deleteComment(UUID lessonId, UUID commentId) {
        lessonCommentRepository.deleteCommentByIdAndLessonId(commentId, lessonId);
    }

    public void saveVideo(Video video, UUID lessonId) {
        try {
            Optional<Lesson> lessonOptional = lessonRepository.findById(lessonId);
            if (lessonOptional.isPresent()) {
                Lesson lesson = lessonOptional.get();
                String fileName = "file_name=" + UUID.randomUUID();
                video.setFileName(fileName);
                video.setLesson(lesson);
                videoRepository.save(video);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public UUID deleteVideo(UUID videoId) {
        Optional<Video> videoOptional = videoRepository.findById(videoId);
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            UUID lessonId = video.getLesson().getId();
            videoRepository.delete(video);
            return lessonId;
        }
        return null;
    }

    public List<Lesson> getLessonsByModuleId(UUID moduleId) {
        return lessonRepository.findLessonsByModuleId(moduleId);
    }

    public Lesson findById(UUID id) {
        return lessonRepository.findById(id).orElse(null);
    }

    public void saveLesson(UUID moduleId, Lesson lesson) {
        Optional<Module> moduleOptional = moduleRepository.findById(moduleId);
        if (moduleOptional.isPresent()) {
            Module module = moduleOptional.get();
            lesson.setModule(module);
            lessonRepository.save(lesson);
        }
    }
}
