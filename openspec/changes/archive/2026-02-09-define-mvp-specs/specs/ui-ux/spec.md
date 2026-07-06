## ADDED Requirements

### Requirement: Navigation System
The application SHALL provide a persistent lateral navigation menu.

#### Scenario: Menu Items
- **WHEN** the user is in the application
- **THEN** the menu SHALL display:
  - "Crear" (Create)
  - "Proyectos" (Projects)
  - "Ajustes" (Settings)

### Requirement: Dashboard View (Create)
The "Crear" view SHALL be the default landing page for new users.

#### Scenario: Create Interface
- **WHEN** the user visits "/" or "Crear"
- **THEN** they see options to Upload File (.txt) or Paste Text
- **AND** a prominent "Generar presentación" button

### Requirement: Project List View
The "Proyectos" view SHALL display all existing projects in a tile grid.

#### Scenario: Project Grid
- **WHEN** viewing "Proyectos"
- **THEN** each project tile shows a preview of the first slide
- **AND** the title and creation date

### Requirement: Project Detail View
The Detail View SHALL display the generated presentation and allow editing.

#### Scenario: Slide Mosaic
- **WHEN** viewing a project detail
- **THEN** all slides are shown in a mosaic grid
- **AND** each slide shows the generated image and editable text fields

#### Scenario: Global Actions
- **WHEN** in detail view
- **THEN** "Descargar PDF" and "Eliminar Proyecto" buttons are available

### Requirement: Feedback System
The system SHALL provide visual feedback during long-running operations.

#### Scenario: Generation Loading State
- **WHEN** "Generar presentación" is clicked
- **THEN** a global loading indicator (e.g., spinner, progress bar) is shown with text "Generando presentación..."
