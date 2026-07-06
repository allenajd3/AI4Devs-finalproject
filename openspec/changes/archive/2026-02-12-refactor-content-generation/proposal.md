# Proposal: Refactorización de Generación de Contenido

## Why

La implementación actual de generación de contenido tiene varias limitaciones:

1. **Uso ineficiente de LLM**: Hace 3 llamadas separadas (análisis, título, slides) cuando podría hacerse en 2 llamadas más eficientes
2. **Configuración mal integrada**: `buildSlideGenerationSystemPrompt()` siempre concatena los campos de configuración incluso cuando son `null`, lo que genera prompts con "null" en el texto
3. **Datos incompletos en Slide**: Solo almacena `imagePrompt`, pero necesitamos también el título de la diapositiva y su contenido textual para futuras funcionalidades (edición, preview, etc.)
4. **Prompt de imágenes sin contexto visual**: El `imagePrompt` no incluye el estilo visual configurado (`visualStyle`), limitando la calidad de las imágenes futuras

Estos problemas dificultan:
- Editar slides individualmente
- Regenerar imágenes con diferente estilo
- Aprovechar la configuración global de forma efectiva
- Mantener un código limpio y mantenible

## What Changes

### Nueva Arquitectura de Llamadas LLM

**De 3 llamadas → 2 llamadas:**

**Llamada 1: Análisis de Transcripción**
- Sistema: `systemPrompt` (solo si no es null) + contexto de análisis
- Usuario: Petición de análisis + `contentOrientation` (solo si no es null)
- Resultado: Resumen estructurado y limpio del contenido

**Llamada 2: Generación de Outline Completo**
- Sistema: `systemPrompt` (solo si no es null) + `visualStyle` (solo si no es null) + contexto de generación de slides
- Usuario: Petición de outline basado en el análisis
- Resultado: JSON con estructura completa:
  ```json
  {
    "title": "Título de la Presentación",
    "slides": [
      {
        "slideNumber": 1,
        "title": "Título de la Diapositiva",
        "content": "Contenido en español con puntos clave",
        "description": "Complete English description for image generation..."
      }
    ]
  }
  ```

### Cambios en Base de Datos

**Entidad Slide** - Agregar campos:
- `title` (String, TEXT) - Título de la diapositiva
- `content` (String, TEXT) - Contenido/texto que aparece en la diapositiva

**Migración de datos**: Como usamos H2 con `ddl-auto=update`, las columnas se agregarán automáticamente. Los registros existentes tendrán estos campos en `null`.

### Cambios en Lógica de Negocio

1. **Eliminación de método**: `generateTitle()` - El título viene en el JSON de la llamada 2
2. **Simplificación de método**: `generateImagePrompts()` → `generateSlideOutline()`
3. **Mejora en validación de nulls**: Los métodos `buildXXXSystemPrompt()` solo incluyen campos de configuración si no son null
4. **Nuevo parseo JSON**: Adaptado para la estructura completa con title, content, description

### Alcance Específico

✅ **Incluido:**
- Refactorizar flujo a 2 llamadas LLM
- Modificar entidad Slide (agregar title, content)
- Actualizar lógica de prompts para manejar nulls correctamente
- Parsear y almacenar toda la información de slides
- Adaptar tests existentes

❌ **Excluido (para futuros cambios):**
- Generación de imágenes (sigue pendiente)
- Edición de slides desde frontend
- Regeneración individual de slides
- Validación avanzada de contenido

## Capabilities

### Modified Capabilities
- `content-processing`: Refactorización de llamadas LLM y lógica de prompts
- `slide-entity`: Ampliación de campos para soportar título y contenido

## Impact

### Archivos Backend Afectados

**Modelo:**
- `backend/src/main/java/com/ayg/presentaciones/model/Slide.java` - Agregar campos `title`, `content` y sus getters/setters

**Servicio:**
- `backend/src/main/java/com/ayg/presentaciones/service/ContentProcessingServiceImpl.java`:
  - Refactorizar `processContent()` para 2 llamadas en vez de 3
  - Eliminar método `generateTitle()`
  - Renombrar/refactorizar `generateImagePrompts()` → `generateSlideOutline()`
  - Actualizar `buildAnalysisSystemPrompt()` para manejar nulls
  - Actualizar `buildSlideGenerationSystemPrompt()` para manejar nulls e incluir `visualStyle`
  - Actualizar `parseImagePromptsFromJson()` → `parseSlideOutlineFromJson()` para parsear estructura completa

**DTO (si existe):**
- Actualizar DTOs de Slide para incluir `title` y `content`

### Base de Datos

- H2 agregará automáticamente las columnas `title` y `content` a la tabla `slides`
- Registros existentes tendrán estos campos en `null` (sin migración de datos necesaria)

### API

- Los endpoints GET que devuelven slides incluirán los nuevos campos `title` y `content`
- Retrocompatibilidad: frontend existente seguirá funcionando (los nuevos campos solo agregan información)

### Beneficios

1. **Eficiencia**: -1 llamada LLM = menos latencia y menor costo
2. **Mejor uso de configuración**: Los campos null no contaminan los prompts
3. **Datos más ricos**: Slides con título y contenido permiten futuras funcionalidades
4. **Código más limpio**: Menos métodos, mejor estructura
5. **Mejor calidad de imágenes futuras**: `description` incluirá `visualStyle` para generación de imágenes
