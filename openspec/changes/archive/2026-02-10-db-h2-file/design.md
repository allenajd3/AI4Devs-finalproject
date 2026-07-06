# Design: Persistencia de Base de Datos H2 en Archivo

## Context

La aplicación actualmente usa H2 en modo in-memory (`jdbc:h2:mem:testdb`) configurada con `ddl-auto=create-drop`. Esto significa que:
- La base de datos se crea vacía en cada inicio
- Todos los datos (proyectos, slides, configuración global) se pierden al reiniciar
- Es necesario recrear datos de prueba en cada sesión de desarrollo

Para MVP y demostraciones, necesitamos que los datos persistan entre reinicios sin migrar aún a PostgreSQL.

## Goals / Non-Goals

**Goals:**
- Persistir datos localmente en un archivo H2
- Mantener los datos entre reinicios del servidor
- Evolucionar el esquema automáticamente con `update` (sin perder datos)
- Facilitar pruebas y demos sin necesidad de recrear proyectos

**Non-Goals:**
- Migración a PostgreSQL (queda para fase de producción)
- Backups automáticos de la base de datos
- Configuración multi-entorno (desarrollo vs producción)
- Optimización de rendimiento de H2

## Decisions

### Decisión 1: Ubicación del Archivo de Base de Datos
**Elegido:** `backend/data/presentaciones` (relativo al directorio del proyecto)

**Razón:**
- Fácil de encontrar y gestionar en desarrollo
- No interfiere con Maven (`target/` ya está excluido)
- Permite tener múltiples instancias del proyecto sin conflictos de path absoluto

**Alternativas consideradas:**
- Path absoluto (`C:/data/...`) → Descartado: no portable entre máquinas
- Dentro de `target/` → Descartado: se borra con `mvn clean`
- Home directory del usuario → Descartado: menos visible para desarrollo

### Decisión 2: Estrategia de DDL
**Elegido:** `spring.jpa.hibernate.ddl-auto=update`

**Razón:**
- Preserva datos existentes
- Aplica cambios de esquema automáticamente al añadir/modificar entidades
- Adecuado para desarrollo y MVP

**Alternativas consideradas:**
- `create-drop` (actual) → Descartado: pierde todos los datos
- `validate` → Descartado: requiere gestión manual de esquema
- Flyway/Liquibase → Descartado: overkill para MVP con H2

### Decisión 3: Exclusión de Git
**Elegido:** Añadir `backend/data/` a `.gitignore`

**Razón:**
- Los archivos de base de datos no deben versionarse
- Cada desarrollador debe tener su propia instancia local
- Evita conflictos en commits

### Decisión 4: Compatibilidad de Configuración
**Elegido:** Mantener compatibilidad con entorno actual (solo cambio de properties)

**Razón:**
- No requiere cambios en código Java
- No afecta a las entidades, repositorios o servicios
- Migración transparente y reversible

## Risks / Trade-offs

### Riesgo 1: Corrupción de Archivo de Datos
**Riesgo:** Si el servidor se cierra abruptamente, el archivo H2 podría corromperse.

**Mitigación:**
- H2 tiene mecanismos de recuperación automática
- En desarrollo, si ocurre, simplemente borrar `backend/data/` y reiniciar
- Documentar cómo limpiar la base de datos

### Riesgo 2: Conflictos de Esquema en Evolución
**Riesgo:** Con `ddl-auto=update`, algunos cambios complejos de esquema podrían fallar.

**Mitigación:**
- Para MVP con cambios simples, `update` es suficiente
- Si hay problema, borrar `backend/data/` y dejar que se recree
- Para producción se usará PostgreSQL con migraciones gestionadas

### Trade-off: H2 vs PostgreSQL
**Trade-off:** H2 file no es adecuado para producción, pero facilita desarrollo.

**Justificación:**
- Para MVP y desarrollo, H2 es más simple (sin instalación adicional)
- Migración a PostgreSQL será directa (mismo JPA/Hibernate)
- Por ahora prioriza velocidad de desarrollo sobre preparación para producción

## Migration Plan

### Despliegue
1. Detener el servidor backend
2. Aplicar cambios en `application.properties`
3. Reiniciar el servidor
4. Verificar que el archivo `backend/data/presentaciones.mv.db` se crea
5. Crear un proyecto de prueba
6. Reiniciar el servidor
7. Verificar que el proyecto de prueba sigue existiendo

### Rollback
Si hay algún problema:
1. Cambiar `application.properties` de vuelta a configuración in-memory
2. Reiniciar el servidor
3. Opcionalmente borrar `backend/data/` si quedó archivo corrupto

### Limpieza de Datos (Cuando sea Necesario)
Para empezar con base de datos limpia:
```powershell
Remove-Item -Recurse -Force backend/data/
```

## Open Questions

Ninguna - el cambio es directo y bien definido.
