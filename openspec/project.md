# Project Context

## Purpose
**AyG Presentaciones IA** es una plataforma diseñada para automatizar la creación de presentaciones ejecutivas de alta calidad a partir de transcripciones de texto (reuniones, conferencias, documentos extensos).

El sistema utiliza Inteligencia Artificial para analizar el contenido aportado por el usuario, generar resúmenes ejecutivos y extraer los puntos clave más relevantes. El resultado final es un documento PDF formateado profesionalmente que respeta la identidad visual y los colores corporativos de la organización.

**Objetivos Principales:**
- **Eficiencia:** Reducir drásticamente el tiempo dedicado a sintetizar información y formatear diapositivas.
- **Calidad Ejecutiva:** Asegurar que el tono y el formato sean adecuados para audiencias de alto nivel.
- **Consistencia de Marca:** Garantizar que todas las salidas cumplan estrictamente con las guías de estilo corporativo.

## Tech Stack

### Backend (Core)
- **Language:** Java 21 (LTS)
- **Framework:** Spring Boot 4.0.x
- **Build Tool:** Maven
- **Database:** H2 (in-memory para MVP, PostgreSQL para producción)
- **AI Integration:** Spring AI 1.0.0-M6 con OpenAI GPT-4o (implementado)
- **Content Processing:** Análisis de transcripciones, generación de títulos, extracción de puntos clave y generación de prompts para diapositivas
- **PDF Generation:** Slides will be generated as individual images using Nano Banana via prompt engineering, then assembled into a final PDF document. This allows for rich, creative, and visually consistent slides.

### Frontend (Implementado)
- **Library:** React 18
- **Language:** TypeScript
- **Styling:** Tailwind CSS v4 (para facilitar la personalización de temas corporativos)
- **Build Tool:** Vite
- **Data Fetching:** TanStack Query (React Query)
- **Routing:** React Router v6
- **WebSocket:** STOMP + SockJS (para comunicación en tiempo real)
- **Real-time Updates:** Server-Sent Events (SSE) para feedback de generación

## Project Conventions

### Code Style
- **Java:** Google Java Style Guide. Uso de `Checkstyle` y `Spotless` recomendado.
- **Nombres:**
  - Clases: `PascalCase`
  - Métodos/Variables: `camelCase`
  - Constantes: `UPPER_SNAKE_CASE`
  - Paquetes: `lowercase` (`com.ayg.presentaciones...`)

### Architecture Patterns
- **Layered Architecture:** Controller -> Service -> Repository.
- **RESTful API:** Diseño de endpoints claros y semánticos.
- **DTO Pattern:** Separación estricta entre entidades de base de datos y objetos de transferencia de datos.
- **Configuration over Code:** Utilizar `application.properties` o `application.yml` para configuraciones.

### Testing Strategy
- **Unit Testing:** JUnit 5 + Mockito para lógica de negocio.
- **Integration Testing:** `@SpringBootTest` para verificar flujos completos y contratos de API.
- **Coverage:** Objetivo > 80% en lógica de negocio crítica (servicios de resumen y generación).

### Git Workflow
- **Main Branch:** `master` (código estable y desplegable).
- **Feature Branches:** `feature/nombre-descriptivo` o `fix/nombre-bug`.
- **Commits:** Conventional Commits (e.g., `feat: add pdf generation service`, `fix: correct summary length`).

## Domain Context
- **Transcripción:** Texto de entrada crudo, puede contener ruido o coloquialismos que deben filtrarse.
- **Puntos Clave (Key Points):** Ideas fuerza extraídas que resumen una sección o tema.
- **Identidad Corporativa:** Conjunto de reglas (colores hex, fuentes, logotipos) que deben aplicarse dinámicamente al generar el PDF.
- **Presentación Ejecutiva:** Documento conciso, orientado a la toma de decisiones, visualmente limpio.

## External Dependencies
- **LLM Provider:** API de OpenAI (GPT-4o) implementado vía Spring AI - utilizado para:
  - Análisis y limpieza de transcripciones
  - Generación de títulos ejecutivos
  - Generación de prompts de imagen para diapositivas (estructura no lineal: intro/nudo/desenlace)
- **Image Generation (Próximo):** Nano Banana - generará cada diapositiva como imagen basándose en los prompts.
- **PDF Engine (Próximo):** Motor de ensamblaje de imágenes en PDF.
