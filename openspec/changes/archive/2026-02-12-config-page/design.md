# Design: Página de Configuración Global

## Context

La aplicación ya tiene una API REST funcional para configuración global (`/api/settings` GET/PUT) con la entidad `GlobalSettings` en el backend. El frontend tiene una ruta `/ajustes` que actualmente solo muestra "Coming Soon".

**Estado Actual:**
- Backend: `GlobalSettings` entity con 3 campos (systemPrompt, contentOrientation, visualStyle)
- Backend: API endpoints funcionales
- Frontend: AjustesPage.tsx con placeholder
- Frontend: Ya existe patrón de integración API con TanStack Query (React Query)

**Stakeholders:**
- Usuarios finales que quieren personalizar cómo se generan sus presentaciones
- Desarrolladores que mantendrán este formulario

## Goals / Non-Goals

**Goals:**
- Formulario funcional y usable para editar los 3 campos de configuración
- Consistencia visual con el resto de la aplicación (colores corporativos a&g)
- UX intuitiva con iconos, placeholders informativos y feedback claro
- Manejo robusto de errores y estados de carga
- Código mantenible que siga los patrones existentes del proyecto

**Non-Goals:**
- Validación compleja de prompts (el backend acepta cualquier string)
- Múltiples perfiles de configuración (solo hay una configuración global)
- Versionado de configuración o historial de cambios
- Preview en vivo de cómo afectará la configuración
- Refactorización del backend o cambios en la API

## Decisions

### 1. State Management: React useState (no form library)

**Decisión:** Usar React `useState` para manejar el formulario, sin librerías como react-hook-form.

**Rationale:**
- Formulario simple: solo 3 campos de texto
- No necesita validación compleja
- Patrones del proyecto ya usan useState para formularios (ver `CrearPage.tsx`)
- Evita dependencias adicionales

**Alternativas consideradas:**
- `react-hook-form`: Overkill para 3 campos sin validaciones complejas
- `formik`: Demasiado boilerplate para este caso

### 2. API Integration: TanStack Query (React Query)

**Decisión:** Usar TanStack Query con hooks `useQuery` para GET y `useMutation` para PUT.

**Rationale:**
- Consistente con el resto del frontend (ya se usa en `CrearPage.tsx`, `ProyectosPage.tsx`)
- Manejo automático de loading/error states
- Caché inteligente y sincronización
- Invalidación de queries después de mutación

**Implementación:**
```typescript
// Fetch settings
const { data: settings, isLoading, error } = useQuery({
  queryKey: ['settings'],
  queryFn: async () => {
    const response = await axios.get('/api/settings');
    return response.data;
  }
});

// Update settings
const updateSettings = useMutation({
  mutationFn: async (data: GlobalSettings) => {
    return axios.put('/api/settings', data);
  },
  onSuccess: () => {
    queryClient.invalidateQueries({ queryKey: ['settings'] });
    // Show success message
  }
});
```

**Alternativas consideradas:**
- `fetch` directo: Menos robusto, requiere manejo manual de estados
- Custom hooks con useState: Reinventaría la rueda

### 3. Placeholders: Mostrar Defaults del Backend

**Decisión:** Los placeholders muestran texto descriptivo genérico, NO los valores por defecto del backend.

**Rationale:**
- El backend devuelve `null` para campos vacíos
- Los defaults reales están hardcoded en el servicio de backend
- Mostrar los defaults requeriría duplicar lógica o nueva API
- Placeholders descriptivos son suficientes para guiar al usuario

**Implementación:**
```typescript
const PLACEHOLDERS = {
  systemPrompt: 'Ej: Eres un experto en presentaciones ejecutivas. Enfócate en claridad y profesionalismo...',
  contentOrientation: 'Ej: Orientación a resultados, enfoque estratégico, datos cuantificables...',
  visualStyle: 'Ej: Estilo corporativo moderno, colores azul y blanco, tipografía sans-serif...'
};
```

**Alternativas consideradas:**
- Endpoint `/api/settings/defaults`: Requiere cambios en backend (fuera de scope)
- Cargar defaults desde config JSON: Duplicación de lógica

### 4. Layout: Formulario Vertical con Iconos

**Decisión:** Layout vertical, cada campo ocupa 100% ancho, textareas grandes (6-8 filas), iconos inline.

**Rationale:**
- Prompts pueden ser largos (100-500 caracteres)
- Vertical permite leer y editar sin scroll horizontal
- Iconos mejoran escaneabilidad visual
- Consistente con formulario de CrearPage

**Estructura:**
```
┌──────────────────────────────────────┐
│  Ajustes de Generación               │
├──────────────────────────────────────┤
│  📝 Prompt del Sistema               │
│  [===== textarea grande ==========]  │
│                                      │
│  🎯 Orientación del Contenido        │
│  [===== textarea grande ==========]  │
│                                      │
│  🎨 Estilo Visual                    │
│  [===== textarea grande ==========]  │
│                                      │
│  [  Guardar Configuración  ]         │
└──────────────────────────────────────┘
```

**Alternativas consideradas:**
- 2 columnas: Campos no caben bien horizontalmente
- Tabs: Solo 3 campos, no justifica tabs

### 5. Feedback: Toast Notifications vs Inline Messages

**Decisión:** Mensajes inline (alert boxes) temporales que desaparecen después de 3-5 segundos.

**Rationale:**
- No requiere librería externa de toasts
- Mensajes contextuales justo encima del botón de guardar
- Usuarios saben dónde buscar el feedback
- Consistente con patrones simples del proyecto

**Implementación:**
```typescript
const [message, setMessage] = useState<{type: 'success'|'error', text: string} | null>(null);

// En onSuccess:
setMessage({ type: 'success', text: 'Configuración guardada correctamente' });
setTimeout(() => setMessage(null), 4000);
```

**Alternativas consideradas:**
- React-hot-toast / sonner: Dependencia extra innecesaria
- Modal de confirmación: Demasiado intrusivo

### 6. Estilos: Tailwind CSS con Colores Corporativos

**Decisión:** Usar Tailwind CSS con colores a&g (`ayg-primary`, `ayg-primary-dark`) consistentes con el Layout.

**Rationale:**
- Ya establecido en todo el frontend
- Configuración de colores en `index.css` con `@theme`
- Botón de guardar sigue el mismo patrón que "Generar Presentación"

**Clases clave:**
- Textareas: `border-gray-300 focus:ring-ayg-primary focus:border-ayg-primary`
- Botón submit: `bg-ayg-primary hover:bg-ayg-primary-dark text-white`
- Labels: iconos emoji + texto en `font-medium text-gray-700`

## Risks / Trade-offs

### [Risk] Settings no cargan al inicio → Formulario vacío sin feedback

**Mitigation:**
- Loading spinner mientras se carga la configuración inicial
- Error boundary si falla el GET: mostrar mensaje "No se pudo cargar la configuración" + botón retry
- Deshabilitar botón "Guardar" mientras está en loading state

### [Risk] Usuario edita y cierra accidentalmente → Pierde cambios

**Mitigation:**
- NO implementar por ahora (scope creep)
- Futuro: `beforeunload` event listener o dirty state tracking
- Trade-off: Simplicidad vs protección de datos

### [Risk] PUT falla por timeout/red → Usuario ve error genérico

**Mitigation:**
- Mostrar mensaje de error específico con el texto del error del backend
- Mantener el formulario con los datos editados (no resetear)
- Botón de guardar vuelve a estar habilitado para reintentar

### [Risk] Prompts muy largos (>5000 chars) → UX degradado

**Mitigation:**
- Textareas con scroll automático
- NO implementar límite por ahora (backend no lo tiene)
- Futuro: Character counter opcional

## Open Questions

Ninguna - todos los requisitos están claros y las decisiones técnicas son sólidas.
