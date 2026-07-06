# settings-ui Specification

## Purpose
TBD - created by archiving change config-page. Update Purpose after archive.
## Requirements
### Requirement: Settings Form Display
The settings page SHALL display a form with three large textarea fields for editing global configuration parameters.

#### Scenario: Form Fields Rendering
- **WHEN** the user navigates to "/ajustes"
- **THEN** the page SHALL display three labeled textarea fields: "Prompt del Sistema", "Orientación del Contenido", and "Estilo Visual"
- **AND** each textarea SHALL have an associated icon (📝, 🎯, 🎨 respectively)
- **AND** each textarea SHALL be large enough for multi-line editing (minimum 6 rows)

#### Scenario: Placeholder Text Display
- **WHEN** a settings field is empty
- **THEN** the textarea SHALL display descriptive placeholder text explaining the field's purpose
- **AND** the placeholder SHALL provide an example of the expected content format

#### Scenario: Field Labeling
- **WHEN** the form is rendered
- **THEN** each field SHALL have a clear, visible label in Spanish
- **AND** labels SHALL use consistent typography (font-medium, appropriate text color)

### Requirement: Settings Data Loading
The settings page SHALL fetch and display current global configuration values on initial load.

#### Scenario: Successful Settings Load
- **WHEN** the settings page is mounted
- **THEN** a GET request SHALL be sent to `/api/settings`
- **AND** upon success, the form fields SHALL be populated with the returned values
- **AND** empty/null values SHALL display as empty textareas with placeholders visible

#### Scenario: Loading State Display
- **WHEN** settings data is being fetched
- **THEN** a loading spinner or indicator SHALL be displayed
- **AND** the form fields SHALL be disabled until data loads

#### Scenario: Load Error Handling
- **WHEN** the GET request to `/api/settings` fails
- **THEN** an error message SHALL be displayed explaining the failure
- **AND** the user SHALL be able to retry loading the settings

### Requirement: Settings Data Persistence
The settings page SHALL allow users to save modified configuration values.

#### Scenario: Save Settings Action
- **WHEN** the user clicks the "Guardar Configuración" button
- **THEN** a PUT request SHALL be sent to `/api/settings` with all three field values
- **AND** the request body SHALL match the `GlobalSettings` interface structure

#### Scenario: Successful Save Feedback
- **WHEN** the PUT request completes successfully
- **THEN** a success message SHALL be displayed ("Configuración guardada correctamente")
- **AND** the message SHALL automatically disappear after 4 seconds
- **AND** the settings query cache SHALL be invalidated to reflect the new values

#### Scenario: Save Error Feedback
- **WHEN** the PUT request fails
- **THEN** an error message SHALL be displayed with the failure reason
- **AND** the form data SHALL remain intact (not cleared)
- **AND** the user SHALL be able to attempt saving again

#### Scenario: Save Button States
- **WHEN** settings are being saved
- **THEN** the "Guardar Configuración" button SHALL be disabled
- **AND** the button SHALL display a loading indicator or text ("Guardando...")
- **WHEN** save is complete (success or error)
- **THEN** the button SHALL be re-enabled for another submission

### Requirement: Corporate Design Consistency
The settings page SHALL follow the established corporate visual identity.

#### Scenario: Color Scheme Application
- **WHEN** the settings page is rendered
- **THEN** form elements SHALL use the ayg corporate color palette
- **AND** the "Guardar" button SHALL use `bg-ayg-primary` with `hover:bg-ayg-primary-dark`
- **AND** focused textareas SHALL display a `ring-ayg-primary` focus ring

#### Scenario: Layout Consistency
- **WHEN** the settings page is rendered
- **THEN** the page layout SHALL be consistent with other pages (same max-width, padding, spacing)
- **AND** the form SHALL use vertical layout with full-width fields
- **AND** spacing between form elements SHALL follow Tailwind spacing conventions (gap-4, gap-6)

#### Scenario: Typography Consistency
- **WHEN** text is displayed on the settings page
- **THEN** headings, labels, and body text SHALL use the same font stack as other pages
- **AND** text sizes and weights SHALL follow established patterns

### Requirement: Type Safety and API Integration
The settings page SHALL use properly typed interfaces for all data structures.

#### Scenario: TypeScript Interface Definition
- **WHEN** the component imports settings types
- **THEN** a `GlobalSettings` interface SHALL be defined with fields: `systemPrompt`, `contentOrientation`, `visualStyle`, `darkMode`
- **AND** text fields SHALL be typed as `string | null` and `darkMode` as `boolean`

#### Scenario: TanStack Query Integration
- **WHEN** the component fetches or updates settings
- **THEN** it SHALL use TanStack Query's `useQuery` for GET operations
- **AND** it SHALL use TanStack Query's `useMutation` for PUT operations
- **AND** the query key SHALL be `['settings']`

#### Scenario: Axios HTTP Client Usage
- **WHEN** API calls are made
- **THEN** they SHALL use Axios with consistent configuration
- **AND** the base URL SHALL respect the application's API base path
- **AND** errors SHALL be handled with try-catch or mutation error callbacks

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

