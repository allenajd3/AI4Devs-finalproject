# Proposal: Implement Image Generation (DALL-E 3 + Local Storage)

## Why
To fulfill the MVP requirement of generating visual slides for presentations. Using DALL-E 3 leverages existing OpenAI integration, and local storage avoids external dependencies for the MVP.

## What Changes
### Backend
1.  **Image Service**: Create `OpenAIImageService` to call DALL-E 3 API via `RestTemplate`.
2.  **Storage Service**: Create `LocalStorageService` to save images to `backend/data/images` and a `WebConfig` to serve them via HTTP.
3.  **Content Pipeline**: Update `ContentProcessingServiceImpl` to call image generation for each slide after text generation, updating SSE progress.
4.  **Configuration**: Add properties for `storage.location` and ensure `openai.api-key` is used.

### Frontend
1.  **Image Display**: No major changes expected if `Slide.imageUrl` returns a valid URL, but need to verify `<img>` rendering with local backend URLs.

## Capabilities
- `image-generation` (OpenAI DALL-E 3)
- `file-storage` (Local)
- `content-processing` (Integration)

## Impact
- Increases processing time per project due to DALL-E 3 generation time.
- Requires local storage directory configuration.
