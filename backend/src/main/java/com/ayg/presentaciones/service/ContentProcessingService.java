package com.ayg.presentaciones.service;

import com.ayg.presentaciones.model.Project;
import java.util.List;
import java.util.function.Consumer;

public interface ContentProcessingService {
    /**
     * Process project content: analyze, structure, generate title and slides.
     * 
     * @param project The project to process
     * @param progressCallback Callback for progress updates (step description)
     * @return List of image prompts for slides
     */
    List<String> processContent(Project project, Consumer<String> progressCallback);
}
