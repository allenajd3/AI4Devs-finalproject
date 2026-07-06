# Design: Identidad Visual Corporativa A&G

## Context

La aplicación actualmente usa estilos genéricos de Tailwind CSS v4 sin personalización de marca. El sistema de navegación (Sidebar + Layout) existe pero carece de identidad corporativa. 

Necesitamos:
- Integrar el logo a&g proporcionado (`frontend/src/assets/logo-ayg.png`)
- Definir y aplicar color primario corporativo: `#5F8FA3` (azul grisáceo)
- Mantener Tailwind CSS v4 como base pero con theme customizado
- Aplicar cambios solo en frontend (React + TypeScript)

## Goals / Non-Goals

**Goals:**
- Establecer identidad visual corporativa con logo y colores a&g
- Configurar Tailwind con colores corporativos como custom theme
- Integrar logo en la navegación lateral de forma visible y profesional
- Aplicar esquema de colores (primario + blanco) en todos los componentes UI
- Mantener diseño responsive y accesible

**Non-Goals:**
- Cambios en backend o API
- Rediseño completo de layout o flujo de navegación
- Animaciones complejas o transiciones avanzadas
- Modo oscuro (queda para futuro)
- Múltiples variantes del logo

## Decisions

### Decisión 1: Configuración de Colores en Tailwind v4
**Elegido:** Extender theme de Tailwind con colores corporativos custom

**Configuración en `tailwind.config.js`:**
```js
export default {
  theme: {
    extend: {
      colors: {
        'ayg': {
          primary: '#5F8FA3',    // Azul grisáceo corporativo
          'primary-dark': '#4A7289',  // Hover/active states
          'primary-light': '#7FA5B8', // Backgrounds suaves
        }
      }
    }
  }
}
```

**Razón:**
- Permite usar clases Tailwind como `bg-ayg-primary`, `text-ayg-primary`
- Mantiene consistencia con el sistema de diseño de Tailwind
- Fácil de mantener y extender
- No requiere CSS custom adicional

**Alternativas consideradas:**
- CSS Variables globales → Descartado: menos integrado con Tailwind, requiere más CSS custom
- Reemplazar colores default → Descartado: podría romper estilos existentes

### Decisión 2: Integración del Logo
**Elegido:** Componente React `<Logo />` reutilizable

**Ubicación:** `frontend/src/components/Logo.tsx`

**Implementación:**
```tsx
interface LogoProps {
  size?: 'sm' | 'md' | 'lg';
  className?: string;
}

export const Logo = ({ size = 'md', className }: LogoProps) => {
  const sizeClasses = {
    sm: 'h-8',
    md: 'h-12',
    lg: 'h-16'
  };
  
  return (
    <img 
      src="/src/assets/logo-ayg.png" 
      alt="a&g" 
      className={`${sizeClasses[size]} w-auto ${className}`} 
    />
  );
};
```

**Razón:**
- Reutilizable en múltiples ubicaciones (Sidebar, futuras páginas)
- Control centralizado de tamaños
- Fácil de actualizar si cambia el logo

**Alternativas consideradas:**
- SVG inline → Descartado: el logo proporcionado es PNG, conversión no necesaria para MVP
- Import directo en cada uso → Descartado: menos mantenible

### Decisión 3: Ubicación del Logo en la Navegación
**Elegido:** Logo en la parte superior del Sidebar, encima del menú

**Layout propuesto:**
```
┌─ Sidebar ────────┐
│  [Logo a&g]      │ ← Centrado, tamaño 'md'
│  ───────────     │ ← Divider sutil
│  → Crear         │
│  → Proyectos     │
│  → Ajustes       │
└──────────────────┘
```

**Razón:**
- Posición jerárquica clara (branding primero)
- No interfiere con la navegación
- Estándar en aplicaciones corporativas

**Alternativas consideradas:**
- Logo en header horizontal → Descartado: el diseño usa sidebar vertical
- Logo como fondo → Descartado: menos visible, dificulta lectura

### Decisión 4: Aplicación del Esquema de Colores
**Elegido:** Enfoque progresivo por componente

**Prioridad de aplicación:**
1. **Sidebar/Navegación** - `bg-ayg-primary` para fondo, texto blanco
2. **Botones primarios** - `bg-ayg-primary hover:bg-ayg-primary-dark`
3. **Enlaces activos** - `text-ayg-primary` o `bg-ayg-primary-light`
4. **Acentos y bordes** - `border-ayg-primary` en elementos destacados

**Razón:**
- Cambio gradual minimiza riesgo de romper estilos
- Fácil de revisar visualmente por componente
- Permite ajustes incrementales

### Decisión 5: Compatibilidad con Diseño Responsive
**Elegido:** Logo se oculta en mobile (<768px) o reduce tamaño a 'sm'

**Implementación:**
```tsx
<Logo size="md" className="hidden md:block" />
```

**Razón:**
- Sidebar en mobile suele ser colapsado/overlay
- Prioriza espacio para navegación en pantallas pequeñas
- Mantiene funcionalidad sin sacrificar UX

## Risks / Trade-offs

### Riesgo 1: Logo en formato PNG podría no escalar bien
**Riesgo:** Logo PNG puede verse pixelado en pantallas de alta resolución.

**Mitigación:**
- Usar logo en resolución 2x o 3x (192px+ de ancho)
- Si hay problemas, solicitar versión SVG para futuro
- Por ahora, tamaño 'md' (h-12 = 48px) debería ser suficiente

### Riesgo 2: Contraste de color primario con blanco
**Riesgo:** `#5F8FA3` sobre blanco podría tener contraste insuficiente para accesibilidad (WCAG AA).

**Mitigación:**
- Usar `ayg-primary-dark` para textos sobre fondo blanco
- Verificar contraste con herramienta (contrast ratio ≥ 4.5:1)
- Fondos principales usan el color corporativo con texto blanco (contraste garantizado)

### Riesgo 3: Colores custom podrían no integrarse bien con componentes existentes
**Riesgo:** Algunos componentes podrían tener estilos hardcoded con colores Tailwind default.

**Mitigación:**
- Revisión visual de todas las páginas después de aplicar cambios
- Extender theme en lugar de reemplazar (mantiene colores default disponibles)
- Testing manual en todas las vistas (Crear, Proyectos, Detalle, Ajustes)

### Trade-off: Personalización vs. Mantenibilidad
**Trade-off:** Más personalización de colores = más mantenimiento futuro.

**Justificación:**
- Para marca corporativa, el trade-off vale la pena
- Configuración en Tailwind es declarativa y fácil de mantener
- Solo 3 variantes de color (primary, primary-dark, primary-light) mantiene simplicidad

## Migration Plan

### Implementación
1. Actualizar `tailwind.config.js` con colores corporativos
2. Crear componente `<Logo />` y verificar que carga correctamente
3. Integrar logo en `Sidebar.tsx`
4. Aplicar esquema de colores en orden:
   - Sidebar (fondo + navegación)
   - Botones primarios
   - Estados hover/active
   - Acentos y detalles
5. Verificar responsive en mobile/tablet
6. Testing visual en todas las rutas

### Rollback
Si hay problemas visuales graves:
1. Revertir `tailwind.config.js` (eliminar extend colors)
2. Remover componente `<Logo />`
3. Revertir cambios en componentes (git revert)
4. No hay impacto en datos o funcionalidad

### Testing
- Verificar carga del logo en desarrollo local
- Comprobar contraste de colores con herramientas de accesibilidad
- Revisar diseño en Chrome, Firefox, Safari
- Probar responsive en mobile (375px), tablet (768px), desktop (1280px+)

## Open Questions

Ninguna - el diseño está bien definido y listo para implementar.
