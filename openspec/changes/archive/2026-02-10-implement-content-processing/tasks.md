## 1. Configuration & Dependencies

- [x] 1.1 Add OpenAI client dependency (Spring AI OpenAI or openai-java) and configure in pom.xml
- [x] 1.2 Add application properties: openai.api-key, openai.model (default gpt-4o)

## 2. Global Settings

- [x] 2.1 Create GlobalSettings entity (id, systemPrompt, contentOrientation, visualStyle) with single-row strategy
- [x] 2.2 Create GlobalSettingsRepository and GlobalSettingsService with default values when empty
- [x] 2.3 Expose GET/PUT endpoint for global settings (e.g. /api/settings) for future Ajustes page

## 3. Slide Entity

- [x] 3.1 Create SlideStatus enum (PENDING, GENERATING, COMPLETED, ERROR)
- [x] 3.2 Create Slide entity (id, projectId, order, imagePrompt, imageUrl, status)
- [x] 3.3 Create SlideRepository with findByProjectIdOrderByOrderAsc
- [x] 3.4 Add OneToMany relationship from Project to Slide

## 4. Content Processing Service

- [x] 4.1 Create ContentProcessingService interface and implementation
- [x] 4.2 Implement step 1: call LLM to analyze transcript (clean noise, extract ideas)
- [x] 4.3 Implement step 2: call LLM to reorganize intro/nudo/desenlace and generate project title
- [x] 4.4 Implement step 3: call LLM to generate 1–12 imagePrompts (one per slide)
- [x] 4.5 Persist: update Project (title, status), create Slide records (order, imagePrompt, status PENDING)
- [x] 4.6 Load GlobalSettings (systemPrompt, contentOrientation) and pass to LLM context where appropriate

## 5. API & SSE

- [x] 5.1 Create endpoint POST /api/projects/{id}/generate-content (reads content from project, runs pipeline)
- [x] 5.2 Implement SSE (Server-Sent Events) to emit progress: analyzing, generating title, generating slide 1/N … completed
- [x] 5.3 Return or stream project ID and completion so frontend can redirect to project detail
- [x] 5.4 Add GET /api/projects/{id}/slides to list slides for a project

## 6. Prompts Design

- [x] 6.1 Define system prompt for transcript analysis (clean, extract, non-linear structure)
- [x] 6.2 Define prompt for title generation
- [x] 6.3 Define prompt for generating N imagePrompts (intro/nudo/desenlace, max 12)
- [x] 6.4 Ensure imagePrompt output is only slide-specific content (no global config in prompt text; config applied later at image generation)
