import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import Layout from './components/Layout';
import CrearPage from './pages/CrearPage';
import ProyectosPage from './pages/ProyectosPage';
import ProyectoDetallePage from './pages/ProyectoDetallePage';
import AjustesPage from './pages/AjustesPage';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
    },
  },
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<Navigate to="/crear" replace />} />
            <Route path="crear" element={<CrearPage />} />
            <Route path="proyectos" element={<ProyectosPage />} />
            <Route path="proyectos/:id" element={<ProyectoDetallePage />} />
            <Route path="ajustes" element={<AjustesPage />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </QueryClientProvider>
  );
}

export default App;
