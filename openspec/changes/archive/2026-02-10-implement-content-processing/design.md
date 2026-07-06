## Context

La transcripción de entrada debe convertirse en una secuencia de "slides" definidas solo por su prompt de imagen. El LLM debe extraer y reorganizar la información (intro, nudo, desenlace) sin seguir el orden lineal del texto. La configuración global (prompt de sistema, orientación, estilo visual) se aplica a nivel de generación de imágenes en un paso posterior; en este cambio el LLM puede recibir parte de esa configuración para alinear tono y enfoque al generar los imagePrompts.

## Goals / Non-Goals

**Goals:**
- Integrar OpenAI para análisis y generación de estructura.
- Crear entidad Slide (id, projectId, order, imagePrompt, imageUrl, status).
- Persistir configuración global (systemPrompt, contentOrientation, visualStyle) para uso en generación de imágenes.
- Pipeline síncrono: analizar → estructurar → título → N slides (imagePrompts), guardar en BD.
- Exponer pasos para feedback (para que el frontend muestre progreso).

**Non-Goals:**
- Generación real de imágenes (Nano Banana) en este cambio.
- WebSocket/SSE en backend (se puede devolver el resultado síncrono; el frontend ya preparado puede mostrar pasos si el backend devuelve estados intermedios o se añade SSE después).
- Edición de slides en UI (solo modelo y datos).

## Decisions

### LLM
- **Proveedor:** OpenAI.
- **Modelo:** Configurable vía `openai.model` (default: `gpt-4o`).
- **Cliente:** Spring AI (spring-ai-openai) o OpenAI Java client; se elige Spring AI si versiones compatibles con Spring Boot 4.

### Entidad Slide
- **Campos:** id (UUID), projectId (FK), order (int), imagePrompt (TEXT), imageUrl (nullable String), status (Enum: PENDING, GENERATING, COMPLETED, ERROR).
- **Sin** title ni content en Slide; solo imagen (prompt + url).

### Configuración global (GlobalSettings)
- **Almacenamiento:** Tabla única con clave (por ejemplo una sola fila "default") o clave-valor. Campos: systemPrompt, contentOrientation, visualStyle.
- **Uso en este cambio:** Leer systemPrompt (y opcionalmente orientación) para el system prompt del LLM al analizar/generar. El estilo visual se usará solo en generación de imágenes (Nano Banana).
- **Valores por defecto:** Si no hay configuración, usar prompts por defecto (profesional, corporativo).

### Pipeline de contenido (orden)
1. Cargar GlobalSettings (o defaults).
2. **Paso 1:** LLM analiza transcripción → limpia ruido, extrae ideas.
3. **Paso 2:** LLM reorganiza en intro / nudo / desenlace y genera título del proyecto.
4. **Paso 3:** LLM genera N slides (1 ≤ N ≤ 12), cada una con un único campo textual: imagePrompt (descripción para la imagen).
5. Persistir: actualizar Project (título, status GENERATING → COMPLETED al terminar), crear N registros Slide (order, imagePrompt, status PENDING).

### Estructura narrativa
- No lineal respecto al orden del texto de entrada.
- Intro: contexto, objetivo.
- Nudo: puntos clave, acciones, datos relevantes.
- Desenlace: conclusiones, próximos pasos, resumen.

### Integración con Project
- Al crear proyecto desde frontend, se puede: crear Project en DRAFT con título provisional, luego llamar a un endpoint tipo `POST /api/projects/{id}/generate-content` con el content del proyecto, o incluir la generación en el flujo de creación. Decisión: endpoint dedicado `POST /api/projects/{id}/generate-content` que recibe el contenido ya guardado (o lo toma del proyecto), ejecuta el pipeline y actualiza proyecto + slides.
- Project.title se actualiza con el título generado por el LLM.

### Feedback síncrono
- Opción A: El endpoint devuelve al final con todos los slides creados; el frontend muestra un único loading "Generando presentación..." (ya lo hace).
- Opción B: Endpoint con SSE (Server-Sent Events) que emite eventos por paso (analizando, generando título, generando slide 1/12…). Para "ejecución síncrona con feedback por pasos", SSE es la opción más simple sin WebSocket.
- Decisión: Implementar SSE en este cambio para enviar eventos por paso; el frontend puede consumir el stream y mostrar el progreso, y al finalizar redirigir a la vista de detalle.

## Risks / Trade-offs

- **Risk:** Latencia del LLM (varios segundos por llamada).
  - **Mitigation:** Pipeline secuencial aceptable para MVP; SSE mantiene al usuario informado.
- **Risk:** Coste de tokens OpenAI.
  - **Mitigation:** Límite de 12 slides; prompts concisos; modelo configurable para usar modelos más baratos si se desea.
