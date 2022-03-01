package com.app.springbootteamprolearningplatform.service;

import com.app.springbootteamprolearningplatform.model.Module;
import com.app.springbootteamprolearningplatform.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ModuleService {
    private ModuleRepository moduleRepository;

    @Autowired
    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public List<Module> getModulesByCourseId(UUID id) {
        return moduleRepository.findAllByCourseId(id);
    }

    public void getDelete(UUID id) {
        moduleRepository.deleteById(id);
    }

    public void save(Module module) {
        moduleRepository.save(module);
    }

    public Module getModuleById(UUID id) {
        return moduleRepository.getById(id);
    }
}
