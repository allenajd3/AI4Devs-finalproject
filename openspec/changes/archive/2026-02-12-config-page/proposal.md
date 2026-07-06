# Proposal: Página de Configuración Global

## Why

La página de Ajustes actualmente muestra solo un placeholder "Coming Soon". Los usuarios necesitan poder visualizar y modificar la configuración global (systemPrompt, contentOrientation, visualStyle) que afecta cómo se generan las presentaciones. 

El backend ya tiene la API funcionando (`GET /api/settings`, `PUT /api/settings`), pero falta la interfaz de usuario para que los usuarios puedan editar estos valores de forma intuitiva.

## What Changes

- Reemplazar el placeholder "Coming Soon" en `AjustesPage.tsx` con un formulario funcional
- Crear formulario con 3 campos de texto (textareas grandes):
  - 📝 **Prompt del Sistema** - Define el contexto general y tono
  - 🎯 **Orientación del Contenido** - El enfoque de las presentaciones
  - 🎨 **Estilo Visual** - Describe el aspecto gráfico deseado
- Mostrar placeholders con los valores por defecto del backend
- Conectar con API existente para:
  - Cargar configuración actual al montar el componente
  - Guardar cambios cuando el usuario hace submit
- Aplicar diseño corporativo a&g consistente con el resto de la aplicación
- Añadir iconos visuales para cada campo (mejor UX)
- Feedback visual: loading states, success/error messages

## Capabilities

### New Capabilities
- `settings-ui`: Interfaz de usuario para visualizar y editar la configuración global de generación de presentaciones

### Modified Capabilities
- `frontend-ui`: Actualización de la página de Ajustes de placeholder a formulario funcional

## Impact

### Archivos Frontend Afectados
- `frontend/src/pages/AjustesPage.tsx` - Reemplazar placeholder con formulario completo
- `frontend/src/services/api.ts` o nuevo `settingsApi.ts` - Funciones para GET/PUT settings
- `frontend/src/types/` - Tipos TypeScript para GlobalSettings
- `frontend/src/hooks/` - Potencial custom hook `useSettings` para manejo de estado

### User Experience
- Los usuarios pueden ver y modificar la configuración global
- Interfaz clara y profesional para editar prompts largos
- Feedback inmediato al guardar cambios
- Placeholders informativos muestran los defaults

### No Breaking Changes
- Solo frontend, no toca backend
- API existente ya funciona correctamente
- No afecta funcionalidad de generación de contenido actual
- No requiere cambios en base de datos
