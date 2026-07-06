## 1. Domain & Persistence

- [x] 1.1 Add Spring Data JPA and H2 database dependencies to `pom.xml`
- [x] 1.2 Create `Project` entity with ID, title, content, status (Enum), createdAt, updatedAt
- [x] 1.3 Create `ProjectRepository` interface extending `JpaRepository`

## 2. Business Logic

- [x] 2.1 Create `ProjectService` interface and implementation
- [x] 2.2 Implement `createProject(String title, String content)` method
- [x] 2.3 Implement `getAllProjects()` method (sorted by date desc)
- [x] 2.4 Implement `getProjectById(UUID id)` method
- [x] 2.5 Implement `deleteProject(UUID id)` method

## 3. API Layer

- [x] 3.1 Create `ProjectController` with `/api/projects` base path
- [x] 3.2 Implement `POST /` endpoint for project creation (with validation)
- [x] 3.3 Implement `GET /` endpoint for listing projects
- [x] 3.4 Implement `GET /{id}` endpoint for getting details
- [x] 3.5 Implement `DELETE /{id}` endpoint for deletion
- [x] 3.6 Create DTOs (`ProjectCreateRequest`, `ProjectResponse`, `ProjectSummaryResponse`)
