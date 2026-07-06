## Context

El MVP debe automatizar la generación de presentaciones ejecutivas desde texto. La arquitectura debe ser capaz de:
1. Recibir texto crudo.
2. Procesarlo con LLM para estructurar contenido (Introducción, Nudo, Desenlace, Puntos Clave).
3. Generar una imagen por slide usando Nano Banana.
4. Ensamblar las imágenes en un PDF final.

La persistencia de datos y archivos es clave.

## Goals / Non-Goals

**Goals:**
- Definir specs completas para desarrollo iterativo.
- Cubrir flujos de usuario (Crear, Listar, Detalle).
- Cubrir integraciones externas (LLM, Imagen, Storage).
- Cubrir exportación PDF.

**Non-Goals:**
- Implementar el código en este cambio.
- Definir detalles de infraestructura de despliegue (fuera de alcance MVP).
- Definir autenticación compleja (MVP es sin usuarios o un solo usuario implícito).

## Decisions

- **Arquitectura de Specs**: Se utilizará una estructura modular de specs para facilitar la implementación incremental:
  - `specs/project-management`: CRUD y estado de proyectos.
  - `specs/content-processing`: Lógica de LLM y estructura de slides.
  - `specs/image-generation`: Integración con Nano Banana.
  - `specs/pdf-assembly`: Composición final.
  - `specs/ui-ux`: Requisitos de interfaz.
  - `specs/system-settings`: Configuración global.

- **Modelo de Datos (Preliminar)**:
  - `Project`: ID, título, estado, fecha creación.
  - `Slide`: ID, proyecto_id, orden, texto_original, texto_resumido, prompt_imagen_generado, url_imagen, estado_generacion.
  - `Settings`: Globales (prompt sistema, estilo).

- **Integración Nano Banana**: Se diseñará para recibir un prompt detallado por slide y devolver una URL de imagen almacenada temporalmente o persistida en Supabase.

- **Generación PDF**: Se opta por ensamblar imágenes generadas por Nano Banana para mantener la fidelidad visual de lo que ve el usuario vs. lo que exporta.

## Risks / Trade-offs

- **Risk**: Latencia en la generación de imágenes (10-14 slides x tiempo de generación).
    - **Mitigation**: Generación asíncrona/paralela y feedback visual de progreso.
- **Risk**: Costo de LLM/Imagen.
    - **Mitigation**: MVP enfocado en funcionalidad, optimización de prompts posterior.
