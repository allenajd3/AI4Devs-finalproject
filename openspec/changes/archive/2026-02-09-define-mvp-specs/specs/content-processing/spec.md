## ADDED Requirements

### Requirement: Text Analysis
The system SHALL use an LLM to analyze the input text and extract key information.

#### Scenario: Conversational Noise Filter
- **WHEN** the user provides raw conversational text
- **THEN** the system SHALL remove filler words, repetitions, and irrelevant conversational markers before further processing

#### Scenario: Narrative Structure Extraction
- **WHEN** the text is analyzed
- **THEN** the content SHALL be structured into Introduction, Body (Key Points), and Conclusion

### Requirement: Slide Generation
The system SHALL generate a structured list of slides (10-14 slides) based on the analyzed content.

#### Scenario: Slide Structure
- **WHEN** slides are generated
- **THEN** each slide SHALL contain:
  - A Title (concise, executive)
  - A Body Text (bullet points or short paragraph, max 50 words)
  - A Visual Prompt (description for the image generation model)
  - An Order index (1 to N)

#### Scenario: Slide Count
- **WHEN** processing a standard input (>500 words)
- **THEN** the system SHALL generate between 10 and 14 slides

### Requirement: Slide Text Editing
The system SHALL allow users to modify the generated title and body text of any slide.

#### Scenario: Update Slide Content
- **WHEN** the user edits the text of a slide
- **THEN** the changes are persisted
- **AND** the image is NOT automatically regenerated (user must trigger it manually if desired)
