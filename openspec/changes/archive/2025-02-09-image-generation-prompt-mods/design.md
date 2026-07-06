## Context

The content pipeline generates slides with `title`, `content`, and `description` (imagePrompt). The image service currently builds a long prompt with separate blocks for TITLE, CONTENT/BULLET POINTS, VISUAL DESIGN INSTRUCTIONS, optional ADDITIONAL STYLE, and IMPORTANT REQUIREMENTS. The outline phase already produces a single English `description` per slide that is intended for image generation. Duplicating title and content in the prompt adds tokens and redundancy. GlobalSettings.visualStyle is optionally applied.

## Goals / Non-Goals

**Goals:**
- Define a single-sentence prompt format: fixed intro + (style or default) + description + fixed closing.
- Reduce prompt length and token usage for DALL-E 3.
- Keep behavior when visualStyle is set vs not set explicit and testable.

**Non-Goals:**
- Changing the ImageService interface or call sites; only the constructed string changes.
- Changing outline generation or slide entity; description remains the sole slide-specific input to the prompt.

## Decisions

1. **Prompt format**
   - **With visualStyle:** `Professional presentation slide design, 16:9 aspect ratio, high quality corporate design. {visualStyle}. {description}. Ultra high resolution, sharp text, professional business presentation.`
   - **Without visualStyle:** `Professional presentation slide design, 16:9 aspect ratio, high quality corporate design. Modern minimalist style with clean typography. {description}. Ultra high resolution, sharp text, professional business presentation.`
   - Rationale: One clear template, minimal branching, same structure for logging and API.

2. **Where to build the prompt**
   - Keep building in both `OpenAIImageService.buildDecoratedPrompt` (used for the actual API call) and `ContentProcessingServiceImpl.buildImagePrompt` (used for logging so the logged prompt matches what would be sent). Both are updated to the new format to avoid drift.

3. **Signature of buildDecoratedPrompt / buildImagePrompt**
   - Keep existing parameters `(title, content, description, visualStyle)` for compatibility. Only `description` and `visualStyle` are used in the new prompt; title and content are ignored. This avoids changing the interface and all call sites.

4. **Configurable image model and model-specific parameters**
   - Model, size, and quality SHALL be configurable via `app.image-generation.model`, `app.image-generation.size`, `app.image-generation.quality` (defaults: gpt-image-1, 1536x1024, high).
   - **gpt-image-1** (and gpt-image-1-mini, gpt-image-1.5): Does NOT accept `response_format`; the API returns base64 by default. Valid size: 1536x1024 (landscape), 1024x1024, 1024x1536. Quality: high, medium, low.
   - **dall-e-3**: Accepts `response_format: b64_json`. Size e.g. 1792x1024. Quality: standard, hd.
   - The service SHALL send `response_format` only when the model is dall-e-3 to avoid "Unknown parameter: response_format" with gpt-image-1.

## Risks / Trade-offs

- **Less explicit control in prompt:** We no longer repeat title/content in the prompt. Mitigation: The outline phase is responsible for including relevant text in `description`; the spec already requires the description to be suitable for image generation.
- **Single place of truth:** Prompt format is duplicated in two methods (service + content processor). Mitigation: Document the exact format in the spec and in code comments; consider extracting a small shared helper if duplication becomes a maintenance issue later.
