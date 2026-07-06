# image-generation Specification

## Purpose
Define the capability to generate images from text prompts using AI models.

## Requirements

### Requirement: DALL-E 3 Integration
The system SHALL use OpenAI's DALL-E 3 model to generate images.

#### Scenario: Generate Image
- **WHEN** an image is requested with a prompt
- **THEN** the system SHALL call OpenAI API with model `dall-e-3`
- **AND** the size SHALL be `1792x1024` (16:9 aspect ratio)
- **AND** the response SHALL contain the image data (URL or B64)

### Requirement: Configurable Image Generation
The system SHALL allow disabling image generation via configuration to avoid costs during testing.

#### Scenario: Image Generation Enabled
- **WHEN** the property `app.image-generation.enabled` is `true` or not set
- **THEN** the system SHALL generate images for all slides
- **AND** slides SHALL have status `COMPLETED` with `imageUrl` populated

#### Scenario: Image Generation Disabled
- **WHEN** the property `app.image-generation.enabled` is `false`
- **THEN** the system SHALL skip all image generation calls
- **AND** slides SHALL have status `PENDING` with `imageUrl` as `null`
- **AND** the content pipeline SHALL complete successfully without generating images
- **AND** no API calls to DALL-E 3 SHALL be made

### Requirement: Error Handling
The system SHALL handle generation failures gracefully.

#### Scenario: Generation Failure
- **WHEN** image generation fails (e.g., API error, rate limit)
- **THEN** the system SHALL log the error
- **AND** the system SHALL return null or a default placeholder mechanism (to be decided, null for now)
- **AND** the process SHALL NOT crash the entire pipeline
