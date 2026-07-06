## ADDED Requirements

### Requirement: Project Structure
The backend SHALL be initialized as a standard Maven project within a `backend/` directory at the repository root.

#### Scenario: Verify Directory Structure
- **WHEN** the project is initialized
- **THEN** the `backend/` directory exists
- **AND** it contains `src/main/java`, `src/main/resources`, `src/test/java`
- **AND** it contains a valid `pom.xml`

### Requirement: Build Configuration
The project SHALL be configured to use Maven with Java 21 and Spring Boot 4.0.x.

#### Scenario: Verify Build Configuration
- **WHEN** `mvn clean install` is executed in `backend/`
- **THEN** the build completes successfully
- **AND** the artifact is generated

### Requirement: Application Entry Point
The application SHALL provide a main class `com.ayg.presentaciones.PresentacionesApplication` annotated with `@SpringBootApplication`.

#### Scenario: Application Startup
- **WHEN** the application is started via `mvn spring-boot:run`
- **THEN** the application starts successfully on port 8080
