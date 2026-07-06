# Proposal: Dark Mode

## Why

Los usuarios necesitan poder usar la aplicación con un tema oscuro para reducir fatiga visual en entornos con poca luz o por preferencia personal. Activar el modo oscuro desde configuración permite mantener la coherencia con el resto del flujo de ajustes y persistir la preferencia.

## What Changes

- Añadir un control en la página de configuración ("/ajustes") para activar o desactivar el modo oscuro.
- Cuando el modo oscuro está **activado**: la interfaz utiliza tonos negros (fondos oscuros, texto claro, contraste adecuado).
- Cuando el modo oscuro está **desactivado**: se mantiene la tonalidad actual (paleta corporativa a&g, fondos claros).
- La preferencia de tema se guarda en la configuración global y se aplica en toda la aplicación (navegación, páginas, formularios).
- Backend: ampliar el modelo de configuración global para incluir la preferencia de tema (por ejemplo `darkMode: boolean`).

## Capabilities

### New Capabilities

- `theme`: Define el soporte de tema claro/oscuro en la aplicación: valores posibles (light/dark), persistencia de la preferencia, y reglas de aplicación (tonos negros en oscuro, tonalidad actual en claro). Incluye criterios de contraste y accesibilidad para el modo oscuro.

### Modified Capabilities

- `settings-ui`: Añadir en la página de ajustes un control (toggle o similar) para activar/desactivar el modo oscuro; cargar y guardar el valor junto con el resto de la configuración.
- `system-settings`: Incluir en la entidad de configuración global un campo para la preferencia de tema (p. ej. `darkMode` o `theme`) y exponerlo en la API GET/PUT de `/api/settings`; definir valor por defecto (p. ej. claro).

## Impact

- **Frontend**: página de configuración (nuevo control), aplicación de clases/variables CSS según tema en layout, cabecera, sidebar y páginas principales; posible uso de contexto React o preferencia en localStorage/API para aplicar el tema al cargar.
- **Backend**: modelo `GlobalSettings` y DTO (nuevo campo), migración o actualización de datos si aplica, sin cambios en otros endpoints.
- **Diseño/UI**: definición de la paleta oscura (negros/grises oscuros, texto claro) y su aplicación coherente en todos los componentes afectados.
