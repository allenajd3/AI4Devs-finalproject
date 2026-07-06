## Context

Se requiere establecer la base de un nuevo proyecto backend utilizando Spring Boot y Java. Actualmente no existe código, por lo que se parte de cero (greenfield). El objetivo es tener un proyecto configurado y listo para recibir lógica de negocio.

## Goals / Non-Goals

**Goals:**
- Crear un proyecto Maven funcional.
- Configurar Spring Boot 3.4.x y Java 21.
- Establecer la estructura de directorios estándar.
- Definir el paquete base `com.ayg.presentaciones`.
- Asegurar que la aplicación arranca correctamente.

**Non-Goals:**
- Implementar controladores o lógica de negocio específica en esta fase.
- Configurar bases de datos o seguridad avanzada por ahora (se hará en siguientes iteraciones).

## Decisions

- **Estructura del Repositorio**: Se creará el proyecto dentro de una carpeta `backend/` en la raíz del repositorio. Esto facilita la futura incorporación de un frontend en el mismo repositorio (monorepo structure) manteniendo una separación clara.
- **Build Tool**: Maven. Es el estándar de facto en el ecosistema Spring Boot empresarial y ofrece una gestión de dependencias robusta.
- **Framework**: Spring Boot 4.0.x. Versión estable más reciente que ofrece soporte a largo plazo y compatibilidad con Java 21.
- **Lenguaje**: Java 21 (LTS). Proporciona las últimas características del lenguaje y soporte a largo plazo.
- **Paquetería**: `com.ayg.presentaciones`. Estructura estándar de dominio invertido.

## Risks / Trade-offs

- **Risk**: Posibles conflictos de puerto si el entorno de desarrollo tiene el 8080 ocupado.
    - **Mitigation**: Configurar el puerto por defecto en `application.properties` pero permitir sobreescritura.
