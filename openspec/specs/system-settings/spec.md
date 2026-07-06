## ADDED Requirements

### Requirement: Global Settings Entity
The system SHALL store global configuration for presentation generation applied to all projects.

#### Scenario: Settings Fields
- **WHEN** global settings are defined
- **THEN** they SHALL include: systemPrompt (context and tone for presentations), contentOrientation (focus of the content), visualStyle (desired graphic aspect for slides)

#### Scenario: Single Global Instance
- **WHEN** the application reads or writes settings
- **THEN** there SHALL be a single global instance (e.g. one row or default key)
- **AND** these values SHALL be used as context when generating each slide image (in a later step)

### Requirement: Default Settings
When no custom settings exist, the system SHALL use sensible defaults (professional, corporate tone and style).

#### Scenario: Defaults Applied
- **WHEN** global settings are requested and none are stored
- **THEN** the system SHALL return default values for systemPrompt, contentOrientation, and visualStyle
