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
The system SHALL generate the project title as part of the slide outline generation, not as a separate LLM call.

#### Scenario: Title Generated With Outline
- **WHEN** the slide outline is generated from the analyzed content
- **THEN** the JSON response SHALL include a top-level "title" field with the presentation title
- **AND** the title SHALL be stored on the Project entity
- **AND** no separate LLM call SHALL be made solely for title generation

### Requirement: Slide Count And Prompts
The system SHALL generate between 1 and 12 slides, each with title, content, and imagePrompt (description in English).

#### Scenario: Slide Count Bounds
- **WHEN** slides are generated
- **THEN** the number of slides SHALL be between 1 and 12
- **AND** it SHALL adapt to the content (not fixed)

#### Scenario: Complete Slide Data Per Slide
- **WHEN** a slide is created by the content pipeline
- **THEN** it SHALL have a "title" field with the slide's title
- **AND** it SHALL have a "content" field with the slide's text content in Spanish
- **AND** it SHALL have an imagePrompt (labeled as "description" in JSON) containing the visual description in English for image generation
- **AND** the imagePrompt SHALL include visual style context from GlobalSettings.visualStyle if configured

### Requirement: Configuration Integration With Null Handling
The system SHALL integrate GlobalSettings configuration into LLM prompts only when the configuration fields are not null.

#### Scenario: SystemPrompt Used When Configured
- **WHEN** GlobalSettings.systemPrompt is not null
- **THEN** it SHALL be included in the system message of LLM calls
- **WHEN** GlobalSettings.systemPrompt is null
- **THEN** only the base context SHALL be included in the system message

#### Scenario: ContentOrientation Used In Analysis
- **WHEN** GlobalSettings.contentOrientation is not null
- **THEN** it SHALL be included in the analysis phase to guide content focus
- **WHEN** GlobalSettings.contentOrientation is null
- **THEN** the analysis SHALL proceed without specific content orientation guidance

#### Scenario: VisualStyle Used In Slide Generation
- **WHEN** GlobalSettings.visualStyle is not null
- **THEN** it SHALL be included in the slide outline generation to guide imagePrompt descriptions
- **WHEN** GlobalSettings.visualStyle is null
- **THEN** the LLM SHALL use its default visual style understanding

### Requirement: Two-Phase LLM Pipeline
The content processing pipeline SHALL make exactly 2 LLM calls: analysis and outline generation.

#### Scenario: Phase 1 - Transcript Analysis
- **WHEN** content processing starts
- **THEN** the first LLM call SHALL analyze the transcript
- **AND** it SHALL clean conversational noise
- **AND** it SHALL extract main ideas
- **AND** it SHALL return a structured summary

#### Scenario: Phase 2 - Outline Generation
- **WHEN** the analysis is complete
- **THEN** the second LLM call SHALL generate a complete outline
- **AND** the outline SHALL include the presentation title
- **AND** the outline SHALL include all slides with title, content, and description
- **AND** the response SHALL be a single JSON object with structure: `{title: string, slides: [{slideNumber, title, content, description}]}`

#### Scenario: No Separate Title Call
- **WHEN** the content pipeline runs
- **THEN** there SHALL NOT be a separate LLM call solely for title generation
- **AND** the title SHALL come from the outline generation call

### Requirement: JSON Response Parsing
The system SHALL parse the complex JSON response from the outline generation call and persist all fields.

#### Scenario: Parse Complete Outline
- **WHEN** the LLM returns the outline JSON
- **THEN** the system SHALL extract the "title" field
- **AND** the system SHALL extract the "slides" array
- **AND** for each slide, it SHALL extract: slideNumber, title, content, description

#### Scenario: Persist Slide Data
- **WHEN** slides are saved to the database
- **THEN** each Slide entity SHALL have its "title" field populated
- **AND** each Slide entity SHALL have its "content" field populated
- **AND** each Slide entity SHALL have its "imagePrompt" field populated with the "description" from JSON
- **AND** the "order" field SHALL be set from "slideNumber"

#### Scenario: Handle Markdown-Wrapped JSON
- **WHEN** the LLM returns JSON wrapped in markdown code blocks (` ```json...``` `)
- **THEN** the system SHALL strip the markdown wrappers before parsing
- **AND** JSON parsing SHALL succeed

### Requirement: Synchronous Execution With Step Feedback
The content pipeline SHALL run synchronously and expose step-by-step progress for the client.

#### Scenario: Steps Exposed
- **WHEN** the pipeline runs
- **THEN** the client SHALL be able to receive feedback for steps such as: analyzing transcript, generating structure, generating slide 1/N … N/N, completed
- **AND** after completion the client SHALL be able to redirect to the project detail view

#### Scenario: Pipeline Persists Results
- **WHEN** the pipeline completes successfully
- **THEN** the Project SHALL be updated (title, status)
- **AND** all Slide records SHALL be persisted with order, title, content, imagePrompt, status PENDING
