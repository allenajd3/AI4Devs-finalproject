# project-management Specification

## Purpose
TBD - created by archiving change implement-project-mgmt. Update Purpose after archive.
## Requirements
### Requirement: Project Entity
The system SHALL support a `Project` entity to manage the lifecycle of a presentation generation task.

#### Scenario: Project Attributes
- **WHEN** a project is created
- **THEN** it SHALL have a unique ID (UUID), a user-defined title, a creation timestamp, the original content (text), and a status (DRAFT, GENERATING, COMPLETED, ERROR).

### Requirement: Create Project
The system SHALL allow creating a new project from a text input or file upload.

#### Scenario: Create from Text
- **WHEN** the user submits raw text
- **THEN** a new Project is created with status DRAFT and the text is stored
- **AND** the system returns the newly created Project with its ID.

#### Scenario: Create from File (Simulated)
- **WHEN** the user uploads a .txt file
- **THEN** a new Project is created with status DRAFT and the file content is stored as text (frontend handles file reading and sends content as string).

### Requirement: List Projects
The system SHALL provide a list of all existing projects ordered by creation date (newest first).

#### Scenario: View Projects
- **WHEN** the user requests the project list
- **THEN** the system returns a summary of all projects (id, title, date, status).

### Requirement: Get Project Details
The system SHALL allow retrieving the full details of a specific project by ID.

#### Scenario: Get Project
- **WHEN** the user requests a project by ID
- **THEN** the system returns the project details including the original content.

### Requirement: Delete Project
The system SHALL allow deleting a project and all its associated data.

#### Scenario: Delete Action
- **WHEN** the user triggers delete on a project
- **THEN** the project is permanently removed from the system.

