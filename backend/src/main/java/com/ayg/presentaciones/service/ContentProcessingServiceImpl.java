package com.ayg.presentaciones.service;

import com.ayg.presentaciones.model.GlobalSettings;
import com.ayg.presentaciones.model.Project;
import com.ayg.presentaciones.model.ProjectStatus;
import com.ayg.presentaciones.model.Slide;
import com.ayg.presentaciones.repository.ProjectRepository;
import com.ayg.presentaciones.repository.SlideRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class ContentProcessingServiceImpl implements ContentProcessingService {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    private final String apiKey;
    private final String model;
    private final GlobalSettingsService settingsService;
    private final ProjectRepository projectRepository;
    private final SlideRepository slideRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public ContentProcessingServiceImpl(
            @Value("${spring.ai.openai.api-key}") String apiKey,
            @Value("${spring.ai.openai.chat.options.model:gpt-4o}") String model,
            GlobalSettingsService settingsService,
            ProjectRepository projectRepository,
            SlideRepository slideRepository) {
        this.apiKey = apiKey;
        this.model = model;
        this.settingsService = settingsService;
        this.projectRepository = projectRepository;
        this.slideRepository = slideRepository;
        this.objectMapper = new ObjectMapper();
        this.restTemplate = new RestTemplate();
    }

    @Override
    @Transactional
    public List<String> processContent(Project project, Consumer<String> progressCallback) {
        GlobalSettings settings = settingsService.getSettings();
        
        // Update project status
        project.setStatus(ProjectStatus.GENERATING);
        projectRepository.save(project);

        try {
            // Step 1: Analyze transcript
            progressCallback.accept("Analizando transcripción...");
            String analyzedContent = analyzeTranscript(project.getContent(), settings);

            // Step 2: Generate title and structured content
            progressCallback.accept("Generando título y estructura...");
            String title = generateTitle(analyzedContent, settings);
            project.setTitle(title);
            projectRepository.save(project);

            // Step 3: Generate image prompts (slides)
            progressCallback.accept("Generando prompts para diapositivas...");
            List<String> imagePrompts = generateImagePrompts(analyzedContent, settings);

            // Step 4: Persist slides
            int slideCount = imagePrompts.size();
            for (int i = 0; i < imagePrompts.size(); i++) {
                progressCallback.accept(String.format("Creando diapositiva %d/%d...", i + 1, slideCount));
                Slide slide = new Slide(project.getId(), i + 1, imagePrompts.get(i));
                slideRepository.save(slide);
            }

            // Step 5: Mark project as completed
            project.setStatus(ProjectStatus.COMPLETED);
            projectRepository.save(project);
            
            progressCallback.accept("¡Presentación generada exitosamente!");

            return imagePrompts;
        } catch (Exception e) {
            project.setStatus(ProjectStatus.ERROR);
            projectRepository.save(project);
            throw new RuntimeException("Error during content processing", e);
        }
    }

    private String analyzeTranscript(String content, GlobalSettings settings) {
        String systemPrompt = buildAnalysisSystemPrompt(settings);
        
        String userPrompt = String.format(
            "Analiza la siguiente transcripción y extrae las ideas principales. " +
            "Limpia ruido conversacional (muletillas, repeticiones, conversación informal). " +
            "Devuelve un resumen estructurado y limpio que capture la esencia del contenido.\n\n" +
            "Transcripción:\n%s",
            content
        );

        return callOpenAI(systemPrompt, userPrompt);
    }

    private String generateTitle(String analyzedContent, GlobalSettings settings) {
        String systemPrompt = settings.getSystemPrompt();
        
        String userPrompt = String.format(
            "Basándote en el siguiente contenido, genera un título ejecutivo corto (máximo 8 palabras) " +
            "que capture la esencia de la presentación.\n\n" +
            "Contenido:\n%s\n\n" +
            "Devuelve SOLO el título, sin comillas ni formato adicional.",
            analyzedContent
        );

        return callOpenAI(systemPrompt, userPrompt).trim();
    }

    private List<String> generateImagePrompts(String analyzedContent, GlobalSettings settings) {
        String systemPrompt = buildSlideGenerationSystemPrompt(settings);
        
        String userPrompt = String.format(
            "Basándote en el siguiente contenido analizado, genera una estructura de presentación " +
            "ejecutiva siguiendo el formato: Introducción, Nudo (puntos clave), Desenlace (conclusiones/acciones).\n\n" +
            "IMPORTANTE:\n" +
            "- Genera entre 1 y 12 diapositivas (adapta al contenido, no fuerces 12 si no es necesario).\n" +
            "- Para cada diapositiva, genera SOLO un 'imagePrompt': una descripción visual de lo que debe aparecer en la imagen " +
            "(sin incluir contexto global de estilo, solo el contenido específico de esa slide).\n" +
            "- La estructura NO debe ser lineal al texto original; reorganiza por relevancia e impacto.\n\n" +
            "Contenido:\n%s\n\n" +
            "Devuelve un JSON con este formato exacto:\n" +
            "{\n" +
            "  \"slides\": [\n" +
            "    {\"order\": 1, \"imagePrompt\": \"...descripción visual...\"},\n" +
            "    {\"order\": 2, \"imagePrompt\": \"...descripción visual...\"},\n" +
            "    ...\n" +
            "  ]\n" +
            "}\n\n" +
            "Devuelve SOLO el JSON, sin texto adicional.",
            analyzedContent
        );

        String response = callOpenAI(systemPrompt, userPrompt).trim();
        return parseImagePromptsFromJson(response);
    }

    private String callOpenAI(String systemPrompt, String userPrompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> request = new HashMap<>();
        request.put("model", model);
        request.put("temperature", 0.7);
        
        List<Map<String, String>> messages = new ArrayList<>();
        
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", systemPrompt);
        messages.add(systemMessage);
        
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", userPrompt);
        messages.add(userMessage);
        
        request.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                OPENAI_API_URL,
                entity,
                String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error calling OpenAI API", e);
        }
    }

    private List<String> parseImagePromptsFromJson(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode slidesNode = root.get("slides");
            
            List<String> prompts = new ArrayList<>();
            if (slidesNode != null && slidesNode.isArray()) {
                for (JsonNode slideNode : slidesNode) {
                    String imagePrompt = slideNode.get("imagePrompt").asText();
                    prompts.add(imagePrompt);
                }
            }
            
            // Ensure we don't exceed 12 slides
            if (prompts.size() > 12) {
                prompts = prompts.subList(0, 12);
            }
            
            return prompts;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse LLM response as JSON", e);
        }
    }

    private String buildAnalysisSystemPrompt(GlobalSettings settings) {
        return settings.getSystemPrompt() + "\n\n" +
               "Tu tarea específica: analizar transcripciones y extraer ideas principales de forma clara y estructurada.";
    }

    private String buildSlideGenerationSystemPrompt(GlobalSettings settings) {
        return settings.getSystemPrompt() + "\n\n" +
               "Orientación del contenido: " + settings.getContentOrientation() + "\n\n" +
               "Tu tarea: generar descripciones visuales (imagePrompts) para cada diapositiva de una presentación ejecutiva.";
    }
}
