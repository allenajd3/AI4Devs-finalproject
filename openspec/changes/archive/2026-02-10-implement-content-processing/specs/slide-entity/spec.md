## ADDED Requirements

### Requirement: Slide Entity
The system SHALL support a Slide entity representing a single slide in a presentation, containing only image-related data.

#### Scenario: Slide Attributes
- **WHEN** a slide is created
- **THEN** it SHALL have: id (UUID), projectId (FK to Project), order (integer), imagePrompt (text for image generation only), imageUrl (nullable, filled when image is generated), status (PENDING, GENERATING, COMPLETED, ERROR)

#### Scenario: No Title Or Content Fields
- **WHEN** the slide entity is defined
- **THEN** it SHALL NOT contain title or body content fields
- **AND** the only content field SHALL be imagePrompt

### Requirement: Slide Ordering
Slides SHALL be ordered within a project by the order field.

#### Scenario: Retrieve Slides By Order
- **WHEN** slides for a project are queried
- **THEN** they SHALL be returned ordered by the order field ascending
