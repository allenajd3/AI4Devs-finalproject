## MODIFIED Requirements

### Requirement: Slide Entity
The system SHALL support a Slide entity representing a single slide in a presentation, containing title, content, and image-related data.

#### Scenario: Slide Attributes
- **WHEN** a slide is created
- **THEN** it SHALL have: id (UUID), projectId (FK to Project), order (integer), title (TEXT, slide title), content (TEXT, slide text content in Spanish), imagePrompt (TEXT for image generation), imageUrl (nullable, filled when image is generated), status (PENDING, GENERATING, COMPLETED, ERROR)

#### Scenario: Title And Content Fields Present
- **WHEN** the slide entity is defined
- **THEN** it SHALL contain a "title" field of type TEXT
- **AND** it SHALL contain a "content" field of type TEXT
- **AND** both fields SHALL allow NULL values for backward compatibility
- **AND** new slides SHALL have these fields populated from the LLM response

#### Scenario: ImagePrompt Contains Visual Description
- **WHEN** a slide is created
- **THEN** the imagePrompt field SHALL contain the English description for image generation
- **AND** it SHALL incorporate visual style guidance from GlobalSettings if configured
- **AND** it SHALL be suitable for passing to an image generation API

## ADDED Requirements

### Requirement: Database Schema Update
The system SHALL add new columns to the slides table without breaking existing data.

#### Scenario: Add Title Column
- **WHEN** the application starts with the new entity
- **THEN** Hibernate SHALL add a "title" column of type TEXT to the slides table
- **AND** existing rows SHALL have NULL in the title column
- **AND** new rows SHALL populate the title column

#### Scenario: Add Content Column
- **WHEN** the application starts with the new entity
- **THEN** Hibernate SHALL add a "content" column of type TEXT to the slides table
- **AND** existing rows SHALL have NULL in the content column
- **AND** new rows SHALL populate the content column

#### Scenario: No Data Migration Required
- **WHEN** the schema is updated
- **THEN** existing slide records SHALL remain valid
- **AND** NULL values in title and content SHALL be acceptable
- **AND** the API SHALL continue to function with existing data
