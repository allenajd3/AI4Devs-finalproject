# h2-file-persistence Specification

## Purpose
Configurar la base de datos H2 para persistir datos en archivo en lugar de memoria, permitiendo que los proyectos y configuraciones se mantengan entre reinicios del servidor.

## ADDED Requirements

### Requirement: File-based H2 Database
El sistema SHALL utilizar H2 en modo archivo para almacenar datos localmente.

#### Scenario: Database File Creation
- **WHEN** la aplicación se inicia por primera vez
- **THEN** se crea el archivo de base de datos en `backend/data/presentaciones.mv.db`
- **AND** el archivo persiste después de cerrar la aplicación

#### Scenario: Data Persistence After Restart
- **WHEN** se crea un proyecto
- **AND** se reinicia el servidor
- **THEN** el proyecto sigue existiendo en la base de datos
- **AND** todos sus datos (título, contenido, status, slides) están intactos

### Requirement: Schema Evolution
El sistema SHALL mantener el esquema de base de datos actualizado automáticamente al modificar entidades JPA.

#### Scenario: New Entity Field Added
- **WHEN** se añade un nuevo campo a una entidad JPA existente
- **AND** se reinicia el servidor
- **THEN** la columna correspondiente se añade a la tabla automáticamente
- **AND** los datos existentes se preservan

#### Scenario: New Entity Created
- **WHEN** se crea una nueva entidad JPA con `@Entity`
- **AND** se reinicia el servidor
- **THEN** la tabla correspondiente se crea automáticamente

### Requirement: Database File Location
El archivo de base de datos SHALL ubicarse en el directorio `backend/data/` relativo a la raíz del proyecto.

#### Scenario: Verify Database Path
- **WHEN** la aplicación se ejecuta desde cualquier directorio de trabajo
- **THEN** el archivo de base de datos se crea en `backend/data/` relativo al proyecto
- **AND** no en una ubicación absoluta del sistema

### Requirement: Clean Database Reset
El desarrollador SHALL poder limpiar la base de datos eliminando el directorio `backend/data/`.

#### Scenario: Manual Database Reset
- **WHEN** se detiene el servidor
- **AND** se elimina el directorio `backend/data/`
- **AND** se reinicia el servidor
- **THEN** se crea una nueva base de datos vacía
- **AND** todas las tablas se recrean según las entidades JPA

### Requirement: Git Exclusion
Los archivos de base de datos SHALL excluirse del control de versiones.

#### Scenario: Database Files Not Committed
- **WHEN** se ejecuta `git status` después de crear proyectos
- **THEN** los archivos en `backend/data/` NO aparecen como archivos a añadir
- **AND** están listados en `.gitignore`
