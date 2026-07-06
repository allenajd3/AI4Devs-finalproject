package com.ayg.presentaciones.service;

import com.ayg.presentaciones.model.Project;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface ProjectService {
    Project createProject(String title, String content);
    List<Project> getAllProjects();
    Optional<Project> getProjectById(UUID id);
    void deleteProject(UUID id);
}
