# content-processing Specification

## Purpose
Define how transcriptions are processed using OpenAI LLM to generate executive presentations with structured slides.

## Requirements

### Requirement: OpenAI Integration
The system SHALL use OpenAI for analysis and structure generation, with model configurable via application properties (default gpt-4o).

#### Scenario: Model Configuration
- **WHEN** the application starts
- **THEN** the OpenAI model SHALL be read from configuration (e.g. openai.model)
- **AND** if not set, it SHALL default to gpt-4o

### Requirement: Transcript Analysis
The system SHALL analyze the raw transcript to remove noise and extract main ideas, then reorganize content non-linearly.

#### Scenario: Non-Linear Structure
- **WHEN** content is processed
- **THEN** the structure SHALL follow intro / nudo / desenlace (or equivalent: context, key points, conclusions and actions)
- **AND** the order SHALL NOT be strictly linear with respect to the input text order

### Requirement: Project Title Generation
The system SHALL generate a short project title based on the analyzed content.

#### Scenario: Title Stored On Project
- **WHEN** the content pipeline completes
- **THEN** the Project title SHALL be updated with the LLM-generated title

### Requirement: Slide Count And Prompts
The system SHALL generate between 1 and 12 slides, each with only an imagePrompt (no title/content fields on slide).

#### Scenario: Slide Count Bounds
- **WHEN** slides are generated
- **THEN** the number of slides SHALL be between 1 and 12
- **AND** it SHALL adapt to the content (not fixed)

#### Scenario: Image Prompt Per Slide
- **WHEN** a slide is created by the content pipeline
- **THEN** it SHALL have an imagePrompt containing the description for that slide's image
- **AND** the imagePrompt SHALL be the slide-specific content only (global config context is applied later when generating the image)

### Requirement: Synchronous Execution With Step Feedback
The content pipeline SHALL run synchronously and expose step-by-step progress for the client.

#### Scenario: Steps Exposed
- **WHEN** the pipeline runs
- **THEN** the client SHALL be able to receive feedback for steps such as: analyzing transcript, generating structure, generating title, generating slide 1/N … N/N, completed
- **AND** after completion the client SHALL be able to redirect to the project detail view

#### Scenario: Pipeline Persists Results
- **WHEN** the pipeline completes successfully
- **THEN** the Project SHALL be updated (title, status)
- **AND** all Slide records SHALL be persisted with order and imagePrompt, status PENDING
