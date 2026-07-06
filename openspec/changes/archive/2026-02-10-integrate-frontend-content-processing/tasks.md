## 1. TypeScript Types

- [x] 1.1 Create `src/types/slide.ts` with Slide interface and SlideStatus type
- [x] 1.2 Export types from types barrel if needed

## 2. API Service Layer

- [x] 2.1 Create `src/services/contentApi.ts` with SSE connection logic
- [x] 2.2 Implement `generateContent(projectId, onProgress)` function using EventSource
- [x] 2.3 Implement `getSlides(projectId)` function using axios
- [x] 2.4 Add proper error handling and timeout management

## 3. Custom Hook: useContentGeneration

- [x] 3.1 Create `src/hooks/useContentGeneration.ts`
- [x] 3.2 Implement state management (isGenerating, progress, error)
- [x] 3.3 Implement `startGeneration(projectId)` method
- [x] 3.4 Add EventSource cleanup on unmount
- [x] 3.5 Handle error scenarios and connection failures

## 4. Progress UI Component

- [x] 4.1 Create `src/components/GenerationProgress.tsx` modal component
- [x] 4.2 Display list of progress messages with auto-scroll
- [x] 4.3 Add loading spinner/animation
- [x] 4.4 Style with Tailwind CSS (overlay, centered modal)
- [x] 4.5 Add error display with retry option

## 5. Slide Display Components

- [x] 5.1 Create `src/components/SlideCard.tsx` component
- [x] 5.2 Display slide order, imagePrompt, and status badge
- [x] 5.3 Add placeholder for image (grey box with icon)
- [x] 5.4 Style with Tailwind CSS (card, responsive)

## 6. Modify CrearPage

- [x] 6.1 Import and use `useContentGeneration` hook
- [x] 6.2 After successful project creation, call `startGeneration(projectId)`
- [x] 6.3 Show `GenerationProgress` component during generation
- [x] 6.4 Navigate to project detail on completion
- [x] 6.5 Handle errors gracefully with user feedback

## 7. Modify ProyectoDetallePage

- [x] 7.1 Create `useSlides` hook or use TanStack Query for slides
- [x] 7.2 Fetch slides with `GET /api/projects/{id}/slides`
- [x] 7.3 Replace "Coming soon" placeholder with slide grid
- [x] 7.4 Map slides to `SlideCard` components
- [x] 7.5 Handle empty state (no slides yet) with "Generar contenido" button
- [x] 7.6 Add loading skeletons while fetching slides

## 8. Testing & Verification

- [x] 8.1 Test SSE connection with real backend
- [x] 8.2 Verify progress messages display correctly
- [x] 8.3 Test error handling (network errors, backend errors)
- [x] 8.4 Verify automatic navigation after completion
- [x] 8.5 Test slide display with various slide counts (1-12)
- [x] 8.6 Verify responsive layout on different screen sizes
