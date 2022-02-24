package com.app.controller;

import com.app.model.Lesson;
import com.app.model.Module;
import com.app.model.Role;
import com.app.repository.LessonRepository;
import com.app.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@RequestMapping("/lessons")
public class LessonController {
    private LessonRepository lessonRepository;
    private ModuleRepository moduleRepository;

    @Autowired
    LessonController(LessonRepository lessonRepository, ModuleRepository moduleRepository){
        this.lessonRepository = lessonRepository;
        this.moduleRepository = moduleRepository;
    }

    @GetMapping("/{moduleId}")
    public String getLessonsByModuleId(@PathVariable UUID moduleId, Model model, HttpServletRequest req){
        Role role = lessonRepository.getRole(req);
        if (role != null) {
            model.addAttribute("lessons", lessonRepository.getLessonsByModuleId(moduleId));
            return "view-lessons";
        }
        return "redirect:/auth/login";
    }

    @GetMapping()
    public String getLessonById(@RequestParam UUID lessonId, Model model){
        model.addAttribute("lesson", lessonRepository.getById(lessonId));
        return "view_lesson";
    }

    @PostMapping("/{moduleId}")
    public String saveModule(@PathVariable UUID moduleId, Lesson lesson) {
        lesson.setModule(moduleRepository.getById(moduleId));
        lessonRepository.save(lesson);
        return "redirect:/lessons/"+moduleId;
    }

    @GetMapping("/edit")
    public String editLesson(@RequestParam UUID lessonId, Model model) {
        model.addAttribute("lesson", lessonRepository.getById(lessonId));
        return "update-lesson";
    }

    @PostMapping("/update")
    public String updateLesson(@RequestParam UUID moduleId, Lesson lesson){
        Module module = moduleRepository.getById(moduleId);
        lesson.setModule(module);
        lessonRepository.save(lesson);
        return "redirect:/lessons/"+module;
    }

    @GetMapping("/add")
    public String getForm(){
        return "add-lesson";
    }


}
