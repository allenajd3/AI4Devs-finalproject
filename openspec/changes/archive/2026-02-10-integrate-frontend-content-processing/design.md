# Design: Integración Frontend con Content Processing

## Decisions

### SSE (Server-Sent Events) Strategy
- **Decisión**: Usar la API nativa `EventSource` para conectar con el endpoint SSE
- **Razón**: Es el estándar web para SSE, no requiere dependencias adicionales
- **Implementación**: Hook personalizado `useContentGeneration` que encapsula la lógica de conexión
- **Alternativas descartadas**: WebSocket (overkill para comunicación unidireccional), polling (ineficiente)

### Content Generation Flow
1. Usuario crea proyecto (POST /api/projects) → Proyecto en estado DRAFT
2. Frontend automáticamente llama a `POST /api/projects/{id}/generate-content`
3. Backend responde con SSE stream de eventos:
   - Evento `progress`: Mensajes de progreso
   - Evento `complete`: Finalización con projectId
4. Frontend redirige a vista de detalle
5. Vista de detalle carga slides con `GET /api/projects/{id}/slides`

### TypeScript Types
Añadir tipos para las nuevas entidades:

```typescript
// src/types/slide.ts
export const SlideStatus = {
  PENDING: 'PENDING',
  GENERATING: 'GENERATING',
  COMPLETED: 'COMPLETED',
  ERROR: 'ERROR',
} as const;

export type SlideStatus = (typeof SlideStatus)[keyof typeof SlideStatus];

export interface Slide {
  id: string;
  order: number;
  imagePrompt: string;
  imageUrl: string | null;
  status: SlideStatus;
}
```

### API Service Layer
- **Archivo**: `src/services/contentApi.ts`
- **Funciones**:
  - `generateContent(projectId: string, onProgress: (msg: string) => void): Promise<void>` - Conecta con SSE
  - `getSlides(projectId: string): Promise<Slide[]>` - Obtiene slides de un proyecto

### Custom Hook: useContentGeneration
- **Responsabilidad**: Manejar el ciclo de vida de la generación de contenido
- **Estados**: `isGenerating`, `progress`, `error`
- **Métodos**: `startGeneration(projectId)`
- **Limpieza**: Cerrar EventSource al desmontar componente

### UI Components

#### CrearPage Modifications
- Después de crear proyecto exitosamente, automáticamente iniciar generación
- Mostrar modal/overlay con progreso en tiempo real
- Lista de mensajes de progreso (scroll automático al último)
- Al completar, redirigir a `/proyectos/{id}`

#### ProyectoDetallePage Modifications
- Cargar slides con `useQuery(['slides', projectId], () => getSlides(projectId))`
- Reemplazar placeholder "Coming soon" con grid de slides
- Cada slide muestra: número de orden, imagePrompt, estado
- Si no hay slides aún (proyecto en DRAFT), mostrar botón "Generar contenido"

#### New Component: SlideCard
- **Props**: `slide: Slide`
- **UI**: Card con borde, número de orden prominente, prompt en texto pequeño, badge de estado
- **Placeholder para imagen**: Icono o área gris donde irá la imagen

### Error Handling
- **Error de red durante SSE**: Mostrar mensaje de error, permitir reintentar
- **Error de OpenAI**: Backend envía evento de error, frontend lo captura y muestra
- **Timeout**: EventSource tiene timeout de 10 minutos (suficiente para procesamiento)

### Loading States
- Spinner durante creación de proyecto
- Overlay semitransparente durante generación con lista de pasos
- Skeleton loaders al cargar slides en vista de detalle

## Integration Points

### Backend Endpoints Used
- `POST /api/projects` - Crear proyecto (existente)
- `POST /api/projects/{id}/generate-content` - Generar contenido (nuevo, SSE)
- `GET /api/projects/{id}/slides` - Obtener slides (nuevo)
- `GET /api/projects/{id}` - Obtener proyecto (existente)

### Frontend Pages Modified
- `CrearPage.tsx` - Añadir flujo de generación automática
- `ProyectoDetallePage.tsx` - Mostrar slides reales

### Frontend Files Created
- `src/types/slide.ts` - Tipos TypeScript
- `src/services/contentApi.ts` - Servicios de API
- `src/hooks/useContentGeneration.ts` - Hook personalizado
- `src/components/SlideCard.tsx` - Componente de slide
- `src/components/GenerationProgress.tsx` - Modal de progreso

## Non-Functional Requirements

### Performance
- SSE debe mantener conexión estable durante todo el procesamiento
- UI debe ser responsive durante la generación (no bloquear)

### UX
- Feedback claro en cada paso
- Estimación visual de progreso (ej: "Generando diapositiva 5/10")
- Manejo graceful de errores con opciones de recuperación

### Accessibility
- Mensajes de progreso accesibles para lectores de pantalla
- Navegación por teclado en todos los componentes nuevos
