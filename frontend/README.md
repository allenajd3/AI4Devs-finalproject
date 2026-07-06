# AyG Presentaciones IA - Frontend

Frontend MVP para la aplicación de generación de presentaciones con IA.

## Stack Tecnológico

- **React 18** con TypeScript
- **Vite** como build tool
- **Tailwind CSS** para estilos
- **React Router v6** para navegación
- **TanStack Query (React Query)** para data fetching
- **Axios** para llamadas HTTP
- **WebSocket (STOMP + SockJS)** para comunicación en tiempo real

## Estructura del Proyecto

```
src/
├── components/     # Componentes reutilizables (Layout, ProjectTile, ErrorBoundary)
├── pages/          # Páginas completas (Crear, Proyectos, Detalle, Ajustes)
├── services/       # Servicios (API, WebSocket)
├── hooks/          # Custom hooks (useProjects, useProjectById, etc.)
├── types/          # Definiciones TypeScript
├── App.tsx         # Configuración de routing y providers
└── main.tsx        # Punto de entrada
```

## Instalación

```bash
npm install
```

## Desarrollo

```bash
npm run dev
```

La aplicación estará disponible en `http://localhost:5173`

**Importante**: El backend debe estar ejecutándose en `http://localhost:8080`

## Build para Producción

```bash
npm run build
```

## Funcionalidades Implementadas

- ✅ Crear proyectos (cargar texto o subir archivo .txt)
- ✅ Listar proyectos en mosaico
- ✅ Ver detalle de proyecto
- ✅ Eliminar proyecto
- ✅ Navegación con sidebar persistente
- ✅ Estados de carga y error
- ✅ Diseño responsive
- ✅ WebSocket preparado (para futuras funcionalidades)

## Próximos Pasos

- Implementar generación de slides en backend
- Conectar WebSocket para progreso en tiempo real
- Vista de edición de diapositivas
- Descarga de PDF
