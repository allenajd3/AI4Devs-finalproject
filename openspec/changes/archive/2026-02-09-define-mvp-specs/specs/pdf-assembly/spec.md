## ADDED Requirements

### Requirement: PDF Export Service
The system SHALL combine all slide images into a single PDF document.

#### Scenario: Generate PDF
- **WHEN** the user requests PDF export
- **THEN** the system retrieves all slide images (in order)
- **AND** generates a PDF file where each page corresponds to one slide image (full-size)

#### Scenario: Download PDF
- **WHEN** the generation is complete
- **THEN** the system provides a download URL for the PDF

### Requirement: Layout Optimization
The system SHALL ensure the generated PDF fits standard executive presentation dimensions.

#### Scenario: Aspect Ratio
- **WHEN** generating slides and PDF
- **THEN** the aspect ratio SHALL be 16:9 or A4 Landscape as configured in settings (default 16:9)
