## Why

El backend de "AyG Presentaciones IA" ya tiene funcionalidad básica (crear/listar proyectos), pero no hay forma de interactuar con él visualmente. Es necesario construir una interfaz de usuario mínima para validar el flujo completo, facilitar el desarrollo iterativo y poder demostrar progreso tangible.

## What Changes

Se implementará el **Frontend MVP** utilizando tecnologías modernas y escalables:
- Inicialización de un proyecto React con Vite.
- Configuración de TypeScript y Tailwind CSS.
- Implementación de 3 vistas principales: Crear, Proyectos (lista), y Detalle.
- Integración con los endpoints REST existentes del backend.
- Preparación de la infraestructura WebSocket para futura comunicación en tiempo real.
- Sistema de routing con React Router v6.

## Capabilities

### New Capabilities
- `frontend-ui`: Interfaz de usuario para gestionar proyectos de presentaciones.

### Modified Capabilities
- `project-management`: El frontend consumirá los endpoints REST ya implementados.

## Impact

- Creación del directorio `/frontend` en el monorepo.
- Nuevos archivos: configuración Vite, componentes React, estilos Tailwind.
- No afecta al backend existente (solo consume APIs).
