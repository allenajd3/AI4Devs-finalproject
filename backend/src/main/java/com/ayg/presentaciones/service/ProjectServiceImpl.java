package com.ayg.presentaciones.service;

import com.ayg.presentaciones.model.Project;
import com.ayg.presentaciones.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional
    public Project createProject(String title, String content) {
        Project project = new Project(title, content);
        return projectRepository.save(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        return projectRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Project> getProjectById(UUID id) {
        return projectRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteProject(UUID id) {
        projectRepository.deleteById(id);
    }
}
