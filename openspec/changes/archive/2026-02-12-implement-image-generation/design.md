# Design: Image Generation Implementation

## Architecture

### Image Generation
- Use OpenAI DALL-E 3 via `v1/images/generations` endpoint.
- Parameters: `model: dall-e-3`, `size: 1024x1024`, `quality: standard`.
- Prompt: Use the `imagePrompt` generated in the outline phase.

### Storage
- Local file system storage in `backend/data/images`.
- Serve images via Spring Boot static resource handler mapping `/images/**` to `file:./backend/data/images/`.
- Filename format: `slide-{slideId}.png` or `project-{projectId}-slide-{order}.png`. Using UUID `slideId` ensures uniqueness.

### Workflow Integration
- In `ContentProcessingServiceImpl.processContent()`:
  1. Generate outline (text).
  2. Create `Slide` entities (status PENDING).
  3. Iterate through created slides:
     - Update SSE: `Generando imagen para diapositiva {order}/{total}...`
     - Call `imageService.generate(prompt)`.
     - Call `storageService.store(imageBytes, filename)`.
     - Update `Slide` entity with `imageUrl` and status `COMPLETED`.
     - Save `Slide`.
  4. Finish processing.

## Components

### `ImageService` (Interface)
- `byte[] generateImage(String prompt)`

### `OpenAIImageService` (Implementation)
- Uses `RestTemplate` to call OpenAI API.
- Handles errors (log and return null or placeholder bytes).

### `StorageService` (Interface)
- `String store(byte[] content, String filename)`
- `Resource load(String filename)`

### `LocalStorageService` (Implementation)
- Stores files in configured directory.
- Returns relative URL path (e.g., `/images/filename.png`).

### `WebConfig`
- Implements `WebMvcConfigurer`.
- Adds resource handler for `/images/**`.
