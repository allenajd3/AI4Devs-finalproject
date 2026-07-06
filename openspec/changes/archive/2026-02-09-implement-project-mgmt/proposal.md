## Why

Es fundamental para la aplicación "AyG Presentaciones IA" tener una gestión centralizada de las presentaciones. La entidad "Proyecto" es el núcleo que agrupa la transcripción, las diapositivas y el estado de generación. Sin esta gestión, el usuario no puede persistir ni recuperar su trabajo.

## What Changes

Se implementará el módulo de gestión de proyectos (`Project Management`).
- Creación de la entidad `Project` en el modelo de dominio.
- Implementación de un `ProjectRepository` (en memoria para MVP o persistente si se decide DB ahora).
- Implementación de un `ProjectService` con lógica de negocio (CRUD).
- Exposición de una API REST `/api/projects` para el frontend.
- Definición de los estados del proyecto: `DRAFT`, `GENERATING`, `COMPLETED`, `ERROR`.

## Capabilities

### New Capabilities
- `project-management`: Capacidad para crear, leer, listar y eliminar proyectos.

### Modified Capabilities
- `backend-foundation`: Se añade la dependencia de base de datos (H2 o PostgreSQL) y Starter Data JPA si es necesario.

## Impact

- Nuevas clases Java: `Project`, `ProjectRepository`, `ProjectService`, `ProjectController`.
- Configuración de base de datos en `application.properties`.
- Nuevos endpoints REST.
