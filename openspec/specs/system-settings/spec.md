# system-settings Specification

## Purpose
Define global settings for presentation generation that apply to all projects.

## Requirements

### Requirement: Global Settings Entity
The system SHALL store global configuration for presentation generation applied to all projects.

#### Scenario: Settings Fields
- **WHEN** global settings are defined
- **THEN** they SHALL include: systemPrompt (context and tone for presentations), contentOrientation (focus of the content), visualStyle (desired graphic aspect for slides), darkMode (theme preference: true for dark, false for light)

#### Scenario: Single Global Instance
- **WHEN** the application reads or writes settings
- **THEN** there SHALL be a single global instance (e.g. one row or default key)
- **AND** these values SHALL be used as context when generating each slide image (in a later step)

### Requirement: Default Settings
When no custom settings exist, the system SHALL use sensible defaults (professional, corporate tone and style).

#### Scenario: Defaults Applied
- **WHEN** global settings are requested and none are stored
- **THEN** the system SHALL return default values for systemPrompt, contentOrientation, and visualStyle

### Requirement: Dark Mode in Global Settings
The global settings entity SHALL include a preference for theme (dark mode) and SHALL expose it via the settings API.

#### Scenario: API Exposes darkMode
- **WHEN** a client calls GET `/api/settings`
- **THEN** the response body SHALL include the `darkMode` field
- **AND** the value SHALL be the stored preference or the default when none is stored

#### Scenario: API Accepts darkMode on Update
- **WHEN** a client sends PUT `/api/settings` with a JSON body
- **THEN** the body MAY include `darkMode` (boolean)
- **AND** when present, the system SHALL persist the value and return it on subsequent GET requests

#### Scenario: Default for darkMode
- **WHEN** global settings are requested and no value for `darkMode` has been stored
- **THEN** the system SHALL treat the theme as light (dark mode off)
- **AND** GET `/api/settings` SHALL return `darkMode: false` (or equivalent) when no custom value exists
