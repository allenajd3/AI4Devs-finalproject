## MODIFIED Requirements

### Requirement: Navigation System
The application SHALL provide a persistent lateral navigation menu with corporate branding that supports collapsible behavior.

#### Scenario: Menu Structure
- **WHEN** the user is in any view
- **THEN** the menu SHALL display links to: "Crear", "Proyectos", "Ajustes"
- **AND** the active route SHALL be visually highlighted

#### Scenario: Corporate Logo Integration
- **WHEN** the navigation sidebar is expanded
- **THEN** it SHALL display the a&g logo at size="md" at the top of the sidebar
- **AND** the "Presentaciones IA" subtitle SHALL be visible below the logo
- **AND** the logo SHALL be centered horizontally

#### Scenario: Corporate Logo in Collapsed State
- **WHEN** the navigation sidebar is collapsed
- **THEN** it SHALL display the a&g logo at size="sm", centered
- **AND** the "Presentaciones IA" subtitle SHALL be hidden

#### Scenario: Corporate Color Scheme
- **WHEN** the navigation sidebar is rendered
- **THEN** it SHALL use the corporate color palette (ayg-primary for background)
- **AND** menu items SHALL use white text for contrast
- **AND** active/hover states SHALL use ayg-primary-dark or appropriate variant
