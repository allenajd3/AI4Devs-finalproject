## Context

La sidebar actual en `Layout.tsx` tiene ancho fijo `w-64` (256px) y siempre muestra iconos + texto. El componente `Logo` ya soporta tres tamaños (`sm`, `md`, `lg`). No existe ningún estado de UI global — todo el estado de navegación vive en el propio `Layout`. El frontend usa Tailwind CSS v4 con transiciones nativas.

## Goals / Non-Goals

**Goals:**
- Toggle colapsado/expandido con animación suave (transición CSS en el ancho).
- En modo colapsado: ancho `w-16` (64px), solo iconos centrados, tooltip nativo en hover.
- Logo pasa de `size="md"` a `size="sm"` en modo colapsado.
- Estado persistido en `localStorage` clave `sidebar-collapsed`.
- Botón de toggle visible en ambos estados (icono de chevron o hamburguesa).

**Non-Goals:**
- Overlay o drawer en móvil (fuera de alcance).
- Animación de iconos individuales.
- Cambios en el backend o en ningún otro componente fuera de `Layout.tsx`.

## Decisions

### Estado local con `useState` + `localStorage`
Se usa `useState` inicializado desde `localStorage.getItem('sidebar-collapsed')`. Al cambiar el estado se sincroniza con `localStorage`. Alternativa descartada: estado global (Context/Zustand) — innecesario para un toggle de UI local.

### Transición CSS en el elemento `<aside>`
Se aplica `transition-all duration-300` con clases condicionales `w-64` / `w-16`. Tailwind maneja la animación sin JS adicional. Alternativa descartada: animar con `framer-motion` — dependencia innecesaria para este caso.

### Tooltips nativos con `title`
En modo colapsado se añade el atributo `title` al `<Link>` para mostrar el tooltip del navegador. Alternativa descartada: librería de tooltips — añade dependencia y complejidad sin beneficio claro en este contexto de uso.

### Botón de toggle con icono chevron
Se ubica en la parte inferior de la zona de navegación, o en el header de la sidebar. Usa un SVG inline consistente con los demás iconos del componente.

## Risks / Trade-offs

- **Tooltip nativo feo** → Si en el futuro se necesita más control visual, se puede sustituir por un tooltip custom sin romper la funcionalidad.
- **`transition-all` puede ser costoso** → En este caso el único elemento animado es el `<aside>`, no hay listas largas; el impacto de rendimiento es despreciable.
- **`localStorage` no reactivo entre pestañas** → Aceptable; cada pestaña mantiene su propio estado de colapso.
