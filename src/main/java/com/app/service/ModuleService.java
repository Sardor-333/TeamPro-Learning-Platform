package com.app.service;

import com.app.model.Module;
import com.app.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ModuleService {
    private ModuleRepository moduleRepository;

    @Autowired
    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public List<Module> getModulesByCourseId(UUID id) {
        return moduleRepository.getByCourseId(id);
    }

    public void getDelete(UUID id) {
        moduleRepository.deleteById(id);
    }

    public String save(Module module){
        if (module.getId()!=null){
            moduleRepository.update(module);
            return "updated";
        }else {
            moduleRepository.save(module);
            return "saved";
        }
    }

    public Module getModuleById(UUID id) {
        return moduleRepository.getById(id);
    }
}
