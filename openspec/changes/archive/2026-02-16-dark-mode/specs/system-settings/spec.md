## ADDED Requirements

### Requirement: Dark Mode in Global Settings
The global settings entity SHALL include a preference for theme (dark mode) and SHALL expose it via the settings API.

#### Scenario: Settings Entity Includes darkMode
- **WHEN** global settings are defined or persisted
- **THEN** they SHALL include a field `darkMode` (boolean)
- **AND** the field SHALL indicate whether the user has enabled dark theme (true) or uses light theme (false)

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
