# Tasks: Identidad Visual Corporativa A&G

## 1. Configuración de Colores Corporativos

- [x] 1.1 Actualizar `frontend/tailwind.config.js` para añadir colores custom en theme.extend.colors
- [x] 1.2 Definir colores: ayg-primary (#5F8FA3), ayg-primary-dark (#4A7289), ayg-primary-light (#7FA5B8)
- [x] 1.3 Verificar que las clases Tailwind custom están disponibles (reiniciar dev server si es necesario)

## 2. Crear Componente Logo

- [x] 2.1 Crear archivo `frontend/src/components/Logo.tsx`
- [x] 2.2 Implementar componente Logo con props: size ('sm' | 'md' | 'lg'), className opcional
- [x] 2.3 Configurar tamaños: sm (h-8), md (h-12), lg (h-16)
- [x] 2.4 Verificar que el logo carga correctamente desde `/src/assets/logo-ayg.png`

## 3. Integrar Logo en Navegación

- [x] 3.1 Leer `frontend/src/components/Sidebar.tsx` (o componente de navegación equivalente)
- [x] 3.2 Importar componente Logo en Sidebar
- [x] 3.3 Añadir Logo en la parte superior del sidebar, centrado
- [x] 3.4 Añadir separador visual (divider o espaciado) entre logo y menú
- [x] 3.5 Aplicar responsive: ocultar o reducir logo en mobile (<768px) usando clases Tailwind

## 4. Aplicar Colores Corporativos en Sidebar

- [x] 4.1 Aplicar `bg-ayg-primary` al fondo del sidebar
- [x] 4.2 Cambiar color de texto del menú a blanco (`text-white`)
- [x] 4.3 Aplicar `bg-ayg-primary-dark` o `bg-ayg-primary-light` a items de navegación activos
- [x] 4.4 Configurar estados hover con `hover:bg-ayg-primary-dark`
- [x] 4.5 Verificar contraste de texto sobre fondos (accesibilidad)

## 5. Aplicar Colores en Botones Primarios

- [x] 5.1 Identificar botones primarios en la aplicación (ej: "Generar presentación", "Crear proyecto")
- [x] 5.2 Aplicar `bg-ayg-primary` y `text-white` a botones primarios
- [x] 5.3 Aplicar `hover:bg-ayg-primary-dark` para estado hover
- [x] 5.4 Verificar que los botones mantienen accesibilidad y legibilidad

## 6. Aplicar Colores en Elementos de UI

- [x] 6.1 Revisar enlaces y estados activos, aplicar `text-ayg-primary` donde corresponda
- [x] 6.2 Aplicar `border-ayg-primary` a elementos con bordes destacados
- [x] 6.3 Usar `bg-ayg-primary-light` para fondos suaves/acentos (si aplica)
- [x] 6.4 Revisar consistencia visual en todas las páginas

## 7. Mejoras de Diseño Profesional

- [x] 7.0.1 Añadir iconos SVG a items del menú para mejor UX
- [x] 7.0.2 Implementar gradiente en sidebar (from-[#5F8FA3] to-[#4A7289])
- [x] 7.0.3 Mejorar estado activo: fondo blanco con texto corporativo y sombra
- [x] 7.0.4 Añadir animaciones y transiciones suaves (hover, activo)
- [x] 7.0.5 Crear header con logo, título "Presentaciones IA" y backdrop blur
- [x] 7.0.6 Añadir footer con copyright "AyG © 2026"
- [x] 7.0.7 Mejorar contenido principal con max-width y mejor espaciado
- [x] 7.0.8 Añadir prop variant ('default' | 'white') al componente Logo
- [x] 7.0.9 Aplicar filtro CSS (brightness-0 invert) para logo blanco en sidebar
- [x] 7.0.10 Mejorar contraste del logo sobre fondo corporativo con drop-shadow

## 8. Verificación y Testing

- [x] 8.1 Verificar que el logo se muestra correctamente en el sidebar
- [x] 8.2 Probar responsive en mobile (375px), tablet (768px), desktop (1280px+)
- [x] 8.3 Revisar todas las rutas: /crear, /proyectos, /proyectos/:id, /ajustes
- [x] 8.4 Verificar contraste de colores con herramienta de accesibilidad (ej: Contrast Checker)
- [x] 8.5 Probar en navegadores: Chrome, Firefox, Safari
- [x] 8.6 Confirmar que no hay regresiones visuales en funcionalidad existente
