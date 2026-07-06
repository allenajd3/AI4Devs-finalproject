## ADDED Requirements

### Requirement: Global Settings
The system SHALL provide a centralized settings page to configure AI generation behavior and output style.

#### Scenario: Settings Configuration
- **WHEN** the user visits "Ajustes"
- **THEN** they can configure:
  - System Prompt (for LLM content generation)
  - Visual Style Prompt (for Nano Banana)
  - Content Orientation (Horizontal/Vertical)

#### Scenario: Default Values
- **WHEN** settings are not provided by the user
- **THEN** the system applies defaults:
  - System Prompt: "Professional executive summary..."
  - Style: "Corporate, clean, minimalist, high-contrast text"
  - Orientation: Horizontal (16:9)

### Requirement: Persistence
The system SHALL persist settings changes for future sessions.

#### Scenario: Save Settings
- **WHEN** the user updates settings
- **THEN** the values are saved to local storage or backend
- **AND** apply to all new projects generated
