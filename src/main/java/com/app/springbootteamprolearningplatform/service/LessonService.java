package com.app.springbootteamprolearningplatform.service;

import com.app.springbootteamprolearningplatform.dto.LessonCommentDto;
import com.app.springbootteamprolearningplatform.model.Module;
import com.app.springbootteamprolearningplatform.model.*;
import com.app.springbootteamprolearningplatform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class LessonService {

    private UserRepository userRepository;
    private LessonRepository lessonRepository;
    private VideoRepository videoRepository;
    private LessonCommentRepository lessonCommentRepository;
    private ModuleRepository moduleRepository;
    private TaskRepository taskRepository;
    private ResourceLoader resourceLoader;
    private static final String FORMAT = "classpath:static/videos/%s";

    @Autowired
    LessonService(UserRepository userRepository,
                  LessonRepository lessonRepository,
                  VideoRepository videoRepository,
                  LessonCommentRepository lessonCommentRepository,
                  ModuleRepository moduleRepository,
                  TaskRepository taskRepository,
                  ResourceLoader resourceLoader) {
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.videoRepository = videoRepository;
        this.lessonCommentRepository = lessonCommentRepository;
        this.moduleRepository = moduleRepository;
        this.taskRepository = taskRepository;
        this.resourceLoader = resourceLoader;
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
        try {
            lessonCommentRepository.deleteByIdAndLessonId(commentId, lessonId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveVideo(MultipartFile video, UUID lessonId) {
        try {
            Optional<Lesson> lessonOptional = lessonRepository.findById(lessonId);
            if (lessonOptional.isPresent()) {
                ///////////// Get Absolute Path////////////
                File currentFilePath = new File(".");
                String absolutePath = currentFilePath.getAbsolutePath().replace(".", "src/main/resources/static/videos/");
                //////// CREATE VIDEO //////////////////
                Video sVideo = new Video();
                Lesson lesson = lessonOptional.get();
                sVideo.setLesson(lesson);
                sVideo.setOriginalFileName(video.getOriginalFilename());
                String s = UUID.randomUUID() + video.getOriginalFilename();
                sVideo.setFileName(s);
                sVideo.setFileLocation(absolutePath);
                sVideo.setContentType(video.getContentType());

                File file = new File(absolutePath + s);
                video.transferTo(file);
                videoRepository.save(sVideo);
                File resource = new ClassPathResource(absolutePath + sVideo.getFileName()).getFile();
                System.out.println(resource);
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
        return lessonRepository.findAllByModuleId(moduleId);
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

    public UUID deleteLesson(UUID lessonId) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonId);
        if (lessonOptional.isPresent()) {
            Lesson lesson = lessonOptional.get();
            UUID moduleId = lesson.getModule().getId();
            lessonRepository.delete(lesson);
            return moduleId;
        }
        return null;
    }

    public ResponseEntity<ByteArrayResource> downloadTask(UUID taskId) {
        Task task = taskRepository.findById(taskId).get();
        Attachment attachment = task.getAttachment();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + attachment.getFileName())
                .body(new ByteArrayResource(attachment.getBytes()));
    }

    public void saveTask(UUID lessonId, MultipartFile task, String question) {
        try {
            Task task1 = new Task();
            Attachment attachment = new Attachment();
            attachment.setBytes(task.getBytes());
            attachment.setFileName(task.getOriginalFilename());
            attachment.setContentType(task.getContentType());
            task1.setLesson(lessonRepository.findById(lessonId).get());
            task1.setQuestion(question);
            task1.setAttachment(attachment);
            taskRepository.save(task1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Task findTaskByid(UUID taskId) {
        return taskRepository.findById(taskId).get();
    }

    public Mono<Resource> getVideo(String title) {
        return Mono.fromSupplier(() -> resourceLoader
                .getResource(String.format(FORMAT, title)));
    }
}
