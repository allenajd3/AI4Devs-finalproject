# dark-mode Tasks

## 1. Backend - GlobalSettings and API

- [x] 1.1 Add `darkMode` (boolean) to the GlobalSettings entity and persistence (default false when not set)
- [x] 1.2 Add `darkMode` to GlobalSettingsDto and map it in service/controller for GET and PUT
- [x] 1.3 Ensure GET `/api/settings` returns `darkMode` (default false if absent) and PUT accepts and persists `darkMode`

## 2. Frontend - Theme Application

- [x] 2.1 Add `darkMode: boolean` to the GlobalSettings TypeScript type used by the app
- [x] 2.2 Configure Tailwind for class-based dark mode (e.g. `darkMode: 'class'`) and apply `dark` class on root when `darkMode` is true
- [x] 2.3 Load settings (including `darkMode`) early on app load and apply theme to root (sidebar, layout, main content) using `dark:` utility classes for dark palette (e.g. bg-gray-900, text-gray-100)
- [x] 2.4 When settings are saved with a changed `darkMode`, update the applied theme immediately without full reload

## 3. Frontend - Settings Page Toggle

- [x] 3.1 Add a dark mode toggle/switch on the settings page ("/ajustes") labeled e.g. "Modo oscuro"
- [x] 3.2 Populate the toggle from loaded settings (`darkMode`) and include `darkMode` in the payload when saving
- [x] 3.3 Ensure the toggle reflects current state and save button sends the updated `darkMode` value with the rest of GlobalSettings

## 4. Verification

- [x] 4.1 Verify dark mode applies across layout, sidebar, and main views with readable contrast
- [x] 4.2 Verify preference persists after save and after reload (GET returns stored value)
