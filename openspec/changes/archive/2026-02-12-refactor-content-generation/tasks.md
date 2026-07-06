## 1. Database Schema - Slide Entity

- [x] 1.1 Add `title` field (String, TEXT, nullable) to `Slide.java` entity
- [x] 1.2 Add `content` field (String, TEXT, nullable) to `Slide.java` entity
- [x] 1.3 Add getter and setter methods for `title` field
- [x] 1.4 Add getter and setter methods for `content` field
- [x] 1.5 Update `Slide` constructor to accept `title` and `content` parameters (keep old constructor for compatibility)
- [x] 1.6 Verify H2 will auto-create columns on next startup with `ddl-auto=update`

## 2. Prompt Building Methods - Structured Base with Conditional Inserts

- [x] 2.1 Refactor `buildAnalysisSystemPrompt(GlobalSettings)` with base structure + conditional inserts
- [x] 2.2 In `buildAnalysisSystemPrompt()`, add base text: "Eres un experto en análisis de reuniones..."
- [x] 2.3 In `buildAnalysisSystemPrompt()`, conditionally insert "Instrucciones adicionales del usuario: {systemPrompt}" if not null
- [x] 2.4 In `buildAnalysisSystemPrompt()`, add structured analysis points (1-5) always
- [x] 2.5 Create new method `buildAnalysisUserPrompt(String content, GlobalSettings)` 
- [x] 2.6 In `buildAnalysisUserPrompt()`, conditionally insert "Enfoque específico: {contentOrientation}" if not null
- [x] 2.7 In `buildAnalysisUserPrompt()`, add base request "Analiza el siguiente transcript..." always
- [x] 2.8 Delete `buildSlideGenerationSystemPrompt()` method (will be replaced)
- [x] 2.9 Create new `buildOutlineGenerationSystemPrompt(GlobalSettings)` method with base structure
- [x] 2.10 In `buildOutlineGenerationSystemPrompt()`, add base text: "Eres un experto diseñador de presentaciones..."
- [x] 2.11 In `buildOutlineGenerationSystemPrompt()`, conditionally insert "Instrucciones adicionales: {systemPrompt}" if not null
- [x] 2.12 In `buildOutlineGenerationSystemPrompt()`, conditionally insert "Orientación del contenido: {contentOrientation}" if not null
- [x] 2.13 In `buildOutlineGenerationSystemPrompt()`, conditionally insert "Estilo visual deseado: {visualStyle}" if not null
- [x] 2.14 In `buildOutlineGenerationSystemPrompt()`, add structured instructions (title, content, description) always
- [x] 2.15 Create new method `buildOutlineGenerationUserPrompt(String analysis)`
- [x] 2.16 In `buildOutlineGenerationUserPrompt()`, format with analysis content and JSON structure example

## 3. LLM Call Refactoring - From 3 to 2 Calls

- [x] 3.1 Review current `processContent()` method to understand flow
- [x] 3.2 Delete `generateTitle()` method (title will come from outline)
- [x] 3.3 Rename `generateImagePrompts()` to `generateSlideOutline()`
- [x] 3.4 Update `generateSlideOutline()` signature to return a custom object (e.g., `SlideOutline`) instead of `List<String>`
- [x] 3.5 Create `SlideOutline` class or record with fields: `String title` and `List<SlideData> slides`
- [x] 3.6 Create `SlideData` class or record with fields: `int slideNumber`, `String title`, `String content`, `String description`
- [x] 3.7 Update `generateSlideOutline()` prompt to request JSON with complete structure (title + slides array)
- [x] 3.8 Update `generateSlideOutline()` to use `buildOutlineGenerationSystemPrompt()` instead of old method

## 4. JSON Parsing - Complex Structure

- [x] 4.1 Rename `parseImagePromptsFromJson()` to `parseSlideOutlineFromJson()`
- [x] 4.2 Update `parseSlideOutlineFromJson()` to return `SlideOutline` instead of `List<String>`
- [x] 4.3 Keep markdown stripping logic (```json blocks)
- [x] 4.4 Parse top-level "title" field from JSON
- [x] 4.5 Parse "slides" array from JSON
- [x] 4.6 For each slide in array, parse: `slideNumber`, `title`, `content`, `description`
- [x] 4.7 Map "description" field to `imagePrompt` for Slide entity
- [x] 4.8 Add error handling for missing/malformed JSON fields
- [x] 4.9 Add logging for parsed outline structure

## 5. Update ProcessContent Flow

- [x] 5.1 Update `analyzeTranscript()` to call both `buildAnalysisSystemPrompt()` and `buildAnalysisUserPrompt()`
- [x] 5.2 Update `analyzeTranscript()` to use separate system and user prompts in `callOpenAI()`
- [x] 5.3 Remove call to `generateTitle()` in `processContent()` method
- [x] 5.4 Update call from `generateImagePrompts()` to `generateSlideOutline()`
- [x] 5.5 Update `generateSlideOutline()` to call both `buildOutlineGenerationSystemPrompt()` and `buildOutlineGenerationUserPrompt()`
- [x] 5.6 Extract `title` from `SlideOutline` and set on `project`
- [x] 5.7 Update slide creation loop to use `SlideData` objects from outline
- [x] 5.8 When creating Slide entities, pass: `projectId`, `order` (from slideNumber), `title`, `content`, `imagePrompt` (from description)
- [x] 5.9 Update progress callback messages to reflect 2-call architecture (remove "Generando título...")
- [x] 5.10 Update logging to show complete slide data (title, content, imagePrompt)

## 6. DTOs and API Response (if needed)

- [x] 6.1 Check if `SlideDTO` exists in `backend/src/main/java/com/ayg/presentaciones/dto/`
- [x] 6.2 If SlideDTO exists, add `title` and `content` fields
- [x] 6.3 If SlideDTO exists, update mapping logic to include new fields
- [x] 6.4 Verify GET endpoints return new fields in JSON response

## 7. Testing and Verification

- [x] 7.1 Delete old database: `Remove-Item -Recurse -Force backend/data/`
- [x] 7.2 Recompile backend: `cd backend; mvn clean package`
- [x] 7.3 Start backend and verify H2 DDL adds `title` and `content` columns
- [x] 7.4 Check H2 Console to confirm schema: `http://localhost:8080/h2-console`
- [x] 7.5 Create test project with sample transcript via frontend
- [x] 7.6 Verify logs show only 2 LLM calls (analysis + outline)
- [x] 7.7 Verify logs show complete outline JSON parsing
- [x] 7.8 Verify slides in database have `title`, `content`, and `imagePrompt` populated
- [x] 7.9 Verify GET /api/projects/{id} endpoint returns slides with all fields
- [x] 7.10 Test with null configuration (all GlobalSettings fields empty)
- [x] 7.11 Test with partial configuration (only systemPrompt set)
- [x] 7.12 Test with full configuration (all GlobalSettings fields populated)
- [x] 7.13 Verify prompt building excludes null fields correctly

## 8. Documentation

- [x] 8.1 Update inline code comments for new methods
- [x] 8.2 Add JavaDoc for `SlideOutline` and `SlideData` classes
- [x] 8.3 Update README.md if needed (architecture section)
- [x] 8.4 Document the new 2-phase LLM pipeline in code comments
