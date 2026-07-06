package com.ayg.presentaciones.controller;

import com.ayg.presentaciones.dto.ProjectCreateRequest;
import com.ayg.presentaciones.dto.ProjectResponse;
import com.ayg.presentaciones.dto.ProjectSummaryResponse;
import com.ayg.presentaciones.model.Project;
import com.ayg.presentaciones.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*") // Allow all origins for MVP
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectCreateRequest request) {
        Project project = projectService.createProject(request.title(), request.content());
        return new ResponseEntity<>(mapToProjectResponse(project), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProjectSummaryResponse>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        List<ProjectSummaryResponse> response = projects.stream()
                .map(this::mapToProjectSummaryResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable UUID id) {
        return projectService.getProjectById(id)
                .map(project -> ResponseEntity.ok(mapToProjectResponse(project)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    private ProjectResponse mapToProjectResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getContent(),
                project.getStatus(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }

    private ProjectSummaryResponse mapToProjectSummaryResponse(Project project) {
        return new ProjectSummaryResponse(
                project.getId(),
                project.getTitle(),
                project.getStatus(),
                project.getCreatedAt()
        );
    }
}
