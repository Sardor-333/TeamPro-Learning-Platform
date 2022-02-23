package com.app.controller;

import com.app.model.Module;
import com.app.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/module")
public class ModuleController {
    private final ModuleService moduleService;

    @Autowired
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
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
            return "edit-form";
        }else {
        model.addAttribute("courseId", courseId);
        return "save-form";
        }
    }

    @RequestMapping("/save")
    public String getEdit(Module module, Model model) {
        String save = moduleService.save(module);
        model.addAttribute("msg", save);
        return "index";
    }

    @RequestMapping("/delete/{id}")
    public String getDelete(@PathVariable UUID id) {
        moduleService.getDelete(id);
        return "module-form";
    }
}
