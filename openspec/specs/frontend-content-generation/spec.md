# frontend-content-generation Specification

## Purpose
Enable real-time content generation feedback in the frontend using Server-Sent Events (SSE).

## Requirements

### Requirement: SSE Connection for Content Generation
The frontend SHALL connect to the backend SSE endpoint to receive real-time progress updates during content generation.

#### Scenario: Establish SSE Connection
- **WHEN** content generation is initiated for a project
- **THEN** the frontend SHALL open an EventSource connection to `POST /api/projects/{id}/generate-content`
- **AND** it SHALL listen for `progress` and `complete` events

#### Scenario: Display Progress Messages
- **WHEN** a `progress` event is received
- **THEN** the frontend SHALL display the progress message to the user
- **AND** it SHALL append new messages to a visible list

#### Scenario: Handle Completion
- **WHEN** a `complete` event is received with a projectId
- **THEN** the frontend SHALL close the EventSource connection
- **AND** it SHALL redirect to the project detail page

### Requirement: Automatic Generation After Project Creation
After creating a project, the frontend SHALL automatically trigger content generation.

#### Scenario: Post-Creation Flow
- **WHEN** a user successfully creates a project
- **THEN** the frontend SHALL immediately call the content generation endpoint
- **AND** it SHALL show a progress overlay/modal with real-time updates

### Requirement: Progress UI
The frontend SHALL provide clear visual feedback during content generation.

#### Scenario: Progress Overlay
- **WHEN** content generation is in progress
- **THEN** the UI SHALL display a modal or overlay with:
  - A list of progress messages
  - Auto-scroll to the latest message
  - A loading indicator
- **AND** the user SHALL NOT be able to navigate away during generation

#### Scenario: Error Display
- **WHEN** an error occurs during generation (network error or backend error)
- **THEN** the frontend SHALL display a clear error message
- **AND** it SHALL offer options to retry or cancel

### Requirement: Custom Hook for Generation
The frontend SHALL implement a reusable hook `useContentGeneration` to encapsulate SSE logic.

#### Scenario: Hook Interface
- **WHEN** the hook is used in a component
- **THEN** it SHALL provide:
  - `isGenerating: boolean` state
  - `progress: string[]` state (list of messages)
  - `error: string | null` state
  - `startGeneration(projectId: string)` method
- **AND** it SHALL automatically clean up EventSource on unmount
