# image-generation-prompt-mods Tasks

## 1. Backend - OpenAIImageService

- [x] 1.1 Replace buildDecoratedPrompt body with simplified format: intro + (visualStyle or "Modern minimalist style with clean typography") + description + closing
- [x] 1.2 Use only description and visualStyle parameters when building the prompt string (title and content unused but signature unchanged)

## 2. Backend - ContentProcessingServiceImpl

- [x] 2.1 Replace buildImagePrompt body with the same simplified format as OpenAIImageService so logged prompt matches API prompt
- [x] 2.2 Ensure log output "PROMPT PARA IMAGEN SLIDE X" shows the new single-sentence format

## 3. Verification

- [x] 3.1 Run content pipeline with image generation enabled and verify one generated image uses the new prompt (check logs)
- [x] 3.2 Run with visualStyle set in GlobalSettings and verify prompt contains visualStyle text; run with visualStyle empty and verify default style phrase appears in prompt

## 4. Support gpt-image-1 (model and parameters)

- [x] 4.1 Add app.image-generation.model, size, quality to application.properties with defaults for gpt-image-1
- [x] 4.2 Inject model, size, quality in OpenAIImageService and build request dynamically
- [x] 4.3 Send response_format only when model is dall-e-3 to fix "Unknown parameter: response_format" with gpt-image-1
- [x] 4.4 Use valid gpt-image-1 size (1536x1024) and quality (high) as defaults
