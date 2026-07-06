## 1. Estado de colapso

- [x] 1.1 Añadir `useState<boolean>` en `Layout.tsx` inicializado desde `localStorage.getItem('sidebar-collapsed') === 'true'`
- [x] 1.2 Implementar función `toggleSidebar` que actualiza el estado y sincroniza `localStorage`

## 2. Dimensiones y transición del aside

- [x] 2.1 Añadir clases condicionales al `<aside>`: `w-64` cuando expandido, `w-16` cuando colapsado
- [x] 2.2 Añadir `transition-all duration-300` al `<aside>` para animar el cambio de ancho
- [x] 2.3 Añadir `overflow-hidden` al `<aside>` para evitar desbordamiento durante la animación

## 3. Botón de toggle

- [x] 3.1 Añadir botón de toggle con icono de chevron (izq/der según estado) en el header o zona de nav
- [x] 3.2 El botón debe ser visible y accesible en ambos estados (expandido y colapsado)

## 4. Header del sidebar (Logo y título)

- [x] 4.1 Pasar `size="sm"` al componente `<Logo>` cuando `collapsed === true`, `size="md"` cuando expandido
- [x] 4.2 Ocultar el `<h2>` "Presentaciones IA" con `hidden` cuando colapsado

## 5. Navegación en modo colapsado

- [x] 5.1 Ocultar el `<span>` del label de cada ítem de nav cuando colapsado (`hidden` o condicional)
- [x] 5.2 Centrar el icono en el link cuando colapsado (quitar `gap-3 px-4`, usar `justify-center px-0`)
- [x] 5.3 Añadir atributo `title={item.label}` a cada `<Link>` cuando collapsed para tooltip nativo

## 6. Footer

- [x] 6.1 Ocultar el bloque footer "AyG © 2026" cuando colapsado

## 7. Verificación

- [x] 7.1 Verificar transición suave al hacer toggle repetido
- [x] 7.2 Verificar que el estado persiste al recargar la página
- [x] 7.3 Verificar que los tooltips aparecen correctamente en modo colapsado
- [x] 7.4 Verificar que el logo cambia de tamaño correctamente en ambos estados
- [x] 7.5 Verificar comportamiento en dark mode
