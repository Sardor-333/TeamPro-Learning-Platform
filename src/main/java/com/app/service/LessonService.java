package com.app.service;

import com.app.dto.LessonReviewDto;
import com.app.model.*;
import com.app.repository.LessonRepository;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class LessonService {

    UserRepository userRepository;
    LessonRepository lessonRepository;

    @Autowired
    LessonService(UserRepository userRepository, LessonRepository lessonRepository){
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
    }

    public void save(LessonReview comment, HttpServletRequest req, UUID lessonId) {
        try {
            Object userId = req.getSession().getAttribute("userId");
            comment.setPostedAt(LocalDateTime.now());
            User user = userRepository.getById((UUID) userId);
            Lesson lesson = lessonRepository.getById(lessonId);
            comment.setLesson(lesson);
            comment.setUser(user);
            lessonRepository.reviewSave(comment);
        }catch (Exception e) {
        }
    }

    public List<LessonReviewDto> getComment(UUID lessonId){
        List<LessonReview> comments = lessonRepository.getComments(lessonId);
        List<LessonReviewDto> commentDto = new ArrayList<>();
        try {
            for (LessonReview comment : comments) {
                try {
                    User user = userRepository.getById(comment.getUser().getId());
                    String userFullName = user.getFirstName()+" "+user.getLastName();
                    byte[] bytes = user.getAttachment().getBytes();
                    byte[] encode = Base64.getEncoder().encode(bytes);
                    String attachment = new String(encode, "UTF-8");
                    String date = comment.getPostedAt().format(DateTimeFormatter.ofPattern("yyyy-MMMM-dd hh:mm:ss a"));
                    LessonReviewDto lessonReviewDto = new LessonReviewDto(
                            comment.getId(),
                            user.getId(),
                            userFullName,
                            comment.getBody(),
                            date,
                            attachment
                    );
                    commentDto.add(lessonReviewDto);
                }catch (Exception e){
                }
            }
        }catch (Exception e) {
            return null;
        }
        return commentDto;
    }

    public String getPhoto(UUID userId) {
        try {
            User user = userRepository.getById(userId);
            byte[] bytes = user.getAttachment().getBytes();
            byte[] encode = Base64.getEncoder().encode(bytes);
            return new String(encode, "UTF-8");
        }catch (Exception e) {
        }
        return "";
    }

    public void deleteComment(UUID commentId) {
        lessonRepository.deleteComment(commentId);
    }

    public void saveVideo(Video video, HttpServletRequest request, UUID lessonId) {
        try {
            Lesson lesson = lessonRepository.getById(lessonId);
            String fileName = "file_name="+UUID.randomUUID();
            video.setLesson(lesson);
            video.setFileName(fileName);
            lessonRepository.saveVideo(video);
        }catch (Exception e){
            return;
        }
    }

    public UUID deleteVideo(UUID videoId) {
        return lessonRepository.deleteVideo(videoId);
    }

    public void saveTask(UUID lessonId, MultipartFile file) {
        Task task = new Task();
        Lesson byId = lessonRepository.getById(lessonId);
        Attachment attachment = null;
        try {
            attachment = new Attachment(UUID.randomUUID()+"", file.getOriginalFilename(),file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        task.setLesson(byId);
        task.setAttachment(attachment);
        lessonRepository.saveTask(task, attachment);
    }
}
