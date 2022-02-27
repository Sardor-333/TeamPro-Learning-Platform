package com.app.controller;

import com.app.model.Module;
import com.app.repository.CourseRepository;
import com.app.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/modules")
public class ModuleController {
    private final ModuleService moduleService;
    private final CourseRepository courseRepository;

    @Autowired
    public ModuleController(ModuleService moduleService, CourseRepository courseRepository) {
        this.moduleService = moduleService;
        this.courseRepository = courseRepository;
    }

    @RequestMapping("/{courseId}")
    public String getModules(@PathVariable UUID courseId, Model model) {
        List<Module> modulesByCourseId = moduleService.getModulesByCourseId(courseId);
        model.addAttribute("modules", modulesByCourseId);
        model.addAttribute("courseId", courseId);
        return "module-form";
    }

    @RequestMapping("/get-save-form")
    public String getEdit(@RequestParam(required = false, name = "id") UUID id,
                          @RequestParam(required = false, name = "courseId") UUID courseId, Model model) {
        if (id != null) {
            Module module = moduleService.getModuleById(id);
            model.addAttribute("modules", module);
            return "edit-module-form";
        } else {
            model.addAttribute("courseId", courseId);
            return "save-module-form";
        }
    }

    @RequestMapping("/save")
    public String getEdit(Module module, Model model) {
        String save = moduleService.save(module);
        model.addAttribute("msg", save);
        return "redirect:/modules/" + module.getCourse().getId();
    }

    @GetMapping("/delete/{id}")
    public String getDeleteModule(@PathVariable UUID id, Model model) {
        Module module = moduleService.getModuleById(id);
        UUID courseId = null;
        if (model != null) {
            courseId = module.getCourse().getId();
            moduleService.getDelete(id);
        }
        return "redirect:/modules/" + courseId;
    }

    @ModelAttribute(value = "role")
    public String getRole(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (String) session.getAttribute("role");
    }
}
