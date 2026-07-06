## Why

Es necesario definir el alcance funcional y técnico del MVP de "AyG Presentaciones IA". El objetivo es tener una hoja de ruta clara y especificaciones detalladas que guíen la implementación incremental de las funcionalidades clave: generación de presentaciones desde texto, gestión de proyectos y exportación a PDF.

## What Changes

Este cambio no introduce código productivo, sino que establece la **definición de especificaciones (specs)** para todo el MVP.

Se definirán los requisitos para:
- Flujos de usuario: Creación, Edición, Visualización y Gestión de Proyectos.
- Procesamiento de contenido: Uso de LLMs para resumir y estructurar presentaciones.
- Generación visual: Integración con Nano Banana para crear diapositivas como imágenes.
- Exportación: Generación de documentos PDF finales.
- Configuración del sistema: Ajustes globales de estilo y prompts.

## Capabilities

### New Capabilities
- `project-management`: Gestión del ciclo de vida de los proyectos (crear, listar, eliminar, persistir).
- `content-processing`: Lógica de negocio para transformar texto crudo en estructura de presentación (títulos, puntos clave, guiones).
- `image-generation`: Integración con Nano Banana para generar assets visuales por diapositiva.
- `pdf-assembly`: Motor de composición de PDF a partir de imágenes generadas.
- `user-interface`: Requisitos de UI/UX para dashboard, editor y visualizadores.
- `system-settings`: Gestión de configuración global (prompts, estilos).

### Modified Capabilities
- `backend-foundation`: Se extenderá para soportar las nuevas entidades y servicios (aunque esto se detallará en las nuevas specs, la capacidad base ya existe).

## Impact

- Creación de múltiples archivos de especificación en `openspec/specs/`.
- Servirá como base para futuros cambios de implementación (`implement-project-mgmt`, `implement-content-proc`, etc.).
