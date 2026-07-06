# Tasks: Persistencia de Base de Datos H2 en Archivo

## 1. Leer Configuración Actual

- [x] 1.1 Leer `backend/src/main/resources/application.properties` para identificar la configuración actual de H2

## 2. Modificar Configuración de Base de Datos

- [x] 2.1 Cambiar `spring.datasource.url` de `jdbc:h2:mem:testdb` a `jdbc:h2:file:./backend/data/presentaciones`
- [x] 2.2 Cambiar `spring.jpa.hibernate.ddl-auto` de `create-drop` a `update`
- [x] 2.3 Verificar que las demás propiedades de H2 (consola, driver) siguen igual

## 3. Actualizar .gitignore

- [x] 3.1 Leer `.gitignore` actual
- [x] 3.2 Añadir línea `backend/data/` para excluir archivos de base de datos del control de versiones

## 4. Actualizar Documentación

- [x] 4.1 Actualizar `README.md` para documentar:
  - La ubicación del archivo de base de datos (`backend/data/`)
  - Cómo limpiar la base de datos (borrar el directorio `backend/data/`)
  - Que los datos ahora persisten entre reinicios

## 5. Verificación

- [x] 5.1 Reiniciar el servidor backend
- [x] 5.2 Verificar que se crea el archivo `backend/data/presentaciones.mv.db`
- [x] 5.3 Crear un proyecto de prueba usando el frontend o API
- [x] 5.4 Reiniciar el servidor backend nuevamente
- [x] 5.5 Verificar que el proyecto de prueba sigue existiendo (GET /api/projects)
- [x] 5.6 Verificar que todas las funcionalidades siguen operativas (crear proyecto, generar contenido, listar slides)
