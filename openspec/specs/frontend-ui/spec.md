# frontend-ui Specification

## Purpose
TBD - created by archiving change frontend-mvp-ui. Update Purpose after archive.
## Requirements
### Requirement: Project Setup
The frontend application SHALL be initialized with Vite, React, TypeScript, and Tailwind CSS.

#### Scenario: Verify Project Structure
- **WHEN** the frontend project is created
- **THEN** it SHALL contain a valid `package.json`, `vite.config.ts`, `tsconfig.json`, and `tailwind.config.js`
- **AND** it SHALL run successfully with `npm run dev`

### Requirement: Navigation System
The application SHALL provide a persistent lateral navigation menu with corporate branding.

#### Scenario: Menu Structure
- **WHEN** the user is in any view
- **THEN** the menu SHALL display links to: "Crear", "Proyectos", "Ajustes"
- **AND** the active route SHALL be visually highlighted

#### Scenario: Corporate Logo Integration
- **WHEN** the navigation sidebar is rendered
- **THEN** it SHALL display the a&g logo at the top of the sidebar
- **AND** the logo SHALL be centered horizontally
- **AND** there SHALL be visual separation (divider or spacing) between the logo and menu items

#### Scenario: Corporate Color Scheme
- **WHEN** the navigation sidebar is rendered
- **THEN** it SHALL use the corporate color palette (ayg-primary for background)
- **AND** menu items SHALL use white text for contrast
- **AND** active/hover states SHALL use ayg-primary-dark or appropriate variant

### Requirement: Create View
The "Crear" view SHALL allow users to input text via textarea or file upload.

#### Scenario: Text Input Validation
- **WHEN** the user types in the textarea
- **THEN** the "Generar presentación" button SHALL be disabled if text length < 100 characters

#### Scenario: File Upload
- **WHEN** the user uploads a .txt file
- **THEN** the file content SHALL be loaded into the textarea
- **AND** validation rules SHALL apply

#### Scenario: Create Project Action
- **WHEN** the user clicks "Generar presentación" with valid input
- **THEN** a POST request SHALL be sent to `/api/projects` with title and content
- **AND** the user SHALL be redirected to `/proyectos/:id` upon success

### Requirement: Projects List View
The "Proyectos" view SHALL display all existing projects in a responsive tile grid.

#### Scenario: Display Projects
- **WHEN** the user navigates to "/proyectos"
- **THEN** a GET request SHALL fetch all projects from `/api/projects`
- **AND** projects SHALL be displayed in tiles ordered by creation date (newest first)

#### Scenario: Project Tile Content
- **WHEN** a project tile is rendered
- **THEN** it SHALL display the project title
- **AND** clicking the tile SHALL navigate to `/proyectos/:id`

### Requirement: Project Detail View
The detail view SHALL display project information and placeholder for slides.

#### Scenario: Fetch Project Details
- **WHEN** the user navigates to `/proyectos/:id`
- **THEN** a GET request SHALL fetch project details from `/api/projects/:id`
- **AND** the project title and original content SHALL be displayed

#### Scenario: Placeholder for Slides
- **WHEN** the project has no slides yet
- **THEN** a "Coming soon" or "Generación pendiente" message SHALL be shown in the slides section

#### Scenario: Delete Project Action
- **WHEN** the user clicks "Eliminar"
- **THEN** a DELETE request SHALL be sent to `/api/projects/:id`
- **AND** the user SHALL be redirected to `/proyectos` upon success

### Requirement: Settings View
The "Ajustes" view SHALL display a placeholder message.

#### Scenario: Coming Soon Message
- **WHEN** the user navigates to "/ajustes"
- **THEN** a "Coming soon" message SHALL be displayed

### Requirement: WebSocket Infrastructure (Prepared)
The application SHALL include WebSocket client configuration for future use.

#### Scenario: WebSocket Client Setup
- **WHEN** the application initializes
- **THEN** the WebSocket client library (`@stomp/stompjs`) SHALL be installed and configured
- **AND** connection attempts SHALL fail gracefully if backend is not ready

