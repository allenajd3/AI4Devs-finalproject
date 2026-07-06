## Context

El sistema necesita gestionar proyectos de presentaciones. Cada proyecto representa una unidad de trabajo que contiene el texto original (transcripción) y su estado de procesamiento.

## Goals / Non-Goals

**Goals:**
- Crear una entidad `Project` robusta.
- Exponer una API REST para operaciones CRUD básicas.
- Gestionar el estado del proyecto.
- Persistir los datos (H2 en memoria/archivo para desarrollo rápido MVP).

**Non-Goals:**
- Implementar la generación de IA en este cambio (solo el esqueleto del proyecto).
- Implementar autenticación de usuarios (fuera de alcance MVP).

## Decisions

- **Base de Datos**: H2 Database (in-memory) para desarrollo rápido y pruebas, fácilmente migratable a PostgreSQL en producción gracias a Spring Data JPA.
- **Identificadores**: UUID para evitar colisiones y enumeración fácil.
- **API**: RESTful estándar (`POST /projects`, `GET /projects`, `GET /projects/{id}`, `DELETE /projects/{id}`).
- **Estado**: Enum `ProjectStatus` (`DRAFT`, `GENERATING`, `COMPLETED`, `ERROR`).
- **DTOs**: Uso de Records de Java 21 para DTOs inmutables y concisos.

## Risks / Trade-offs

- **Risk**: Pérdida de datos con H2 en memoria al reiniciar.
    - **Mitigation**: Aceptable para desarrollo inicial. Se puede configurar H2 persistente en archivo o cambiar a PostgreSQL más adelante.
