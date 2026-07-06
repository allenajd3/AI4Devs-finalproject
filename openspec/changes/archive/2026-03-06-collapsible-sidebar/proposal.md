## Why

El menú lateral ocupa siempre 256px de ancho, reduciendo el espacio disponible para el contenido principal. Los usuarios necesitan poder colapsar la barra lateral para maximizar el área de trabajo, especialmente al revisar slides o trabajar en pantallas pequeñas.

## What Changes

- Añadir un botón de toggle en la barra lateral para alternar entre modo expandido y colapsado.
- En modo colapsado, la sidebar pasa a mostrar únicamente los iconos de cada enlace (sin texto), con tooltips al hacer hover.
- El logo de AyG en la cabecera de la sidebar se ajusta a un tamaño menor (`sm`) en modo colapsado, centrado.
- El footer "AyG © 2026" se oculta en modo colapsado.
- El estado de colapso se persiste en `localStorage` para mantenerlo entre sesiones.
- El área de contenido principal se expande suavemente con transición CSS al colapsar.

## Capabilities

### New Capabilities
- `sidebar-collapse`: Control de colapso/expansión del menú lateral con persistencia de estado y ajuste del logo.

### Modified Capabilities
- `frontend-ui`: La navegación lateral cambia su comportamiento visual y de espacio.

## Impact

- `frontend/src/components/Layout.tsx` — lógica de toggle, clases condicionales de ancho, render condicional de labels
- `frontend/src/components/Logo.tsx` — ya soporta `size="sm"`, se usará directamente
- Sin cambios en backend ni API
