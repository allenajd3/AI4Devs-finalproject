# theme Specification

## Purpose
Define support for light and dark theme in the application: preference storage, application rules, and accessibility.

## Requirements

### Requirement: Theme Modes
The application SHALL support two theme modes: light (current corporate look) and dark (black tones). The active mode SHALL be determined by the global setting `darkMode`.

#### Scenario: Dark Mode Active
- **WHEN** the global setting `darkMode` is true
- **THEN** the application SHALL apply the dark theme across all views (layout, sidebar, pages, forms)
- **AND** backgrounds SHALL use dark tones (e.g. gray-900, gray-950)
- **AND** text SHALL use light tones (e.g. gray-100, gray-200) for readability

#### Scenario: Light Mode Active
- **WHEN** the global setting `darkMode` is false or not set
- **THEN** the application SHALL use the current tonalidad (corporate a&g palette, light backgrounds)
- **AND** existing color and typography rules SHALL apply as today

### Requirement: Theme Application Mechanism
The frontend SHALL apply the selected theme consistently from a single source of truth (e.g. class on root element or CSS variables).

#### Scenario: Theme Applied on Load
- **WHEN** the application loads and settings have been fetched
- **THEN** the UI SHALL render using the theme corresponding to the `darkMode` value from settings
- **AND** the theme SHALL apply to the root layout (sidebar, main content area, and all child views)

#### Scenario: Theme Change on Save
- **WHEN** the user saves settings with a changed `darkMode` value
- **THEN** the application SHALL update the visible theme immediately without requiring a full page reload

### Requirement: Dark Theme Accessibility
When dark mode is active, the system SHALL maintain sufficient contrast for text and interactive elements.

#### Scenario: Contrast in Dark Mode
- **WHEN** dark mode is active
- **THEN** text on dark backgrounds SHALL meet WCAG AA contrast (at least 4.5:1 for normal text)
- **AND** interactive elements (buttons, links, borders) SHALL remain clearly visible and distinguishable
