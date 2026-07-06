# Proposal: Persistencia de Base de Datos H2 en Archivo

## Why

Actualmente la base de datos H2 está configurada en modo in-memory, lo que significa que todos los proyectos creados se pierden al reiniciar el servidor. Esto dificulta las pruebas de desarrollo y la demostración del sistema, ya que hay que recrear los proyectos de ejemplo cada vez.

Es necesario cambiar a un almacenamiento basado en archivo para mantener los datos persistentes entre reinicios del backend.

## What Changes

- Modificar la configuración de H2 de `mem:` a `file:` en `application.properties`
- Definir ruta de almacenamiento del archivo de base de datos
- Configurar `spring.jpa.hibernate.ddl-auto` para evolución de esquema (cambiar de `create-drop` a `update`)
- Añadir archivo de base de datos H2 al `.gitignore` para evitar commits accidentales
- Documentar ubicación del archivo de datos en README

## Capabilities

### New Capabilities
- `h2-file-persistence`: Persistencia de datos en archivo H2 local para mantener proyectos, slides y configuraciones entre reinicios del servidor

### Modified Capabilities
- `backend-foundation`: Actualización de la configuración de base de datos para usar almacenamiento en archivo en lugar de in-memory

## Impacts

### Configuration
- `backend/src/main/resources/application.properties` - URL de conexión JDBC y estrategia de DDL
- `.gitignore` - Añadir exclusión del directorio de datos H2

### Development Experience
- Los proyectos de prueba se mantendrán después de reiniciar el servidor
- Facilita testing y demostración sin pérdida de datos
- Permite acumulación de datos de ejemplo para pruebas

### Database Files
- Nuevo directorio: `backend/data/` para almacenar archivos H2
- Archivos generados: `presentaciones.mv.db` y `presentaciones.trace.db`

### No Breaking Changes
- La migración es transparente para el código existente
- Las entidades y repositorios no requieren cambios
- La API REST mantiene la misma interfaz
