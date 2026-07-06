## 1. Project Setup

- [x] 1.1 Initialize Vite project with React + TypeScript template in `/frontend` directory
- [x] 1.2 Install and configure Tailwind CSS
- [x] 1.3 Install dependencies: `react-router-dom`, `@tanstack/react-query`, `axios`, `@stomp/stompjs`, `sockjs-client`
- [x] 1.4 Configure Vite proxy to backend (`http://localhost:8080`)
- [x] 1.5 Create basic folder structure (`components`, `pages`, `services`, `hooks`, `types`)

## 2. Routing & Layout

- [x] 2.1 Set up React Router with routes: `/crear`, `/proyectos`, `/proyectos/:id`, `/ajustes`
- [x] 2.2 Create `Layout` component with persistent sidebar navigation
- [x] 2.3 Implement sidebar menu with links to: Crear, Proyectos, Ajustes
- [x] 2.4 Add active route highlighting in sidebar

## 3. API Integration

- [x] 3.1 Create Axios instance with base URL configuration
- [x] 3.2 Set up TanStack Query client provider
- [x] 3.3 Create API service functions: `createProject`, `getProjects`, `getProjectById`, `deleteProject`
- [x] 3.4 Create custom hooks: `useProjects`, `useProjectById`, `useCreateProject`, `useDeleteProject`

## 4. Vista CREAR

- [x] 4.1 Create `CrearPage` component with textarea and file upload button
- [x] 4.2 Implement text validation (minimum 100 characters)
- [x] 4.3 Implement file upload handler (.txt files only)
- [x] 4.4 Disable "Generar presentación" button when validation fails
- [x] 4.5 Connect to `useCreateProject` mutation
- [x] 4.6 Add loading state and redirect to project detail on success

## 5. Vista PROYECTOS

- [x] 5.1 Create `ProyectosPage` component with responsive grid layout
- [x] 5.2 Create `ProjectTile` component
- [x] 5.3 Fetch projects using `useProjects` hook
- [x] 5.4 Display loading and error states
- [x] 5.5 Implement click handler to navigate to detail view

## 6. Vista DETALLE

- [x] 6.1 Create `ProyectoDetallePage` component
- [x] 6.2 Fetch project details using `useProjectById` hook
- [x] 6.3 Display project title and original content
- [x] 6.4 Add placeholder section for slides ("Coming soon")
- [x] 6.5 Implement "Eliminar" button with confirmation dialog
- [x] 6.6 Add "Descargar PDF" placeholder button (disabled)

## 7. Vista AJUSTES

- [x] 7.1 Create `AjustesPage` component with "Coming soon" message

## 8. WebSocket Preparation

- [x] 8.1 Install WebSocket dependencies
- [x] 8.2 Create WebSocket service with STOMP client configuration
- [x] 8.3 Add graceful connection failure handling (backend not ready yet)

## 9. Styling & Polish

- [x] 9.1 Apply consistent Tailwind styling across all views
- [x] 9.2 Add responsive design for mobile/tablet
- [x] 9.3 Implement loading spinners and skeleton states
- [x] 9.4 Add error boundaries for graceful error handling
