## 1. Type Definitions and API Setup

- [x] 1.1 Create `GlobalSettings` interface in `frontend/src/types/settings.ts` with fields: systemPrompt, contentOrientation, visualStyle (all `string | null`)
- [x] 1.2 Add settings API functions in `frontend/src/services/api.ts` or new `settingsApi.ts`: `getSettings()` and `updateSettings(data: GlobalSettings)`
- [x] 1.3 Verify API functions use Axios with correct base URL and error handling

## 2. Settings Form Component Implementation

- [x] 2.1 Update `frontend/src/pages/AjustesPage.tsx` to remove "Coming Soon" placeholder
- [x] 2.2 Add TanStack Query `useQuery` hook to fetch settings with query key `['settings']`
- [x] 2.3 Add TanStack Query `useMutation` hook for updating settings with onSuccess handler
- [x] 2.4 Implement loading state UI (spinner) while settings data is being fetched
- [x] 2.5 Implement error state UI with error message and optional retry button for failed GET
- [x] 2.6 Create form state using React `useState` for three fields (systemPrompt, contentOrientation, visualStyle)
- [x] 2.7 Populate form state with fetched settings data using `useEffect`

## 3. Form UI and Layout

- [x] 3.1 Create page heading "Ajustes de Generación" or similar with appropriate styling
- [x] 3.2 Implement first textarea: label with 📝 icon + "Prompt del Sistema", textarea with 6-8 rows, placeholder text
- [x] 3.3 Implement second textarea: label with 🎯 icon + "Orientación del Contenido", textarea with 6-8 rows, placeholder text
- [x] 3.4 Implement third textarea: label with 🎨 icon + "Estilo Visual", textarea with 6-8 rows, placeholder text
- [x] 3.5 Apply Tailwind CSS styling: vertical layout, full-width fields, `border-gray-300`, `focus:ring-ayg-primary`, `focus:border-ayg-primary`
- [x] 3.6 Add "Guardar Configuración" submit button with corporate styling: `bg-ayg-primary`, `hover:bg-ayg-primary-dark`, `text-white`, appropriate padding and shadow

## 4. Form Interaction and Feedback

- [x] 4.1 Implement `onChange` handlers for each textarea to update form state
- [x] 4.2 Implement `onSubmit` handler that calls mutation with current form values
- [x] 4.3 Disable submit button and show loading state while mutation is pending
- [x] 4.4 Add success message component (green alert) displaying "Configuración guardada correctamente" on successful save
- [x] 4.5 Add error message component (red alert) displaying error details on failed save
- [x] 4.6 Implement auto-dismiss for success message after 4 seconds using `setTimeout`
- [x] 4.7 Configure mutation to invalidate `['settings']` query on success for cache synchronization

## 5. Verification and Testing

- [x] 5.1 Test loading state: verify spinner shows while fetching settings
- [x] 5.2 Test initial data population: verify form fields display current settings from backend
- [x] 5.3 Test empty fields: verify placeholders are visible when settings are null/empty
- [x] 5.4 Test field editing: verify textareas update state correctly on user input
- [x] 5.5 Test successful save: verify PUT request is sent, success message appears, and data persists
- [x] 5.6 Test save error handling: verify error message displays if PUT fails and form data remains intact
- [x] 5.7 Test corporate styling: verify colors, fonts, and layout match rest of application
- [x] 5.8 Test button states: verify button disables during save and re-enables after completion

## 6. Documentation

- [x] 6.1 Update `README.md` to document the settings page functionality and configuration fields
- [x] 6.2 Add inline comments in code for any non-obvious logic (placeholder definitions, timeout durations, etc.)
