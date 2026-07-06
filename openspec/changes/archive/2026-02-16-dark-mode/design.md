# Design: Dark Mode

## Context

La aplicación es un frontend React (Vite) con backend Spring Boot. La configuración global se gestiona en "/ajustes", con GET/PUT a `/api/settings` y modelo `GlobalSettings` (systemPrompt, contentOrientation, visualStyle). La UI usa Tailwind con paleta corporativa a&g (fondos claros, ayg-primary). No existe actualmente soporte de tema; el aspecto es siempre claro.

## Goals / Non-Goals

**Goals:**
- Permitir activar/desactivar modo oscuro desde la página de configuración.
- Aplicar tema en toda la app: fondos oscuros y texto claro cuando está activo; tonalidad actual cuando está desactivado.
- Persistir la preferencia en backend (GlobalSettings) para que sea consistente entre sesiones.
- Mantener contraste accesible (WCAG AA) en modo oscuro.

**Non-Goals:**
- Detección de preferencia del sistema (prefer-color-scheme); solo control manual desde ajustes.
- Temas adicionales más allá de claro/oscuro.
- Modo oscuro solo en vistas concretas (p. ej. solo presentación); es global.

## Decisions

1. **Campo en backend: `darkMode` (boolean)**  
   Se añade `darkMode` a la entidad y DTO de GlobalSettings. Alternativa considerada: `theme: "light" | "dark"` para futura extensión; se elige boolean por simplicidad y porque no hay planes inmediatos de más temas.

2. **Aplicación del tema en frontend vía clase en raíz**  
   Se aplica una clase en el elemento raíz del app (p. ej. `dark` en `<html>` o en el div raíz) cuando `darkMode === true`; Tailwind con variante `dark:` se usa para estilos oscuros. Alternativa: CSS variables (custom properties) con dos conjuntos de valores; se elige Tailwind `dark:` para reutilizar utilidades existentes y mantener consistencia con el resto del proyecto.

3. **Origen de la verdad: API de settings**  
   El tema se lee al cargar la app desde GET `/api/settings` (junto con el resto de configuración). No se usa solo localStorage para evitar desincronización; el frontend puede cachear en contexto React y aplicar al montar. Alternativa: solo localStorage; se descarta para mantener una única fuente de verdad en backend.

4. **Paleta oscura**  
   Fondos: grises muy oscuros / negro (p. ej. `gray-900`, `gray-950`). Texto: `gray-100`/`gray-200`. Bordes y superficies secundarias: `gray-700`/`gray-800`. Acentos (botones primarios, enlaces) pueden mantener ayg-primary o una variante más clara para contraste. Se documentan en la spec de `theme` los tokens concretos.

5. **Sin migración de datos**  
   El nuevo campo `darkMode` tendrá valor por defecto `false` en código; entidades existentes sin el campo se consideran "tema claro". Si el almacenamiento es BBDD, añadir columna con default false; no se requiere script de migración de datos.

## Risks / Trade-offs

- **[Riesgo]** Primera carga puede mostrar tema claro un instante hasta que llegue la respuesta de settings.  
  **Mitigación:** Cargar settings lo antes posible (App o layout raíz); opcionalmente guardar última preferencia en localStorage como hint para la primera pintura (sin sustituir a la API como fuente de verdad tras la carga).

- **[Trade-off]** No respetar `prefer-color-scheme` puede frustrar a usuarios que esperan que el sistema decida.  
  **Aceptado:** El alcance es solo control manual; una mejora futura podría ser "usar preferencia del sistema" como opción.

## Migration Plan

1. Backend: añadir campo `darkMode` a modelo, DTO, y persistencia; actualizar GET/PUT de `/api/settings`. Desplegar backend.
2. Frontend: añadir soporte de tema (clase raíz + estilos `dark:`), contexto o estado de tema desde settings, y toggle en página de ajustes. Desplegar frontend.
3. Rollback: revertir despliegues; el campo nuevo en backend es opcional (default false), no rompe clientes antiguos.

## Open Questions

- Ninguno bloqueante. Opcional: si se quiere evitar "flash" de tema claro al cargar, definir si se usa localStorage como hint inicial y cómo se reconcilia con la API.
