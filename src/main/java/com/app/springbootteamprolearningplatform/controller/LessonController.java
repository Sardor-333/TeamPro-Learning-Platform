package com.app.springbootteamprolearningplatform.controller;

import com.app.springbootteamprolearningplatform.model.Lesson;
import com.app.springbootteamprolearningplatform.model.LessonComment;
import com.app.springbootteamprolearningplatform.model.Task;
import com.app.springbootteamprolearningplatform.model.Video;
import com.app.springbootteamprolearningplatform.repository.RoleRepository;
import com.app.springbootteamprolearningplatform.repository.VideoRepository;
import com.app.springbootteamprolearningplatform.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/lessons")
public class LessonController {
    private LessonService lessonService;
    private RoleRepository roleRepository;
    private VideoRepository videoRepository;

    @Autowired
    LessonController(LessonService lessonService,
                     RoleRepository roleRepository,
                     VideoRepository videoRepository) {
        this.lessonService = lessonService;
        this.roleRepository = roleRepository;
        this.videoRepository = videoRepository;
    }

    // GET
    @GetMapping("/{moduleId}")
    public String getLessonsByModuleId(@PathVariable UUID moduleId, Model model, HttpServletRequest req) {
        String role = roleRepository.getRole(req);
        if (role != null) {
            model.addAttribute("lessons", lessonService.getLessonsByModuleId(moduleId));
            model.addAttribute("moduleId", moduleId);
            return "view-lessons";
        }
        return "redirect:/auth/login";
    }

    @GetMapping
    public String getLessonById(@RequestParam(value = "id") UUID id, Model model, HttpServletRequest req) {
        String role = roleRepository.getRole(req);
        if (role != null) {
            model.addAttribute("lesson", lessonService.findById(id));
            model.addAttribute("comments", lessonService.getComment(id));
            model.addAttribute("photo", getPhoto(req));
            model.addAttribute("videos", videoRepository.findAllByLessonId(id));
            return "view-lesson";
        }
        return "redirect:/auth/login";
    }

    @PostMapping("/{moduleId}")
    public String saveLesson(@PathVariable UUID moduleId, Lesson lesson, HttpServletRequest req) {
        String role = roleRepository.getRole(req);
        if (role != null) {
            lessonService.saveLesson(moduleId, lesson);
            return "redirect:/lessons/" + moduleId;
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/update/{lessonId}")
    public String editLesson(@PathVariable UUID lessonId, Model model, HttpServletRequest req) {
        String role = roleRepository.getRole(req);
        if (role != null) {
            Lesson lesson = lessonService.findById(lessonId);
            model.addAttribute("lesson", lesson);
            model.addAttribute("moduleId", lesson.getModule().getId());
            return "update-lesson";
        }
        return "redirect:/auth/login";
    }

    @PostMapping("/update")
    public String updateLesson(@RequestParam UUID moduleId, Lesson lesson, HttpServletRequest request) {
        String role = roleRepository.getRole(request);
        if (role != null) {
            lessonService.saveLesson(moduleId, lesson);
            return "redirect:/lessons/" + moduleId;
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/add/{moduleId}")
    public String getForm(@PathVariable UUID moduleId, HttpServletRequest request, Model model) {
        String role = roleRepository.getRole(request);
        if (role != null) {
            model.addAttribute("moduleId", moduleId);
            return "add-lesson";
        }
        return "redirect:/auth/login";
    }

    @PostMapping("/add/{moduleId}")
    public String addLesson(String theme, HttpServletRequest req, @PathVariable UUID moduleId) {
        String role = roleRepository.getRole(req);
        if (role != null) {
            Lesson lesson = new Lesson();
            lesson.setTheme(theme);
            lessonService.saveLesson(moduleId,lesson);
            return "redirect:/lessons/" + moduleId;
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/delete")
    public String deleteLesson(@RequestParam(name = "lessonId") UUID lessonId, HttpServletRequest request) {
        String role = roleRepository.getRole(request);
        if (role != null) {
            UUID moduleId = lessonService.deleteLesson(lessonId);
            return "redirect:/lessons/" + moduleId;
        }
        return "redirect:/auth/login";
    }

    @PostMapping("/addComment/{lessonId}")
    public String addComment(LessonComment comment, HttpServletRequest req, @PathVariable UUID lessonId) {
        lessonService.save(comment, req, lessonId);
        return "redirect:/lessons?id=" + comment.getLesson().getId() + "";
    }

    @GetMapping("/del/{lesId}/{com}")
    public String deletePost(@PathVariable UUID lesId, @PathVariable UUID com) {
        lessonService.deleteComment(lesId, com);
        return "redirect:/lessons?id=" + lesId;
    }

    @GetMapping("/add/video/{lessonId}")
    public String addVideoUrl(@PathVariable UUID lessonId, Model model) {
        model.addAttribute("lessonId", lessonId);
        return "add-video";
    }

    @PostMapping("/add/video")
    public String addVideo(Video video, @RequestParam(value = "id") UUID lessonId) {
        lessonService.saveVideo(video, lessonId);
        return "redirect:/lessons?id=" + lessonId;
    }

    @GetMapping("/del/video/{videoId}")
    public String deleteVideo(@PathVariable UUID videoId) {
        UUID lessonId = lessonService.deleteVideo(videoId);
        return "redirect:/lessons?id=" + lessonId;
    }


    @GetMapping("/uploadForm/{lessonId}")
    public String fileUploadForm(@PathVariable UUID lessonId, Model model){
        model.addAttribute("lessonId", lessonId);
        return "upload-file";
    }

    @PostMapping("/upload/{lessonId}")
    public String uploadFile(@PathVariable UUID lessonId, MultipartFile task, String question) {
        lessonService.saveTask(lessonId,task, question);
        return "redirect:/lessons?id=" + lessonId;
    }

    @GetMapping("/download/{taskId}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> downloadTask(@PathVariable UUID taskId){
        return lessonService.downloadTask(taskId);
    }

    @GetMapping("/show/task/{taskId}")
    public String getInfoTask(@PathVariable UUID taskId, RedirectAttributes redirectAttributes){
        Task taskByid = lessonService.findTaskByid(taskId);
        redirectAttributes.addFlashAttribute("infoTask", taskByid);
        return "redirect:/lessons?id=" + taskByid.getLesson().getId();
    }




    ////////////    MODEL ATTRIBUTE /////////////////////////
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
}
