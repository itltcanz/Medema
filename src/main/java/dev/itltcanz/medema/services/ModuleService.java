package dev.itltcanz.medema.services;

import dev.itltcanz.medema.repositories.ModuleRepository;
import dev.itltcanz.medema.entity.Module;

public class ModuleService {
    public static Module getModule(String hostId) {
        return ModuleRepository.findModuleById(hostId);
    }

    public static void addModule(String moduleId, String moduleLocation) {
        if (ModuleRepository.findModuleById(moduleId) == null) {
            Module module = new Module(moduleId, moduleLocation);
            ModuleRepository.save(module);
        }
    }
}
