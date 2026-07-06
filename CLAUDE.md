# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

### Backend (from `backend/`)
```bash
# Run dev server (requires JAVA_HOME set to Java 21+)
export JAVA_HOME='C:/JAVA/openjdk-24'
mvn spring-boot:run

# Build
mvn clean package

# Run all tests
mvn test

# Run a single test class
mvn test -Dtest=MyServiceTest

# Clean + install dependencies
mvn clean install -U
```

### Frontend (from `frontend/`)
```bash
npm install
npm run dev      # Dev server on http://localhost:5174
npm run build    # Production build to dist/
npm test         # Run tests
```

### Reset database
```bash
# Stop backend first, then:
rm -rf backend/data/
```

## Architecture

This is a **full-stack AI presentation generator**: users paste a transcript, the backend runs a 2-phase AI pipeline (GPT-4o) to produce structured slides with titles, content, and image prompts, then generates images via OpenAI's image API. Results stream in real-time to the frontend via SSE.

### Backend (`backend/src/main/java/com/ayg/presentaciones/`)

Layered: `Controller → Service → Repository`. Standard Spring Boot conventions.

- **Models:** `Project`, `Slide`, `GlobalSettings` (JPA entities). `GlobalSettings` is a singleton row (id=1).
- **Controllers:**
  - `ProjectController` — CRUD for projects (`/api/projects`)
  - `ContentController` — SSE endpoint `GET /api/projects/{id}/generate-content` and slide retrieval
  - `GlobalSettingsController` — GET/PUT `/api/settings`
- **Services:**
  - `ContentProcessingServiceImpl` — core pipeline: calls OpenAI chat API directly via `RestTemplate` (not Spring AI), parses JSON outline, creates `Slide` entities, then delegates to `ImageService`
  - `OpenAIImageService` implements `ImageService` — calls OpenAI image generation API (`gpt-image-1` by default, configured via `app.image-generation.*` properties)
  - `LocalStorageService` implements `StorageService` — saves generated images to `data/images/`
  - `GlobalSettingsService` — provides settings singleton; settings (systemPrompt, contentOrientation, visualStyle, darkMode) are injected into AI prompts
- **Database:** H2 file-based at `backend/data/presentaciones.mv.db`. Schema auto-updates via `ddl-auto=update`. H2 console at `/h2-console` (dev only).

### Content Generation Pipeline

`ContentProcessingServiceImpl.processContent()` runs in a background thread (ExecutorService in the controller):
1. **Phase 1 - Analysis:** Cleans and structures the raw transcript via GPT-4o chat completion
2. **Phase 2 - Outline:** Generates full presentation (10-15 slides) as JSON with `title`, `content` (Spanish), `imagePrompt` (English) per slide. Uses non-linear narrative structure (intro/conflict/resolution).
3. **Image Generation:** For each slide, calls `OpenAIImageService` to generate a 1536x1024 image, stores it locally, updates `Slide.imageUrl` and `Slide.status`.

Progress steps are sent as SSE `progress` events; `complete`/`error` events signal completion.

### Frontend (`frontend/src/`)

React 18 + TypeScript + Vite + TanStack Query + React Router v6.

- **Routing** (in `App.tsx`): `/crear`, `/proyectos`, `/proyectos/:id`, `/ajustes`
- **API layer:** `services/api.ts` (axios, baseURL `/api`) for projects and settings; `services/contentApi.ts` for SSE-based generation
- **Hooks:** `useProjects`, `useSlides`, `useContentGeneration` wrap TanStack Query calls
- **Dark mode:** `ThemeSync` component polls `GET /api/settings` and toggles the `dark` class on `document.documentElement`. Tailwind CSS v4 dark mode via class strategy.
- **Frontend proxies** `/api` and `/ws` to `http://localhost:8080` (see `vite.config.ts`)

### OpenSpec

Project uses [OpenSpec](https://openspec.dev) for structured change management. Specs live in `openspec/specs/`, completed changes in `openspec/changes/archive/`. Use `openspec view` to see current state. When contributing a new feature, follow: `opsx:new` → `opsx:apply` → `opsx:archive`.

## Key Configuration

`backend/src/main/resources/application.properties` contains:
- OpenAI API key (WARNING: currently committed in plaintext — move to env var)
- Image generation model/size/quality (`app.image-generation.*`)
- Storage path (`app.storage.location=data/images`)

To override without editing the file: set `OPENAI_API_KEY` env var and reference it as `${OPENAI_API_KEY}` in properties, or pass `-Dspring.ai.openai.api-key=...` to Maven.
