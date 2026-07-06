package com.ayg.presentaciones.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ayg.presentaciones.dto.SlideResponse;
import com.ayg.presentaciones.model.Project;
import com.ayg.presentaciones.model.Slide;
import com.ayg.presentaciones.repository.ProjectRepository;
import com.ayg.presentaciones.repository.SlideRepository;
import com.ayg.presentaciones.service.ContentProcessingService;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ContentController {

    private final ContentProcessingService contentProcessingService;
    private final ProjectRepository projectRepository;
    private final SlideRepository slideRepository;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public ContentController(
            ContentProcessingService contentProcessingService,
            ProjectRepository projectRepository,
            SlideRepository slideRepository) {
        this.contentProcessingService = contentProcessingService;
        this.projectRepository = projectRepository;
        this.slideRepository = slideRepository;
    }

    @GetMapping(value = "/{id}/generate-content", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter generateContent(@PathVariable UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        SseEmitter emitter = new SseEmitter(600000L); // 10 minutes timeout

        executor.execute(() -> {
            try {
                contentProcessingService.processContent(project, (step) -> {
                    try {
                        emitter.send(SseEmitter.event()
                                .name("progress")
                                .data(step));
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                });

                emitter.send(SseEmitter.event()
                        .name("complete")
                        .data(Map.of("projectId", id.toString())));
                emitter.complete();
            } catch (Exception e) {
                try {
                    String errorMessage = "Error durante la generación";
                    if (e.getMessage() != null && e.getMessage().contains("insufficient_quota")) {
                        errorMessage = "Error: Tu cuenta de OpenAI no tiene créditos disponibles. Por favor, verifica tu plan y añade créditos.";
                    } else if (e.getMessage() != null && e.getMessage().contains("OpenAI")) {
                        errorMessage = "Error de OpenAI: " + e.getMessage();
                    }
                    
                    emitter.send(SseEmitter.event()
                            .name("error")
                            .data(errorMessage));
                } catch (IOException ioException) {
                    // No podemos enviar el error, solo completar con error
                }
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }

    @GetMapping("/{id}/slides")
    public ResponseEntity<List<SlideResponse>> getSlides(@PathVariable UUID id) {
        List<Slide> slides = slideRepository.findByProjectIdOrderByOrderAsc(id);
        List<SlideResponse> response = slides.stream()
                .map(slide -> new SlideResponse(
                        slide.getId(),
                        slide.getOrder(),
                        slide.getImagePrompt(),
                        slide.getImageUrl(),
                        slide.getStatus()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
