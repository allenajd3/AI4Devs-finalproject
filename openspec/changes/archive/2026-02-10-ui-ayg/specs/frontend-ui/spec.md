## MODIFIED Requirements

### Requirement: Navigation System
The application SHALL provide a persistent lateral navigation menu with corporate branding.

#### Scenario: Menu Structure
- **WHEN** the user is in any view
- **THEN** the menu SHALL display links to: "Crear", "Proyectos", "Ajustes"
- **AND** the active route SHALL be visually highlighted

#### Scenario: Corporate Logo Integration
- **WHEN** the navigation sidebar is rendered
- **THEN** it SHALL display the a&g logo at the top of the sidebar
- **AND** the logo SHALL be centered horizontally
- **AND** there SHALL be visual separation (divider or spacing) between the logo and menu items

#### Scenario: Corporate Color Scheme
- **WHEN** the navigation sidebar is rendered
- **THEN** it SHALL use the corporate color palette (ayg-primary for background)
- **AND** menu items SHALL use white text for contrast
- **AND** active/hover states SHALL use ayg-primary-dark or appropriate variant
