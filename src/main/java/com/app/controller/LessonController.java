package com.app.controller;

import com.app.model.Lesson;
import com.app.model.Module;
import com.app.model.Role;
import com.app.repository.LessonRepository;
import com.app.repository.ModuleRepository;
import com.app.repository.UserRepository;
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

    @Autowired
    LessonController(LessonRepository lessonRepository, ModuleRepository moduleRepository, UserRepository userRepository) {
        this.lessonRepository = lessonRepository;
        this.moduleRepository = moduleRepository;
        this.userRepository = userRepository;

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

    @GetMapping()
    public String getLessonById(@RequestParam UUID lessonId, Model model, HttpServletRequest req) {
        String role = userRepository.getRole(req);
        if (role != null) {
            model.addAttribute("lesson", lessonRepository.getById(lessonId));
            return "view_lesson";
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

    @ModelAttribute(value = "role")
    public String getModelId(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return (String) session.getAttribute("moduleId");
    }


}
