## Context

El backend expone endpoints REST para gestión de proyectos. Se necesita una interfaz gráfica para que los usuarios puedan crear, visualizar y gestionar presentaciones generadas desde transcripciones de texto.

## Goals / Non-Goals

**Goals:**
- Crear una UI funcional y moderna que permita crear y listar proyectos.
- Implementar la estructura base para futuras funcionalidades (generación de slides, edición).
- Preparar la comunicación WebSocket para progreso en tiempo real (sin implementar backend todavía).
- Validar visualmente el flujo end-to-end.

**Non-Goals:**
- Implementar la generación real de slides (backend Content Processing pendiente).
- Implementar la edición avanzada de diapositivas.
- Sistema de autenticación/autorización.

## Decisions

### Stack Tecnológico
- **Build Tool**: Vite (rápido, moderno, HMR excelente).
- **Framework**: React 18 con TypeScript.
- **Styling**: Tailwind CSS (utility-first, rápido para prototipar).
- **State Management**: TanStack Query (React Query) para data fetching y cache. Sin Redux/Zustand por ahora.
- **Routing**: React Router v6 (estándar de facto).
- **WebSocket**: SockJS + STOMP client (preparado para Spring Boot STOMP).

### Arquitectura del Proyecto
```
/frontend
  /src
    /components     (componentes reutilizables)
    /pages          (vistas completas)
    /services       (API calls, WebSocket)
    /hooks          (custom hooks)
    /types          (TypeScript types)
    App.tsx
    main.tsx
  vite.config.ts
  tailwind.config.js
  tsconfig.json
```

### Routing
```
/               → Redirect a /crear
/crear          → Vista Crear (upload/paste text)
/proyectos      → Lista de proyectos (mosaico)
/proyectos/:id  → Detalle de proyecto
/ajustes        → Placeholder "Coming soon"
```

### Integración con Backend
- **Base URL**: `http://localhost:8080/api` (configurable via env).
- **CORS**: Ya configurado en el backend con `@CrossOrigin(origins = "*")`.
- **API Client**: Axios (para requests REST) + TanStack Query (para cache y estado).

### WebSocket (Preparación)
- **Client**: `@stomp/stompjs` + `sockjs-client`.
- **Endpoint**: `/ws` (a implementar en backend más adelante).
- **Uso**: Escuchar eventos de progreso durante generación (paso 1/5, 2/5, etc.).

### Vistas

#### Vista CREAR
- Textarea para pegar texto + botón "Subir archivo .txt".
- Validación: mínimo 100 caracteres.
- Botón "Generar presentación" deshabilitado si no cumple validación.
- Al hacer clic: por ahora crea un proyecto con estado DRAFT y redirige a detalle (sin generación real).

#### Vista PROYECTOS
- Mosaico de tiles (grid responsive).
- Cada tile muestra: título del proyecto (por ahora el título manual, más adelante generado por IA).
- Click en tile → navegación a `/proyectos/:id`.
- Orden: más reciente primero (el backend ya devuelve ordenado).

#### Vista DETALLE
- Título del proyecto.
- Botones: [Descargar PDF] (placeholder) y [Eliminar].
- Sección slides: placeholder "Generación pendiente" o "Coming soon".
- Mostrar texto original completo (para contexto).

#### Vista AJUSTES
- Mensaje "Coming soon" (se implementará con System Settings más adelante).

## Risks / Trade-offs

- **Risk**: Frontend listo pero sin funcionalidad de generación real.
    - **Mitigation**: Aceptable para MVP. Permite iterar en UI mientras se desarrolla backend.
- **Risk**: WebSocket configurado pero sin servidor.
    - **Mitigation**: Simplemente no conecta hasta que backend esté listo. No bloquea desarrollo.
