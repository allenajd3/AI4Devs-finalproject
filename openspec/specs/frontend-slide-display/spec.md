# frontend-slide-display Specification

## Purpose
Display generated slides in the project detail view with proper layout and status indicators.

## Requirements

### Requirement: Fetch and Display Slides
The project detail page SHALL fetch and display all slides for a project.

#### Scenario: Fetch Slides
- **WHEN** the user navigates to a project detail page
- **THEN** the frontend SHALL call `GET /api/projects/{id}/slides`
- **AND** it SHALL display the slides in order

#### Scenario: Slide Grid Layout
- **WHEN** slides are loaded
- **THEN** they SHALL be displayed in a responsive grid
- **AND** each slide SHALL show:
  - Order number (prominently)
  - Image prompt text
  - Status badge (PENDING, GENERATING, COMPLETED, ERROR)
  - Placeholder for image (grey box or icon)

### Requirement: Slide Card Component
The frontend SHALL implement a `SlideCard` component to display individual slides.

#### Scenario: Card Content
- **WHEN** a SlideCard is rendered
- **THEN** it SHALL display:
  - Slide order number as a badge or header
  - Image prompt as readable text (with line breaks if needed)
  - Status indicator with color coding
  - Placeholder area for future image display

### Requirement: Handle Empty State
The detail page SHALL handle projects without slides gracefully.

#### Scenario: No Slides Yet
- **WHEN** a project has no slides (status DRAFT)
- **THEN** the frontend SHALL display a message "Sin diapositivas generadas"
- **AND** it SHALL show a button "Generar contenido" to trigger generation

#### Scenario: Loading State
- **WHEN** slides are being fetched
- **THEN** the frontend SHALL display skeleton loaders or spinners
- **AND** the layout SHALL be preserved to avoid layout shift

### Requirement: TypeScript Types for Slides
The frontend SHALL define proper TypeScript types for Slide entities.

#### Scenario: Slide Type Definition
- **WHEN** working with slide data
- **THEN** the type SHALL include:
  - `id: string` (UUID)
  - `order: number`
  - `imagePrompt: string`
  - `imageUrl: string | null`
  - `status: SlideStatus` (enum)
