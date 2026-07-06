## Why

El MVP necesita transformar la transcripción cruda en una estructura de presentación lista para generar imágenes. Sin este módulo, el usuario no puede pasar de "texto pegado" a "diapositivas definidas". El LLM analiza el contenido, lo reorganiza (intro, nudo, desenlace) y genera los prompts específicos por slide que luego alimentarán Nano Banana.

## What Changes

- Integración con **OpenAI** (modelo configurable en `application.properties`, por defecto GPT-4o).
- Nueva entidad **Slide**: id, projectId, order, imagePrompt, imageUrl, status.
- Entidad o configuración **GlobalSettings** para: prompt de sistema, orientación del contenido, estilo visual (usada como contexto en la generación de cada slide).
- Servicio **ContentProcessingService** que:
  1. Limpia y analiza la transcripción.
  2. Reorganiza el contenido en estructura intro / nudo / desenlace (no lineal).
  3. Genera un título corto para el proyecto.
  4. Genera entre 1 y 12 slides, cada una con un `imagePrompt` (texto para la imagen).
- Persistencia de slides asociadas al proyecto.
- Ejecución **síncrona** con feedback por pasos (para integrar con WebSocket/SSE en frontend).
- El `imagePrompt` de cada slide es solo el contenido de la diapositiva; el contexto global (config) se aplica al generar la imagen (en un cambio posterior con Nano Banana).

## Capabilities

### New Capabilities
- `content-processing`: Análisis de transcripción con LLM y generación de estructura de slides.
- `slide-entity`: Modelo de datos para diapositivas (solo imagen: imagePrompt, imageUrl, status).

### Modified Capabilities
- `project-management`: El proyecto tendrá título generado por IA y relación 1-N con Slide.
- `system-settings`: Definición del modelo de configuración global (prompt sistema, orientación, estilo visual).

## Impact

- Nuevas dependencias: cliente OpenAI (p. ej. Spring AI o OpenAI Java client).
- Nuevas entidades JPA: Slide, GlobalSettings (o equivalente).
- Nuevos endpoints o ampliación de `POST /api/projects` para disparar el pipeline de contenido.
- Configuración en `application.properties`: `openai.api-key`, `openai.model` (p. ej. gpt-4o).
