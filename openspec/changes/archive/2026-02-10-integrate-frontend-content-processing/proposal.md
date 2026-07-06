# Proposal: Integración Frontend con Content Processing

## Why

El backend ya cuenta con el módulo de Content Processing completo que puede analizar transcripciones usando OpenAI GPT-4o, generar títulos ejecutivos y crear prompts para slides. Sin embargo, el frontend actual solo crea proyectos en estado DRAFT sin procesarlos.

Es necesario conectar el frontend con el endpoint `/api/projects/{id}/generate-content` para permitir a los usuarios ver el procesamiento en tiempo real y acceder a las slides generadas.

## What Changes

### Frontend Integration
- **Conexión con Backend**: Integrar el endpoint `POST /api/projects/{id}/generate-content` que usa SSE
- **Feedback en Tiempo Real**: Mostrar progreso paso a paso durante el procesamiento
- **Vista de Slides**: Mostrar las slides generadas con sus imagePrompts en la vista de detalle
- **Flujo Completo**: Al crear un proyecto, automáticamente iniciar la generación de contenido

### User Experience
- **Loading States**: Indicadores visuales durante cada paso del procesamiento
- **Progress Messages**: Mensajes claros como "Analizando transcripción...", "Generando título...", "Creando diapositiva 3/10..."
- **Error Handling**: Manejo de errores de red o procesamiento
- **Automatic Navigation**: Redirigir a la vista de detalle al completar

## Capabilities

### New Capabilities
- `frontend-sse-integration`: Capacidad de escuchar eventos SSE desde el backend
- `frontend-content-generation`: UI para iniciar y monitorear la generación de contenido
- `frontend-slide-display`: Visualización de slides con sus prompts (sin imágenes aún)

### Modified Capabilities
- `frontend-ui` (CrearPage): Añadir flujo automático de generación después de crear proyecto
- `frontend-ui` (ProyectoDetallePage): Mostrar slides generadas en lugar de placeholder

## Impacts

### User-Facing
- Los usuarios podrán ver el procesamiento de IA en tiempo real
- Feedback claro de cada paso del pipeline
- Visualización inmediata de las slides generadas (prompts)

### Technical
- Nuevo hook `useContentGeneration` para manejar SSE
- Nuevo servicio `contentApi.ts` para endpoints de generación
- Tipos TypeScript para `Slide` y estados de generación
- Componente `SlideCard` para mostrar cada slide con su prompt

### Testing
- Probar con transcripciones reales
- Verificar comportamiento de SSE
- Validar manejo de errores de OpenAI

## Out of Scope

- Generación de imágenes reales (Nano Banana) - queda para un cambio futuro
- Edición de imagePrompts - queda para un cambio futuro
- Regeneración individual de slides - queda para un cambio futuro
- Configuración de ajustes globales (página Ajustes) - queda para un cambio futuro
