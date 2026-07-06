package com.ayg.presentaciones.service;

import com.ayg.presentaciones.model.GlobalSettings;
import com.ayg.presentaciones.model.Project;
import com.ayg.presentaciones.model.ProjectStatus;
import com.ayg.presentaciones.model.Slide;
import com.ayg.presentaciones.model.SlideStatus;
import com.ayg.presentaciones.repository.ProjectRepository;
import com.ayg.presentaciones.repository.SlideRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(ContentProcessingServiceImpl.class);
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    private final String apiKey;
    private final String model;
    private final GlobalSettingsService settingsService;
    private final ProjectRepository projectRepository;
    private final SlideRepository slideRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ImageService imageService;
    private final StorageService storageService;
    private final boolean imageGenerationEnabled;

    public ContentProcessingServiceImpl(
            @Value("${spring.ai.openai.api-key}") String apiKey,
            @Value("${spring.ai.openai.chat.options.model:gpt-4o}") String model,
            @Value("${app.image-generation.enabled:true}") boolean imageGenerationEnabled,
            GlobalSettingsService settingsService,
            ProjectRepository projectRepository,
            SlideRepository slideRepository,
            ImageService imageService,
            StorageService storageService) {
        this.apiKey = apiKey;
        this.model = model;
        this.imageGenerationEnabled = imageGenerationEnabled;
        this.settingsService = settingsService;
        this.projectRepository = projectRepository;
        this.slideRepository = slideRepository;
        this.objectMapper = new ObjectMapper();
        this.restTemplate = new RestTemplate();
        this.imageService = imageService;
        this.storageService = storageService;
    }

    /**
     * Procesa el contenido del proyecto utilizando una arquitectura de 2 fases con LLM.
     * 
     * FASE 1: Análisis de Transcripción
     * - Limpia ruido conversacional y extrae ideas principales
     * - Estructura el contenido de forma coherente
     * - Aplica configuración: systemPrompt, contentOrientation
     * 
     * FASE 2: Generación de Outline Completo
     * - Genera el título de la presentación
     * - Crea 10-15 slides con: título, contenido (español), description (inglés para imagen)
     * - Aplica configuración: systemPrompt, contentOrientation, visualStyle
     * - Reorganiza contenido en estructura no-lineal (Intro/Nudo/Desenlace)
     * 
     * @param project El proyecto a procesar con el contenido de transcripción
     * @param progressCallback Callback para reportar progreso al cliente (vía SSE)
     * @return Lista de imagePrompts (descriptions) generados para las slides
     */
    @Override
    @Transactional
    public List<String> processContent(Project project, Consumer<String> progressCallback) {
        log.info("========================================");
        log.info("Iniciando procesamiento de contenido para proyecto: {}", project.getId());
        log.info("Pipeline: 2-phase LLM (Analysis → Outline Generation)");
        log.info("Generación de imágenes: {}", imageGenerationEnabled ? "ACTIVADA" : "DESACTIVADA");
        log.info("========================================");
        
        GlobalSettings settings = settingsService.getSettings();
        
        // Update project status
        project.setStatus(ProjectStatus.GENERATING);
        projectRepository.save(project);

        try {
            // Step 1: Analyze transcript
            progressCallback.accept("Analizando transcripción...");
            String analyzedContent = analyzeTranscript(project.getContent(), settings);

            // Step 2: Generate complete outline (title + slides) - Nueva arquitectura de 2 llamadas
            progressCallback.accept("Generando outline de presentación...");
            SlideOutline outline = generateSlideOutline(analyzedContent, settings);
            
            // Extract and set title from outline
            project.setTitle(outline.title());
            projectRepository.save(project);

            // Step 3: Persist slides with complete data (title, content, imagePrompt)
            List<SlideData> slideDataList = outline.slides();
            int slideCount = slideDataList.size();
            
            for (SlideData slideData : slideDataList) {
                progressCallback.accept(String.format("Creando diapositiva %d/%d...", slideData.slideNumber(), slideCount));
                
                // Create slide with new constructor including title and content
                Slide slide = new Slide(
                    project.getId(),
                    slideData.slideNumber(),
                    slideData.title(),
                    slideData.content(),
                    slideData.description()  // description maps to imagePrompt
                );
                
                // Save initial slide
                slide = slideRepository.save(slide);
                
                // Build and log the image generation prompt (always, for debugging)
                String imagePrompt = buildImagePrompt(slideData.title(), slideData.content(), slideData.description(), settings.getVisualStyle());
                log.info("=== PROMPT PARA IMAGEN SLIDE {} ===", slideData.slideNumber());
                log.info("{}", imagePrompt);
                log.info("====================================");
                
                // Generate Image (if enabled)
                if (imageGenerationEnabled) {
                    try {
                        progressCallback.accept(String.format("Generando imagen %d/%d...", slideData.slideNumber(), slideCount));
                        
                        byte[] imageBytes = imageService.generateImage(
                            slideData.title(),
                            slideData.content(),
                            slideData.description(),
                            settings.getVisualStyle()
                        );
                        if (imageBytes != null) {
                            String filename = "project-" + project.getId() + "-slide-" + slide.getOrder() + ".png";
                            String imageUrl = storageService.store(imageBytes, filename);
                            
                            slide.setImageUrl(imageUrl);
                            slide.setStatus(SlideStatus.COMPLETED);
                            slideRepository.save(slide);
                            log.info("Imagen generada para slide {}: {}", slide.getOrder(), imageUrl);
                        } else {
                            log.warn("No se generó imagen para slide {}", slide.getOrder());
                        }
                    } catch (Exception e) {
                        log.error("Error generando imagen para slide {}", slide.getOrder(), e);
                        // Continue processing other slides
                    }
                } else {
                    log.info("Generación de imágenes desactivada - Slide {} permanece en estado PENDING", slide.getOrder());
                }
                
                log.info("Slide {} procesada completamente", slideData.slideNumber());
            }

            // Step 4: Mark project as completed
            project.setStatus(ProjectStatus.COMPLETED);
            projectRepository.save(project);
            
            progressCallback.accept("¡Presentación generada exitosamente!");

            log.info("========================================");
            log.info("Procesamiento completado exitosamente para proyecto: {}", project.getId());
            log.info("Título generado: {}", project.getTitle());
            log.info("Número de slides: {}", slideDataList.size());
            log.info("========================================");

            // Return list of image prompts for backward compatibility (if needed)
            return slideDataList.stream()
                .map(SlideData::description)
                .toList();
                
        } catch (Exception e) {
            log.error("========================================");
            log.error("ERROR en procesamiento de proyecto: {}", project.getId(), e);
            log.error("========================================");
            project.setStatus(ProjectStatus.ERROR);
            projectRepository.save(project);
            throw new RuntimeException("Error during content processing", e);
        }
    }

    private String analyzeTranscript(String content, GlobalSettings settings) {
        String systemPrompt = buildAnalysisSystemPrompt(settings);
        String userPrompt = buildAnalysisUserPrompt(content, settings);

        String analyzedContent = callOpenAI(systemPrompt, userPrompt);
        
        log.info("=== RESUMEN DE LA TRANSCRIPCIÓN ===");
        log.info("Contenido original (primeros 200 chars): {}", 
            content.length() > 200 ? content.substring(0, 200) + "..." : content);
        log.info("Resumen generado por IA:\n{}", analyzedContent);
        log.info("===================================");
        
        return analyzedContent;
    }

    /**
     * Genera el outline completo de la presentación (título + slides con toda su información).
     * Segunda fase del pipeline LLM - consolida título y slides en una sola llamada.
     */
    private SlideOutline generateSlideOutline(String analyzedContent, GlobalSettings settings) {
        String systemPrompt = buildOutlineGenerationSystemPrompt(settings);
        String userPrompt = buildOutlineGenerationUserPrompt(analyzedContent);

        String response = callOpenAI(systemPrompt, userPrompt).trim();
        
        log.info("=== RESPUESTA DE GENERACIÓN DE OUTLINE ===");
        log.info("Respuesta cruda de OpenAI (primeros 500 chars):\n{}", 
            response.length() > 500 ? response.substring(0, 500) + "..." : response);
        log.info("==========================================");
        
        return parseSlideOutlineFromJson(response);
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

    /**
     * Parsea el JSON devuelto por el LLM en la fase de outline generation.
     * Extrae el título de la presentación y todos los datos de las slides.
     */
    private SlideOutline parseSlideOutlineFromJson(String jsonResponse) {
        try {
            // Clean markdown code blocks if present (```json ... ``` or ``` ... ```)
            String cleanedJson = jsonResponse.trim();
            
            // Remove markdown code blocks
            if (cleanedJson.startsWith("```")) {
                // Remove opening ```json or ```
                cleanedJson = cleanedJson.replaceFirst("^```(?:json)?\\s*", "");
                // Remove closing ```
                cleanedJson = cleanedJson.replaceFirst("```\\s*$", "");
                cleanedJson = cleanedJson.trim();
            }
            
            log.info("JSON limpio para parsear:\n{}", cleanedJson);
            
            JsonNode root = objectMapper.readTree(cleanedJson);
            
            // Parse top-level title
            String presentationTitle = root.has("title") ? root.get("title").asText() : "Presentación";
            
            // Parse slides array
            JsonNode slidesNode = root.get("slides");
            if (slidesNode == null || !slidesNode.isArray()) {
                throw new RuntimeException("Missing or invalid 'slides' array in JSON response");
            }
            
            List<SlideData> slides = new ArrayList<>();
            for (JsonNode slideNode : slidesNode) {
                // Validate required fields
                if (!slideNode.has("slideNumber") || !slideNode.has("title") || 
                    !slideNode.has("content") || !slideNode.has("description")) {
                    log.warn("Slide missing required fields, skipping: {}", slideNode);
                    continue;
                }
                
                int slideNumber = slideNode.get("slideNumber").asInt();
                String title = slideNode.get("title").asText();
                String content = slideNode.get("content").asText();
                String description = slideNode.get("description").asText();
                
                slides.add(new SlideData(slideNumber, title, content, description));
            }
            
            // Ensure we don't exceed 15 slides
            if (slides.size() > 15) {
                log.warn("Número de slides ({}) excede el máximo (15). Truncando...", slides.size());
                slides = slides.subList(0, 15);
            }
            
            log.info("=== OUTLINE PARSEADO ===");
            log.info("Título de presentación: {}", presentationTitle);
            log.info("Número de slides: {}", slides.size());
            for (SlideData slide : slides) {
                log.info("Slide {}: title='{}', content length={}, description length={}", 
                    slide.slideNumber(), 
                    slide.title(),
                    slide.content().length(),
                    slide.description().length());
            }
            log.info("========================");
            
            return new SlideOutline(presentationTitle, slides);
            
        } catch (JsonProcessingException e) {
            log.error("Error parseando JSON de OpenAI. Respuesta recibida: {}", jsonResponse);
            throw new RuntimeException("Failed to parse LLM response as JSON: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error inesperado parseando outline. Respuesta: {}", jsonResponse, e);
            throw new RuntimeException("Failed to parse slide outline: " + e.getMessage(), e);
        }
    }

    /**
     * Construye el system prompt para la fase de análisis de transcripción.
     * Incluye una estructura base y opcionalmente las instrucciones del usuario.
     */
    private String buildAnalysisSystemPrompt(GlobalSettings settings) {
        StringBuilder systemPrompt = new StringBuilder();
        
        // Base del prompt (siempre presente)
        systemPrompt.append("Eres un experto en análisis de reuniones y creación de presentaciones. ");
        systemPrompt.append("Tu tarea es analizar transcripts de reuniones y extraer la información ");
        systemPrompt.append("más relevante para crear presentaciones impactantes.\n\n");
        
        // Inserción condicional: systemPrompt del usuario
        if (settings.getSystemPrompt() != null && !settings.getSystemPrompt().isEmpty()) {
            systemPrompt.append("Instrucciones adicionales del usuario: ")
                       .append(settings.getSystemPrompt())
                       .append("\n\n");
        }
        
        // Estructura de análisis (siempre presente)
        systemPrompt.append("Debes analizar el transcript y proporcionar:\n");
        systemPrompt.append("1. Los puntos clave de la reunión\n");
        systemPrompt.append("2. Las decisiones tomadas\n");
        systemPrompt.append("3. Los próximos pasos y responsables\n");
        systemPrompt.append("4. Datos o métricas mencionados\n");
        systemPrompt.append("5. Temas que requieren visualización\n\n");
        systemPrompt.append("Responde en español y de forma estructurada.");
        
        return systemPrompt.toString();
    }

    /**
     * Construye el user prompt para la fase de análisis de transcripción.
     * Incluye opcionalmente la orientación del contenido.
     */
    private String buildAnalysisUserPrompt(String content, GlobalSettings settings) {
        StringBuilder userPrompt = new StringBuilder();
        
        // Inserción condicional: contentOrientation
        if (settings.getContentOrientation() != null && !settings.getContentOrientation().isEmpty()) {
            userPrompt.append("Enfoque específico para esta presentación: ")
                      .append(settings.getContentOrientation())
                      .append("\n\n");
        }
        
        // Petición base (siempre presente)
        userPrompt.append("Analiza el siguiente transcript de reunión:\n");
        userPrompt.append(content);
        
        return userPrompt.toString();
    }

    /**
     * Construye el system prompt para la fase de generación de outline.
     * Incluye estructura base y estilo visual (deseado o por defecto).
     */
    private String buildOutlineGenerationSystemPrompt(GlobalSettings settings) {
        StringBuilder systemPrompt = new StringBuilder();
        
        systemPrompt.append("Eres un experto diseñador de presentaciones ejecutivas. ");
        systemPrompt.append("Tu tarea es crear un outline detallado para una presentación profesional ");
        systemPrompt.append("basándote en el análisis de una reunión.\n\n");
        systemPrompt.append("IMPORTANTE: Debes crear entre 10 y 15 diapositivas para cubrir adecuadamente todo el contenido.\n\n");
        systemPrompt.append("Para cada diapositiva debes proporcionar:\n");
        systemPrompt.append("1. title: Título de la diapositiva (corto, impactante)\n");
        systemPrompt.append("2. content: Contenido principal con los puntos clave en formato de bullets o párrafo\n");
        systemPrompt.append("3. description: Descripción COMPLETA para generar la imagen de la diapositiva\n\n");

        if (settings.getVisualStyle() != null && !settings.getVisualStyle().isEmpty()) {
            systemPrompt.append("Estilo visual deseado: ").append(settings.getVisualStyle()).append("\n\n");
        } else {
            systemPrompt.append("Estilo visual: Profesional, corporativo, moderno\n\n");
        }

        systemPrompt.append("CRÍTICO para el campo \"description\":\n");
        systemPrompt.append("- Debe ser una descripción en INGLÉS para el modelo de generación de imágenes\n");
        systemPrompt.append("- DEBE incluir el texto EXACTO que debe aparecer visible en la diapositiva (título y puntos clave)\n");
        systemPrompt.append("- Debe describir el layout, colores, tipografía y elementos visuales\n");
        systemPrompt.append("- Incluir instrucciones de diseño específicas\n\n");
        systemPrompt.append("Ejemplo de description correcto:\n");
        systemPrompt.append("\"A professional presentation slide with dark blue gradient background. Large white title text at top reading 'Q4 Financial Results'. Below the title, three bullet points in white text: '• Revenue increased 25%', '• New market expansion completed', '• Customer satisfaction at 95%'. Modern sans-serif typography (like Montserrat or Roboto). A subtle upward trending graph icon in the bottom right corner in teal color. Clean minimalist corporate design with plenty of white space.\"\n\n");
        systemPrompt.append("IMPORTANTE: Responde SOLO con un JSON válido sin markdown ni texto adicional. El formato debe ser:\n");
        systemPrompt.append("{\n");
        systemPrompt.append("  \"title\": \"Título de la presentación\",\n");
        systemPrompt.append("  \"slides\": [\n");
        systemPrompt.append("    {\n");
        systemPrompt.append("      \"slideNumber\": 1,\n");
        systemPrompt.append("      \"title\": \"Título de la diapositiva\",\n");
        systemPrompt.append("      \"content\": \"Contenido principal con puntos clave en español\",\n");
        systemPrompt.append("      \"description\": \"Complete English description for image generation including ALL text that must appear on the slide, visual elements, colors, layout, and typography\"\n");
        systemPrompt.append("    }\n");
        systemPrompt.append("  ]\n");
        systemPrompt.append("}");

        return systemPrompt.toString();
    }

    /**
     * Construye el user prompt para la fase de generación de outline.
     * Incluye el análisis previo y las instrucciones de esta ejecución.
     */
    private String buildOutlineGenerationUserPrompt(String analysis) {
        return String.format(
            "Crea un outline de presentación basándote en este análisis:\n\n%s\n\n" +
            "INSTRUCCIONES:\n" +
            "1. Genera entre 10 y 15 diapositivas según la cantidad y complejidad del contenido\n" +
            "2. La primera diapositiva debe ser una portada con el título principal\n" +
            "3. La última diapositiva debe ser un cierre/conclusiones/próximos pasos\n" +
            "4. Cada \"description\" DEBE incluir el texto exacto que debe aparecer visible en la imagen\n" +
            "5. Las descripciones deben ser en inglés y muy detalladas para generar imágenes de alta calidad",
            analysis
        );
    }
    
    /**
     * Construye el prompt simplificado para la generación de imágenes.
     * Replica la lógica de OpenAIImageService para que el log coincida con el prompt enviado a la API.
     */
    private String buildImagePrompt(String title, String content, String description, String visualStyle) {
        String intro = "Professional presentation slide design, 16:9 aspect ratio, high quality corporate design.";
        String style = (visualStyle != null && !visualStyle.isEmpty())
                ? visualStyle
                : "Modern minimalist style with clean typography.";
        String desc = (description != null) ? description : "";
        String closing = "Ultra high resolution, sharp text, professional business presentation.";
        return intro + " " + style + " " + desc + " " + closing;
    }
}
