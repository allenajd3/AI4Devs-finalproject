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

### Requirement: Simplified Prompt Format
The system SHALL send to the configured image generation API (e.g. gpt-image-1 or DALL-E 3) a single-sentence prompt built from fixed intro, optional style, the slide description in English, and a fixed closing. The prompt SHALL NOT contain separate blocks for TITLE, CONTENT/BULLET POINTS, or IMPORTANT REQUIREMENTS.

#### Scenario: Prompt With Visual Style Configured
- **WHEN** GlobalSettings.visualStyle is not null and not empty
- **THEN** the prompt SHALL be: "Professional presentation slide design, 16:9 aspect ratio, high quality corporate design. {visualStyle}. {description}. Ultra high resolution, sharp text, professional business presentation."
- **AND** {visualStyle} SHALL be the value of GlobalSettings.visualStyle
- **AND** {description} SHALL be the slide's English description from the outline (imagePrompt)

#### Scenario: Prompt Without Visual Style
- **WHEN** GlobalSettings.visualStyle is null or empty
- **THEN** the prompt SHALL be: "Professional presentation slide design, 16:9 aspect ratio, high quality corporate design. Modern minimalist style with clean typography. {description}. Ultra high resolution, sharp text, professional business presentation."
- **AND** {description} SHALL be the slide's English description from the outline (imagePrompt)

### Requirement: Configurable Image Model and API Parameters
The system SHALL support configurable image generation model and SHALL send only parameters accepted by the selected model.

#### Scenario: Model and Parameters Configuration
- **WHEN** the application runs
- **THEN** the image model SHALL be read from `app.image-generation.model` (default: gpt-image-1)
- **AND** size SHALL be read from `app.image-generation.size` (default: 1536x1024 for gpt-image-1)
- **AND** quality SHALL be read from `app.image-generation.quality` (default: high for gpt-image-1)

#### Scenario: gpt-image-1 Parameters
- **WHEN** the model is gpt-image-1 (or gpt-image-1-mini, gpt-image-1.5)
- **THEN** the request SHALL NOT include `response_format` (parameter not supported; API returns base64 by default)
- **AND** size SHALL be one of 1536x1024, 1024x1024, 1024x1536
- **AND** quality SHALL be one of high, medium, low

#### Scenario: DALL-E 3 Parameters
- **WHEN** the model is dall-e-3
- **THEN** the request SHALL include `response_format: b64_json` to receive base64 image data
- **AND** size and quality SHALL follow DALL-E 3 API (e.g. 1792x1024, standard or hd)
