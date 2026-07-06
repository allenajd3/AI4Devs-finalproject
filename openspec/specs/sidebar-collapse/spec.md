# sidebar-collapse Specification

## Purpose
Collapsible sidebar navigation that persists its state in localStorage and adapts logo size and nav labels to the collapsed state.

## Requirements

### Requirement: Sidebar toggle button
The sidebar SHALL include a visible button to toggle between expanded and collapsed states in both modes.

#### Scenario: Toggle to collapsed
- **WHEN** the user clicks the toggle button while the sidebar is expanded
- **THEN** the sidebar SHALL animate to collapsed width (64px) and hide all nav labels and footer text

#### Scenario: Toggle to expanded
- **WHEN** the user clicks the toggle button while the sidebar is collapsed
- **THEN** the sidebar SHALL animate back to expanded width (256px) and show all nav labels and footer text

### Requirement: Collapsed icon-only navigation
When collapsed, the sidebar SHALL display only nav icons, centered, without labels.

#### Scenario: Icon visible when collapsed
- **WHEN** the sidebar is in collapsed state
- **THEN** each nav link SHALL show only its icon, centered horizontally, with no label text

#### Scenario: Tooltip on hover when collapsed
- **WHEN** the user hovers over a nav icon in collapsed state
- **THEN** a tooltip SHALL display the nav item's label

### Requirement: Logo adapts to collapsed state
The AyG logo in the sidebar header SHALL resize to fit the collapsed width.

#### Scenario: Logo size in expanded state
- **WHEN** the sidebar is expanded
- **THEN** the Logo SHALL render at size="md" with the "Presentaciones IA" subtitle visible

#### Scenario: Logo size in collapsed state
- **WHEN** the sidebar is collapsed
- **THEN** the Logo SHALL render at size="sm" and the "Presentaciones IA" subtitle SHALL be hidden

### Requirement: Collapse state persistence
The sidebar collapse state SHALL persist across page reloads using localStorage.

#### Scenario: State saved on toggle
- **WHEN** the user toggles the sidebar
- **THEN** the new state SHALL be saved to localStorage under key "sidebar-collapsed"

#### Scenario: State restored on load
- **WHEN** the page loads and "sidebar-collapsed" exists in localStorage
- **THEN** the sidebar SHALL initialize in the saved state without animation
