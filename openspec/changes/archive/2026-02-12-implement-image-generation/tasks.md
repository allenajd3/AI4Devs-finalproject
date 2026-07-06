# implement-image-generation Tasks

## 1. Backend - Storage Implementation
- [x] 1.1 Create `StorageService` interface in `service` package
- [x] 1.2 Create `LocalStorageService` implementation
- [x] 1.3 Add `app.storage.location` property to `application.properties` (default: `data/images`)
- [x] 1.4 Create `WebConfig` class implementing `WebMvcConfigurer` to map `/images/**` to storage location
- [x] 1.5 Update `.gitignore` to exclude `backend/data/images` content but keep directory

## 2. Backend - Image Generation Implementation
- [x] 2.1 Create `ImageService` interface in `service` package
- [x] 2.2 Create `OpenAIImageService` implementation using `RestTemplate`
- [x] 2.3 Add DALL-E 3 API call logic (request DTO, response DTO)
- [x] 2.4 Add error handling to log failures and return null

## 3. Backend - Integration
- [x] 3.1 Inject `ImageService` and `StorageService` into `ContentProcessingServiceImpl`
- [x] 3.2 Update `processContent` loop to call `imageService.generate` for each slide
- [x] 3.3 Store generated image using `storageService.store`
- [x] 3.4 Update `Slide` entity with `imageUrl` and `status`
- [x] 3.5 Emit SSE events for image generation progress

## 4. Verification
- [x] 4.1 Verify `LocalStorageService` creates files
- [x] 4.2 Verify `/images/` URL serves files
- [x] 4.3 Verify `OpenAIImageService` calls DALL-E 3 successfully
- [x] 4.4 Verify end-to-end flow creates slides with image URLs

## 5. Feature Toggle
- [x] 5.1 Add `app.image-generation.enabled` property to `application.properties` (default: `true`)
- [x] 5.2 Inject `@Value("${app.image-generation.enabled:true}")` in `ContentProcessingServiceImpl`
- [x] 5.3 Add conditional check before image generation loop
- [x] 5.4 Log when image generation is skipped due to configuration
