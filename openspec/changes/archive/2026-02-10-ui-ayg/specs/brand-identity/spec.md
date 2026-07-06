## ADDED Requirements

### Requirement: Corporate Logo Asset
The system SHALL include the a&g corporate logo as an accessible asset in the frontend.

#### Scenario: Logo File Location
- **WHEN** the frontend application is built
- **THEN** the logo SHALL be located at `frontend/src/assets/logo-ayg.png`
- **AND** it SHALL be accessible for import in React components

#### Scenario: Logo Component
- **WHEN** the Logo component is rendered
- **THEN** it SHALL display the a&g logo image
- **AND** it SHALL support size variants: 'sm' (h-8), 'md' (h-12), 'lg' (h-16)
- **AND** it SHALL include appropriate alt text for accessibility

### Requirement: Corporate Color Palette
The system SHALL define and use the a&g corporate color palette throughout the UI.

#### Scenario: Primary Color Configuration
- **WHEN** Tailwind CSS is configured
- **THEN** it SHALL include custom theme colors:
  - `ayg-primary`: #5F8FA3 (corporate blue-gray)
  - `ayg-primary-dark`: #4A7289 (for hover/active states)
  - `ayg-primary-light`: #7FA5B8 (for soft backgrounds)

#### Scenario: Tailwind Color Classes
- **WHEN** developers use Tailwind classes
- **THEN** they SHALL be able to use:
  - `bg-ayg-primary`, `text-ayg-primary`, `border-ayg-primary`
  - `bg-ayg-primary-dark`, `text-ayg-primary-dark`, `border-ayg-primary-dark`
  - `bg-ayg-primary-light`, `text-ayg-primary-light`, `border-ayg-primary-light`

### Requirement: Color Application in UI
The system SHALL apply the corporate color scheme to key UI elements.

#### Scenario: Navigation Styling
- **WHEN** the sidebar/navigation is rendered
- **THEN** it SHALL use `bg-ayg-primary` for the background
- **AND** navigation text SHALL be white for proper contrast
- **AND** active navigation items SHALL be highlighted with `bg-ayg-primary-dark` or similar

#### Scenario: Primary Buttons
- **WHEN** primary action buttons are rendered
- **THEN** they SHALL use `bg-ayg-primary` as the base color
- **AND** they SHALL use `bg-ayg-primary-dark` for hover state
- **AND** text SHALL be white for readability

#### Scenario: Accent Elements
- **WHEN** UI elements require visual emphasis (borders, badges, links)
- **THEN** they SHALL use colors from the `ayg` palette
- **AND** color contrast SHALL meet WCAG AA standards (≥ 4.5:1 for text)

### Requirement: Responsive Logo Display
The logo SHALL adapt to different screen sizes appropriately.

#### Scenario: Desktop Logo Size
- **WHEN** the application is viewed on desktop (≥768px width)
- **THEN** the logo SHALL be displayed at size 'md' (h-12, 48px height)

#### Scenario: Mobile Logo Behavior
- **WHEN** the application is viewed on mobile (<768px width)
- **THEN** the logo MAY be hidden or reduced to size 'sm' (h-8, 32px height)
- **AND** navigation functionality SHALL remain intact
