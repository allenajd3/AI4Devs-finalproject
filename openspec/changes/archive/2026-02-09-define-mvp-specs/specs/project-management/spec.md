## ADDED Requirements

### Requirement: Project Entity
The system SHALL support a `Project` entity to manage the lifecycle of a presentation generation task.

#### Scenario: Project Attributes
- **WHEN** a project is created
- **THEN** it SHALL have a unique ID, a user-defined title, a creation timestamp, and a status (DRAFT, GENERATING, COMPLETED, ERROR)

### Requirement: Create Project
The system SHALL allow creating a new project from a text input or file upload.

#### Scenario: Create from Text
- **WHEN** the user submits raw text
- **THEN** a new Project is created with status DRAFT and the text is stored

#### Scenario: Create from File
- **WHEN** the user uploads a .txt file
- **THEN** a new Project is created with status DRAFT and the file content is stored

### Requirement: List Projects
The system SHALL provide a list of all existing projects ordered by creation date (newest first).

#### Scenario: View Projects
- **WHEN** the user requests the project list
- **THEN** the system returns a summary of all projects (id, title, date, status, preview_image_url)

### Requirement: Delete Project
The system SHALL allow deleting a project and all its associated data (slides, images).

#### Scenario: Delete Action
- **WHEN** the user triggers delete on a project
- **THEN** the project and all related resources are permanently removed
