## MODIFIED Requirements

### Requirement: Settings View
The "Ajustes" view SHALL display a functional settings form for editing global configuration.

#### Scenario: Settings Form Display
- **WHEN** the user navigates to "/ajustes"
- **THEN** a functional settings form SHALL be displayed
- **AND** the form SHALL contain three editable textarea fields
- **AND** the form SHALL have a "Guardar Configuración" submit button

#### Scenario: Initial Data Load
- **WHEN** the settings view is mounted
- **THEN** a GET request SHALL be sent to `/api/settings`
- **AND** the form SHALL display current configuration values
- **AND** a loading state SHALL be shown while data is being fetched

#### Scenario: Save Settings
- **WHEN** the user modifies settings and clicks "Guardar Configuración"
- **THEN** a PUT request SHALL be sent to `/api/settings` with the updated values
- **AND** success or error feedback SHALL be displayed to the user
