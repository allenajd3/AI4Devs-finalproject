## ADDED Requirements

### Requirement: Dark Mode Toggle
The settings page SHALL include a control to enable or disable dark mode, and SHALL load and save this value with the rest of the global configuration.

#### Scenario: Dark Mode Toggle Display
- **WHEN** the user navigates to "/ajustes"
- **THEN** the page SHALL display a control (e.g. toggle or switch) labeled for dark mode (e.g. "Modo oscuro" or "Activar modo oscuro")
- **AND** the control SHALL reflect the current value of `darkMode` from the loaded settings

#### Scenario: Dark Mode Toggle Load
- **WHEN** the settings page loads and GET `/api/settings` returns successfully
- **THEN** the dark mode control SHALL be set to the value of `darkMode` from the response
- **AND** if `darkMode` is absent or null, the control SHALL default to off (light theme)

#### Scenario: Dark Mode Toggle Save
- **WHEN** the user changes the dark mode control and clicks "Guardar Configuración"
- **THEN** the PUT request to `/api/settings` SHALL include the `darkMode` field in the request body
- **AND** the value SHALL match the state of the toggle (true when enabled, false when disabled)

### Requirement: GlobalSettings Type Includes Dark Mode
The settings form and API integration SHALL treat `darkMode` as part of the global settings type.

#### Scenario: TypeScript Interface Includes darkMode
- **WHEN** the frontend uses the GlobalSettings type for the settings page
- **THEN** the type SHALL include a field `darkMode: boolean`
- **AND** the field SHALL be used when populating the form and when building the PUT request body
