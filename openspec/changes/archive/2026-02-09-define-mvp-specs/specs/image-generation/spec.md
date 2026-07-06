## ADDED Requirements

### Requirement: Image Generation Service
The system SHALL integrate with Nano Banana to generate high-quality images for each slide based on a text prompt.

#### Scenario: Generate Slide Image
- **WHEN** a slide is created or regenerated
- **THEN** the system SHALL call the Nano Banana API
- **AND** the system SHALL store the generated image URL in the slide entity

### Requirement: Regenerate Image
The system SHALL allow users to regenerate the image for a specific slide without affecting the text.

#### Scenario: Manual Regenerate
- **WHEN** the user clicks "Regenerate Image"
- **THEN** the system SHALL invoke the Nano Banana API again with the current prompt
- **AND** the image URL SHALL be updated

### Requirement: Image Storage
The system SHALL store generated images in Supabase Storage for persistence and public access.

#### Scenario: Store Image
- **WHEN** Nano Banana returns an image
- **THEN** the system SHALL upload it to a dedicated bucket in Supabase
- **AND** the public URL SHALL be saved in the database
