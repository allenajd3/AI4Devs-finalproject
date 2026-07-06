package com.ayg.presentaciones.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIImageService implements ImageService {

    private static final Logger logger = LoggerFactory.getLogger(OpenAIImageService.class);
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/images/generations";

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${app.image-generation.model:gpt-image-1}")
    private String imageModel;

    @Value("${app.image-generation.size:1536x1024}")
    private String imageSize;

    @Value("${app.image-generation.quality:high}")
    private String imageQuality;

    private final RestTemplate restTemplate;

    public OpenAIImageService() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public byte[] generateImage(String title, String content, String description, String visualStyle) {
        String decoratedPrompt = buildDecoratedPrompt(title, content, description, visualStyle);
        
        logger.info("=== GENERANDO IMAGEN CON {} ===", imageModel);
        logger.info("Prompt enviado:\n{}", decoratedPrompt);
        logger.info("======================================");

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", imageModel);
            requestBody.put("prompt", decoratedPrompt);
            requestBody.put("n", 1);
            requestBody.put("size", imageSize);
            requestBody.put("quality", imageQuality);
            // response_format only for DALL-E 3; gpt-image-1 returns b64 by default and does not accept this parameter
            if ("dall-e-3".equals(imageModel)) {
                requestBody.put("response_format", "b64_json");
            }

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_API_URL, request, Map.class);

            if (response.getBody() != null && response.getBody().containsKey("data")) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");
                if (!data.isEmpty()) {
                    String b64Json = (String) data.get(0).get("b64_json");
                    return java.util.Base64.getDecoder().decode(b64Json);
                }
            }
        } catch (RestClientException e) {
            logger.error("Error generating image from OpenAI: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during image generation", e);
        }
        
        return null;
    }
    
    /**
     * Builds a simplified single-sentence prompt for DALL-E 3.
     * Uses only description and visualStyle; title and content are kept in signature for compatibility.
     */
    private String buildDecoratedPrompt(String title, String content, String description, String visualStyle) {
        String intro = "Professional presentation slide design, 16:9 aspect ratio, high quality corporate design.";
        String style = (visualStyle != null && !visualStyle.isEmpty())
                ? visualStyle
                : "Modern minimalist style with clean typography.";
        String desc = (description != null) ? description : "";
        String closing = "Ultra high resolution, sharp text, professional business presentation.";
        return intro + " " + style + " " + desc + " " + closing;
    }
}
