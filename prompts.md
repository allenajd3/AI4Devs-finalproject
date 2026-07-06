# prompts.md — Registro de uso de IA en AyG PresentacionesIA

> Documentación de los prompts más relevantes utilizados durante el desarrollo del proyecto, por sección. Para cada uno se indica la herramienta, el prompt y una nota de cómo se guió al asistente.

---

## Índice

1. [Diseño del producto y PRD](#1-diseño-del-producto-y-prd)
2. [Arquitectura del sistema](#2-arquitectura-del-sistema)
3. [Modelo de datos](#3-modelo-de-datos)
4. [Pipeline de generación IA (implementación)](#4-pipeline-de-generación-ia-implementación)
5. [Frontend y UI](#5-frontend-y-ui)
6. [Configuración y ajustes globales](#6-configuración-y-ajustes-globales)
7. [Proceso de desarrollo spec-driven con OpenSpec](#7-proceso-de-desarrollo-spec-driven-con-openspec)

---

## 1. Diseño del producto y PRD

### Prompt 1.1 — Definición del problema y propuesta de valor

**Herramienta:** Claude.ai (claude-sonnet-4)

**Prompt:**
```
Soy un profesional que asiste a muchas reuniones. Tras cada reunión necesito preparar 
una presentación ejecutiva para compartir con el equipo directivo. Actualmente me lleva 
2-3 horas por presentación.

Quiero construir una herramienta que automatice esto: pego la transcripción de la reunión 
y la IA genera la presentación completa.

Ayúdame a definir:
1. El problema concreto que resuelve
2. El público objetivo
3. Las funcionalidades Must-Have para el MVP
4. Las funcionalidades Should-Have para fases posteriores
5. Una propuesta de valor clara en 2-3 frases
```

**Nota:** Se proporcionó el contexto del problema real antes de pedir la estructura del PRD. El asistente generó la distinción Must-Have/Should-Have, que se ajustó manualmente para limitar el MVP a lo técnicamente realizable en el tiempo disponible.

---

### Prompt 1.2 — Historias de usuario con criterios de aceptación

**Herramienta:** Claude Code (claude-sonnet-4-6)

**Prompt:**
```
Basándote en las funcionalidades del MVP de AyG PresentacionesIA que ya hemos definido, 
genera las historias de usuario en formato "Como [rol], quiero [acción] para [beneficio]".

Para cada historia incluye:
- Criterios de aceptación concretos y verificables (no ambiguos)
- Clasificación Must-Have o Should-Have

El flujo principal E2E es:
1. Usuario pega transcripción
2. Sistema genera slides con IA (GPT-4o)
3. Sistema genera imágenes por slide
4. Usuario ve el resultado

Genera entre 5 y 7 historias.
```

**Nota:** Se proporcionó el flujo E2E explícitamente para que el asistente no inventara historias fuera de alcance. Los criterios de aceptación generados se revisaron y se añadieron los límites técnicos concretos (ej: mínimo 100 caracteres).

---

### Prompt 1.3 — Planificación en sprints

**Herramienta:** Claude Code (claude-sonnet-4-6)

**Prompt:**
```
Tengo las siguientes historias de usuario para el MVP:
[lista de US-001 a US-007]

Y el siguiente stack técnico:
- Backend: Spring Boot 4, Java 21, H2, OpenAI REST API
- Frontend: React 19, TypeScript, Vite, Tailwind CSS 4, TanStack Query

Organiza los tickets de trabajo en 3 sprints lógicos que reflejen 
cómo se construyó el proyecto de forma incremental:
- Sprint 1: Fundación (estructura base sin IA)
- Sprint 2: Integración IA (pipeline completo)
- Sprint 3: UX y configuración

Para cada ticket incluye: descripción, criterios de aceptación, historia de usuario referenciada.
```

**Nota:** Se especificó explícitamente la división en 3 sprints para que los tickets reflejaran el orden real de desarrollo, no un orden abstracto. Se ajustó manualmente el Sprint 2 para añadir el ticket de SSE que el modelo había omitido.

---

## 2. Arquitectura del sistema

### Prompt 2.1 — Diseño de arquitectura full-stack

**Herramienta:** Claude Code (claude-sonnet-4-6)

**Prompt:**
```
Quiero diseñar la arquitectura de un generador de presentaciones IA con estas 
restricciones:
- Backend: Spring Boot 4 (Java 21), sin Spring AI, llamadas directas a OpenAI REST API
- Frontend: React 19 + Vite + TanStack Query
- Base de datos: H2 file-based (no necesito PostgreSQL por ahora)
- La generación es lenta (puede tardar minutos): necesito feedback en tiempo real al usuario

¿Qué patrón arquitectónico me recomiendas para el feedback en tiempo real: 
WebSocket, SSE o long polling? Justifica la elección.

Después, dibuja la arquitectura completa con las capas del backend (Controller, Service, 
Repository) y cómo se conectan con el frontend.
```

**Nota:** Se preguntó primero por el patrón de comunicación en tiempo real como decisión clave antes de definir la arquitectura completa. El asistente recomendó SSE por ser más simple que WebSockets para el caso de uso unidireccional (servidor → cliente), lo que se adoptó directamente.

---

### Prompt 2.2 — Diseño del pipeline de 2 fases

**Herramienta:** Claude Code (claude-sonnet-4-6)

**Prompt:**
```
Estoy implementando un pipeline de generación de presentaciones con GPT-4o.
El input es una transcripción de reunión de longitud variable.

Propón un pipeline en 2 fases:
- Fase 1: ¿Qué debería hacer? ¿Qué le pido a GPT-4o? ¿Cuál debería ser el system prompt?
- Fase 2: ¿Cómo genero el outline completo en formato JSON? ¿Qué estructura JSON necesito?

Restricciones:
- El contenido de las slides debe estar en español
- Los image prompts deben estar en inglés (para generación de imágenes)
- Necesito 10-15 slides con estructura narrativa (intro/conflicto/resolución)
- La respuesta debe ser parseable robustamente (el modelo a veces devuelve markdown)
```

**Nota:** Se proporcionaron las restricciones de idioma y el formato esperado explícitamente. El asistente diseñó la estructura JSON con `slideNumber`, `title`, `content` (ES) y `description` (EN), que se implementó directamente. El parsing de markdown code blocks se añadió por sugerencia del asistente al anticipar el comportamiento del modelo.

---

### Prompt 2.3 — Patrones de diseño backend

**Herramienta:** Claude Code (claude-sonnet-4-6)

**Prompt:**
```
En el backend de Spring Boot necesito:
1. Intercambiar el proveedor de imágenes (OpenAI, Pollinations, etc.) sin cambiar el código cliente
2. Intercambiar el sistema de almacenamiento (local, S3, etc.) en el futuro
3. Tener una configuración global que sea un singleton en base de datos

¿Qué patrones de diseño usarías? Muéstrame las interfaces y cómo inyectarlas con Spring.
```

**Nota:** Se identificaron primero los puntos de variabilidad futura antes de pedir el patrón. El asistente propuso el patrón Strategy vía interfaces (`ImageService`, `StorageService`) y el patrón Single-Row para `GlobalSettings`. Esto se implementó directamente.

---

## 3. Modelo de datos

### Prompt 3.1 — Diseño del modelo de datos inicial

**Herramienta:** Claude Code (claude-sonnet-4-6)

**Prompt:**
```
Necesito el modelo de datos JPA para un sistema de generación de presentaciones.

Entidades necesarias:
1. Project: representa una presentación completa (tiene transcripción de entrada y slides de salida)
2. Slide: representa una diapositiva individual (tiene título, contenido, imagen)
3. GlobalSettings: configuración global del sistema (singleton)

Requisitos:
- Los IDs deben ser UUID (no autoincrement)
- Project tiene una relación one-to-many con Slide, con cascade delete
- Necesito campos de auditoría: createdAt, updatedAt (automáticos)
- Los estados deben ser enums (no strings libres)
- GlobalSettings siempre tiene id=1

Genera las entidades JPA con las anotaciones correctas.
```

**Nota:** Se especificó UUID vs autoincrement explícitamente para evitar colisiones en entornos distribuidos. El cascade delete fue una decisión de diseño propia que se incluyó en el prompt; el asistente generó `CascadeType.ALL` con `orphanRemoval=true`.

---

### Prompt 3.2 — DTOs y separación de capas

**Herramienta:** Claude Code (claude-sonnet-4-6)

**Prompt:**
```
Tengo las entidades JPA Project y Slide. No quiero exponer las entidades directamente 
en los endpoints REST.

Crea los DTOs necesarios para:
- Crear un proyecto (request)
- Responder con un proyecto completo (response)
- Responder con un resumen de proyecto para listados (sin el campo content que puede ser muy largo)
- Responder con una slide

Añade validaciones Jakarta Validation (@NotBlank, etc.) donde corresponda.
```

**Nota:** La distinción entre `ProjectResponse` (detalle completo) y `ProjectSummaryResponse` (para listados, sin el campo `content`) fue una sugerencia propia incluida en el prompt para optimizar el payload de la lista de proyectos.

---

### Prompt 3.3 — Migración y estrategia de base de datos

**Herramienta:** Claude.ai (claude-sonnet-4)

**Prompt:**
```
Estoy usando H2 file-based con ddl-auto=update para desarrollo.

Preguntas:
1. ¿Cuáles son los riesgos de usar ddl-auto=update en producción?
2. ¿Cómo migro a PostgreSQL cuando lo necesite? ¿Qué cambios requiere?
3. ¿Debería usar Flyway o Liquibase desde el principio?

El proyecto es un MVP, priorizando velocidad de desarrollo sobre robustez de producción.
```

**Nota:** Se usó para tomar una decisión técnica consciente, no para implementar código. El asistente confirmó que `ddl-auto=update` es válido para MVP, y recomendó añadir Flyway en la Entrega 2 cuando el esquema se estabilice. Esta decisión se documentó en el CLAUDE.md.

---

## 4. Pipeline de generación IA (implementación)

### Prompt 4.1 — System prompt para análisis de reuniones

**Herramienta:** Claude Code (claude-sonnet-4-6)

**Prompt:**
```
Necesito un system prompt para GPT-4o que analice transcripciones de reuniones 
de trabajo y extraiga la información relevante para construir una presentación ejecutiva.

El output debe incluir:
- Puntos clave discutidos
- Decisiones tomadas
- Próximos pasos y responsables
- Métricas o datos cuantitativos mencionados
- Temas que podrían ilustrarse visualmente

El system prompt debe:
- Ser directivo y concreto (no vago)
- Admitir que el usuario añada un system prompt personalizado adicional
- Funcionar bien con transcripciones en español e inglés
```

**Nota:** Se iteró el system prompt 3 veces hasta conseguir un output estructurado y consistente. La versión final incluye instrucciones explícitas sobre el formato de salida para facilitar el parsing en la Fase 2.

---

### Prompt 4.2 — System prompt para generación del outline JSON

**Herramienta:** Claude Code (claude-sonnet-4-6)

**Prompt:**
```
Necesito un system prompt para GPT-4o que genere un outline de presentación en formato JSON.

Restricciones del JSON:
{
  "title": "string - título ejecutivo de la presentación",
  "slides": [
    {
      "slideNumber": 1,
      "title": "string - título de la slide",
      "content": "string - contenido en español, puede incluir bullets con •",
      "description": "string - descripción detallada en inglés para generar imagen con IA"
    }
  ]
}

Requisitos narrativos:
- 10-15 slides con estructura Introducción / Desarrollo (nudo) / Resolución
- No lineal: evitar el formato clásico de 'agenda → puntos → conclusión'
- El campo 'description' debe describir una escena visual específica (no genérica)

El system prompt debe responder SOLO con JSON válido, sin texto adicional.
```

**Nota:** La instrucción "responder SOLO con JSON válido" se añadió después de observar que el modelo tendía a añadir explicaciones antes del JSON. Aun así, a veces devuelve markdown code blocks, por lo que se implementó un parser que elimina ` ```json ``` ` antes de parsear.

---

### Prompt 4.3 — Prompt de generación de imágenes

**Herramienta:** Claude Code (claude-sonnet-4-6)

**Prompt:**
```
Necesito construir el prompt para la API de generación de imágenes de OpenAI (gpt-image-1).

El input que tengo por slide es:
- description: descripción visual en inglés generada por GPT-4o
- visualStyle: estilo visual configurado por el usuario (ej: "corporativo moderno, tonos azules")

Construye una función en Java que combine estos inputs en un prompt de imagen 
de alta calidad. El prompt debe:
- Ser adecuado para presentaciones profesionales (no ilustraciones artísticas)
- Incluir el estilo visual del usuario si está definido
- Tener un prefix con contexto de "presentation slide image"
- Estar en inglés
```

**Nota:** El prefix `"Professional presentation slide image:"` fue una sugerencia del asistente que mejoró significativamente la calidad y consistencia de las imágenes generadas respecto a enviar solo la description.

---

## 5. Frontend y UI

### Prompt 5.1 — Componente de progreso SSE en React

**Herramienta:** Claude Code (claude-sonnet-4-6)

**Prompt:**
```
Necesito un componente React (TypeScript) que se suscriba a un endpoint SSE y 
muestre el progreso de un proceso largo.

El endpoint emite eventos con esta estructura:
- event: progress, data: { step: string, message: string, progress: number }
- event: complete, data: { projectId: string }
- event: error, data: { message: string }

Requisitos del componente:
- Modal con overlay oscuro
- Barra de progreso animada
- Mensaje del paso actual
- Manejo de errores con mensaje al usuario
- Al recibir 'complete': cierra el modal y navega a /proyectos/{id}
- Al desmontar el componente: cierra la conexión SSE

Usa TanStack Query y React Router v7.
```

**Nota:** Se especificó el ciclo de vida del EventSource en el prompt (especialmente el cleanup al desmontar) para evitar memory leaks. El asistente generó el hook `useContentGeneration` como abstracción separada, que se adoptó tal cual.

---

### Prompt 5.2 — Layout con sidebar colapsable y dark mode

**Herramienta:** Claude Code (claude-sonnet-4-6)

**Prompt:**
```
Necesito un Layout para una SPA en React con:
- Sidebar de navegación con iconos y etiquetas
- Botón para colapsar el sidebar (solo iconos visible cuando está colapsado)
- Dark mode: se activa añadiendo la clase 'dark' al elemento <html>
- Un componente ThemeSync que lea el dark mode de GET /api/settings y lo aplique

Stack: React 19 + Tailwind CSS 4 + React Router v7

El sidebar debe tener estas rutas:
- /crear (icono: +)
- /proyectos (icono: grid)
- /ajustes (icono: configuración)

Tailwind CSS 4 usa la variante 'dark:' con la estrategia 'class'.
```

**Nota:** Se especificó explícitamente Tailwind CSS 4 (no v3) porque la configuración de dark mode difiere. También se indicó el componente `ThemeSync` como separado para evitar re-renders del Layout completo al cambiar el tema.

---

### Prompt 5.3 — Integración de TanStack Query con operaciones CRUD

**Herramienta:** Claude Code (claude-sonnet-4-6)

**Prompt:**
```
Tengo una API REST en /api/projects. Necesito los hooks de TanStack Query v5 para:
1. Listar proyectos: useQuery
2. Obtener un proyecto por ID: useQuery
3. Crear un proyecto: useMutation (invalida la lista tras crear)
4. Eliminar un proyecto: useMutation (invalida la lista + navega a /proyectos tras eliminar)

El cliente Axios está configurado en services/api.ts con baseURL='/api'.

Usa la sintaxis de TanStack Query v5 (ya no existe isLoading, se usa isPending para mutations).
```

**Nota:** Se especificó la versión v5 explícitamente porque el modelo tenía tendencia a usar la API de v4. La corrección de `isLoading` → `isPending` para mutations fue una corrección necesaria que se incluyó en el prompt para ahorrar una iteración.

---

## 6. Configuración y ajustes globales

### Prompt 6.1 — GlobalSettings como singleton en base de datos

**Herramienta:** Claude Code (claude-sonnet-4-6)

**Prompt:**
```
Necesito implementar una entidad de configuración global en Spring Boot que:
- Siempre tenga exactamente una fila en base de datos (id=1)
- Se cree automáticamente con valores por defecto si no existe
- Sea accesible mediante GET /api/settings y PUT /api/settings
- Los campos son: systemPrompt, contentOrientation, visualStyle (TEXT), darkMode (boolean)

Implementa la entidad JPA, el repositorio, el servicio con el patrón findOrCreate y el controlador REST.
```

**Nota:** El patrón "findOrCreate" (buscar en repositorio, crear si no existe) fue la solución propuesta por el asistente para el singleton. Es más robusto que `@PostConstruct` porque funciona aunque se borre la fila manualmente.

---

### Prompt 6.2 — Inyección de settings en los prompts de IA

**Herramienta:** Claude Code (claude-sonnet-4-6)

**Prompt:**
```
En ContentProcessingServiceImpl tengo el pipeline de generación con 2 llamadas a GPT-4o.
Ahora necesito que los GlobalSettings del usuario modifiquen el comportamiento:

- systemPrompt: añadir al final del system prompt de AMBAS fases si no está vacío
- contentOrientation: añadir al user prompt de la FASE 1 (análisis) si no está vacío
- visualStyle: añadir al system prompt de la FASE 2 (outline) y al prompt de imagen si no está vacío

Muéstrame cómo modificar los prompts con esta lógica sin romper el comportamiento 
por defecto cuando los campos son null o vacíos.
```

**Nota:** Se diseñó la inyección de settings como aditiva (se añade al final del prompt) y solo cuando no está vacío, para no romper el comportamiento por defecto. Esta decisión de diseño se tomó conscientemente y se incluyó en el prompt.

---

### Prompt 6.3 — Comparativa antes/después de los prompts

**Herramienta:** Claude.ai (claude-sonnet-4)

**Contexto:** Tras observar que las presentaciones generadas tenían demasiado texto por slide y poca estructura narrativa.

**Prompt:**
```
Revisa estos dos system prompts para generación de presentaciones ejecutivas y 
dime cuál produce resultados de mayor calidad y por qué:

PROMPT A (original):
"Eres un experto en comunicaciones corporativas. Genera una presentación basada 
en el siguiente análisis de reunión..."

PROMPT B (mejorado):
"Eres un consultor de estrategia y comunicación ejecutiva de primer nivel. 
Tu objetivo es transformar información de reuniones en narrativas visuales 
que cautiven a audiencias directivas. 

Principios que guían tu trabajo:
- Menos es más: máximo 3 bullets por slide, cada uno con impacto real
- Narrativa no lineal: evita el formato clásico agenda→puntos→conclusión
- Cada slide debe tener una idea central que se sostiene sola..."

¿Qué ajustes harías al PROMPT B para mejorar aún más la calidad?
```

**Nota:** Esta comparativa surgió tras la primera demo del MVP, donde el output era correcto pero demasiado genérico. El asistente identificó 3 mejoras concretas al PROMPT B (añadir contexto de audiencia, especificar el tono y pedir density de información por slide) que se incorporaron directamente.

---

## 7. Proceso de desarrollo spec-driven con OpenSpec

Toda la implementación se ha realizado siguiendo el flujo **spec-driven** de [OpenSpec](https://github.com/Fission-AI/OpenSpec) con Claude Code y Cursor. Las skills y comandos (`/opsx:new`, `/opsx:ff`, `/opsx:apply`, `/opsx:archive`) están versionados en `.claude/` y `.cursor/`. Cada funcionalidad siguió el mismo ciclo:

1. **Proposal** — describir el cambio en lenguaje natural; el agente genera `proposal.md` (why / what changes / capabilities).
2. **Design + Specs + Tasks** — el agente deriva el diseño técnico, las delta-specs por capacidad y la lista de tareas (< 2h cada una).
3. **Apply** — implementación guiada tarea a tarea, revisando el código generado en cada paso.
4. **Archive** — al validar la funcionalidad, el change se archiva y las specs principales se sincronizan.

Los 14 changes completados están en [`openspec/changes/archive/`](openspec/changes/archive/), y las specs resultantes (estado actual del sistema, 15 capacidades) en [`openspec/specs/`](openspec/specs/).

### Prompt 7.1 — Definición de las specs del MVP

**Herramienta:** Claude Code + OpenSpec (`/opsx:new`)

**Prompt:**
```
/opsx:new Quiero definir las specs del MVP a partir del PRD (docs/PRD-AYGPresentaciones.md).
Las capacidades que veo son: procesamiento de contenido con LLM, generación de imágenes,
ensamblado en PDF, gestión de proyectos, configuración global del sistema y la UI.
No implementes nada todavía: solo las specs con sus requisitos y escenarios,
para que sirvan de base a los changes de implementación.
```

**Nota:** Este primer change (`define-mvp-specs`) estableció el contrato del sistema completo antes de escribir una sola línea de código. Los changes posteriores referencian estas capacidades como "modified" o añaden nuevas.

### Prompt 7.2 — Change de implementación del pipeline de contenido

**Herramienta:** Claude Code + OpenSpec (`/opsx:ff`)

**Prompt:**
```
/opsx:ff Implementar el procesamiento de contenido: el MVP necesita transformar la
transcripción cruda en una estructura de presentación. Integración con OpenAI
(modelo configurable en application.properties, por defecto GPT-4o). Nueva entidad
Slide (id, projectId, order, imagePrompt, imageUrl, status) y GlobalSettings para
system prompt, orientación del contenido y estilo visual. El servicio debe:
limpiar y analizar la transcripción, reorganizar el contenido en estructura
intro/nudo/desenlace (no lineal), generar un título corto y entre 1 y 12 slides.
Ejecución síncrona con feedback por pasos para integrarlo luego con SSE.
```

**Nota:** El comando `ff` (fast-forward) genera proposal, design, specs y tasks de una vez. Tras revisar los artefactos se lanzó `/opsx:apply` y el agente implementó las tareas en orden (modelo → servicio → controller), pidiendo confirmación en cada bloque.

### Prompt 7.3 — Change de UX a partir de una molestia concreta

**Herramienta:** Cursor + OpenSpec (`/opsx-new`)

**Prompt:**
```
El menú lateral ocupa siempre 256px y me quita espacio al revisar slides.
Quiero poder colapsarlo: en modo colapsado solo iconos con tooltip, el logo
en tamaño pequeño centrado, el footer oculto, y que el estado persista en
localStorage. La transición debe ser suave (CSS).
```

**Nota:** Ejemplo del ciclo completo para un cambio pequeño (`collapsible-sidebar`): la descripción informal de una molestia de uso se convirtió en proposal con requisitos verificables, se implementó en una sesión y se archivó. Todo cambio, por pequeño que sea, deja rastro en `openspec/changes/archive/`.

### Prompt 7.4 — Archivado y sincronización de specs

**Herramienta:** Claude Code + OpenSpec (`/opsx:archive`)

**Prompt:**
```
/opsx:archive He probado la generación de imágenes end-to-end y funciona
(incluido el caso de fallo de una imagen individual sin bloquear el resto).
Archiva el change implement-image-generation y sincroniza las specs.
```

**Nota:** El archivado mueve el change a `archive/` con fecha y aplica las delta-specs sobre `openspec/specs/`, manteniendo las specs principales como fuente de verdad del estado actual del sistema.

### Registro de changes completados

| Fecha | Change | Alcance |
|---|---|---|
| 2026-02-09 | `define-mvp-specs` | Specs iniciales de las 6 capacidades del MVP |
| 2026-02-09 | `config-base-backend` | Base Spring Boot + Java 21 (TK-001) |
| 2026-02-09 | `implement-project-mgmt` | CRUD de proyectos con DTOs (TK-002) |
| 2026-02-09 | `frontend-mvp-ui` | React + Vite + Tailwind + rutas (TK-003, TK-004) |
| 2026-02-10 | `implement-content-processing` | Pipeline GPT-4o de 2 fases (TK-005, TK-006) |
| 2026-02-10 | `integrate-frontend-content-processing` | SSE + modal de progreso (TK-007, TK-008) |
| 2026-02-10 | `db-h2-file` | H2 persistente en fichero |
| 2026-02-10 | `ui-ayg` | Identidad visual corporativa |
| 2026-02-12 | `config-page` | Página de ajustes (TK-011, TK-012) |
| 2026-02-12 | `refactor-content-generation` | Records SlideData/SlideOutline |
| 2026-02-12 | `implement-image-generation` | OpenAI Images + storage local (TK-009, TK-010) |
| 2026-02-12 | `image-generation-prompt-mods` | Prompts de imagen con estilo visual global |
| 2026-02-16 | `dark-mode` | Modo oscuro persistido (US-006) |
| 2026-03-06 | `collapsible-sidebar` | Sidebar colapsable (US-007) |

---

> **Nota sobre el uso de IA en el desarrollo:**
> 
> El 100% del código ha sido generado con asistencia de Claude Code (claude-sonnet-4-6) como copiloto. El proceso habitual fue: definir el requisito en lenguaje natural → revisar el código generado → ajustar los detalles específicos del dominio → iterar si era necesario. Las decisiones de diseño (patrones, estructura, tecnologías) se tomaron siempre con criterio humano, usando la IA como herramienta de implementación y validación, no como diseñador del sistema.
