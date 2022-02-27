package com.app.controller;

import com.app.model.Lesson;
import com.app.model.LessonReview;
import com.app.model.Module;
import com.app.model.Video;
import com.app.repository.LessonRepository;
import com.app.repository.ModuleRepository;
import com.app.repository.UserRepository;
import com.app.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/lessons")
public class LessonController {
    private LessonRepository lessonRepository;
    private ModuleRepository moduleRepository;
    private UserRepository userRepository;
    private LessonService lessonService;

    @Autowired
    LessonController(LessonRepository lessonRepository, ModuleRepository moduleRepository, UserRepository userRepository, LessonService lessonService) {
        this.lessonRepository = lessonRepository;
        this.moduleRepository = moduleRepository;
        this.userRepository = userRepository;
        this.lessonService = lessonService;

    }

    @GetMapping("/{moduleId}")
    public String getLessonsByModuleId(@PathVariable UUID moduleId, Model model, HttpServletRequest req) {
        String role = userRepository.getRole(req);
        if (role != null) {
            model.addAttribute("lessons", lessonRepository.getLessonsByModuleId(moduleId));
            return "view-lessons";
        }
        return "redirect:/auth/login";
    }

    @GetMapping
    public String getLessonById(@RequestParam(value = "id") UUID id, Model model, HttpServletRequest req) {
        String role = userRepository.getRole(req);
        if (role != null) {
            model.addAttribute("lesson", lessonRepository.getById(id));
            model.addAttribute("comments", lessonService.getComment(id));
            model.addAttribute("photo", getPhoto(req));
            model.addAttribute("videos", lessonRepository.getVideoByLessonId(id));
            return "view-lesson";
        }
        return "redirect:/auth/login";
    }

    @PostMapping("/{moduleId}")
    public String saveModule(@PathVariable UUID moduleId, Lesson lesson, HttpServletRequest req) {
        String role = userRepository.getRole(req);
        if (role != null) {
            lesson.setModule(moduleRepository.getById(moduleId));
            lessonRepository.save(lesson);
            return "redirect:/lessons/" + moduleId;
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/edit")
    public String editLesson(@RequestParam UUID lessonId, Model model, HttpServletRequest req) {
        String role = userRepository.getRole(req);
        if (role != null) {
            model.addAttribute("lesson", lessonRepository.getById(lessonId));
            return "update-lesson";
        }
        return "redirect:/auth/login";
    }

    @PostMapping("/update")
    public String updateLesson(@RequestParam UUID moduleId, Lesson lesson, HttpServletRequest request) {
        String role = userRepository.getRole(request);
        if (role != null) {
            Module module = moduleRepository.getById(moduleId);
            lesson.setModule(module);
            lessonRepository.save(lesson);
            return "redirect:/lessons/" + module;
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/add")
    public String getForm(HttpServletRequest request) {
        String role = userRepository.getRole(request);
        if (role != null) {
            return "add-lesson";
        }
        return "redirect:/auth/login";
    }

    @PostMapping("/add/{moduleId}")
    public String addLesson(Lesson lesson, @PathVariable UUID moduleId, HttpServletRequest req) {
        String role = userRepository.getRole(req);
        if (role != null) {
            lessonRepository.save(lesson);
            return "redirect:/lessons/" + moduleId;
        }
        return "redirect:/auth/login";
    }

    @PostMapping("/addComment/{lessonId}")
    public String addComment(LessonReview comment, HttpServletRequest req, @PathVariable UUID lessonId) {
        lessonService.save(comment, req, lessonId);
        return "redirect:/lessons?id=" + comment.getLesson().getId() + "";
    }

    @ModelAttribute(value = "role")
    public String getModelId(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return (String) session.getAttribute("role");
    }

    @ModelAttribute(value = "userId")
    public UUID getUserId(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return (UUID) session.getAttribute("userId");
    }

    //    @ModelAttribute(value = "photo")
    public String getPhoto(HttpServletRequest req) {
        HttpSession session = req.getSession();
        UUID userId = (UUID) session.getAttribute("userId");
        return lessonService.getPhoto(userId);
    }

    @GetMapping("/del/{lesId}/{com}")
    public String deletePost(@PathVariable UUID lesId, @PathVariable UUID com) {
        lessonService.deleteComment(com);
        return "redirect:/lessons?id=" + lesId;
    }

    @GetMapping("/add/video/{lessonId}")
    public String addVideoUrl(@PathVariable UUID lessonId, Model model) {
        model.addAttribute("lessonId", lessonId);
        return "add-video";
    }

    @PostMapping("/add/video")
    public String addVideo(Video video, HttpServletRequest request, @RequestParam(value = "id") UUID lessonId) {
        lessonService.saveVideo(video, request, lessonId);
        return "redirect:/lessons?id=" + lessonId;
    }

    @GetMapping("/del/video/{videoId}")
    public String deleteVideo(@PathVariable UUID videoId) {
        UUID lessonId = lessonService.deleteVideo(videoId);
        return "redirect:/lessons?id=" + lessonId;
    }

}
