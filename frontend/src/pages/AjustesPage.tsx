import { useState, useEffect } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { settingsApi } from '../services/api';
import type { GlobalSettings } from '../types/settings';

// Placeholders descriptivos para los campos de configuración
const PLACEHOLDERS = {
  systemPrompt: 'Ej: Eres un experto en presentaciones ejecutivas. Enfócate en claridad, profesionalismo y resultados medibles. Utiliza un tono formal pero accesible...',
  contentOrientation: 'Ej: Orientación a resultados, enfoque estratégico, datos cuantificables, decisiones basadas en evidencia, impacto en el negocio...',
  visualStyle: 'Ej: Estilo corporativo moderno, colores azul y blanco, tipografía sans-serif (Montserrat), diseño minimalista, elementos visuales profesionales...'
};

export default function AjustesPage() {
  const queryClient = useQueryClient();

  // Estado del formulario
  const [formData, setFormData] = useState<GlobalSettings>({
    systemPrompt: null,
    contentOrientation: null,
    visualStyle: null,
  });

  // Estado para mensajes de feedback (success/error)
  const [message, setMessage] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  // Query para obtener la configuración actual
  const { data: settings, isLoading, error, refetch } = useQuery({
    queryKey: ['settings'],
    queryFn: settingsApi.getSettings,
  });

  // Mutation para actualizar configuración
  const updateMutation = useMutation({
    mutationFn: settingsApi.updateSettings,
    onSuccess: () => {
      // Invalidar cache para refrescar datos
      queryClient.invalidateQueries({ queryKey: ['settings'] });
      
      // Mostrar mensaje de éxito
      setMessage({ type: 'success', text: 'Configuración guardada correctamente' });
      
      // Auto-dismiss después de 4 segundos
      setTimeout(() => setMessage(null), 4000);
    },
    onError: (error: any) => {
      // Mostrar mensaje de error con detalles
      const errorMessage = error.response?.data?.message || error.message || 'Error al guardar la configuración';
      setMessage({ type: 'error', text: errorMessage });
    },
  });

  // Efecto para poblar el formulario con los datos obtenidos
  useEffect(() => {
    if (settings) {
      setFormData({
        systemPrompt: settings.systemPrompt || null,
        contentOrientation: settings.contentOrientation || null,
        visualStyle: settings.visualStyle || null,
      });
    }
  }, [settings]);

  // Handler para cambios en los campos
  const handleChange = (field: keyof GlobalSettings, value: string) => {
    setFormData(prev => ({
      ...prev,
      [field]: value || null,
    }));
  };

  // Handler para submit del formulario
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    updateMutation.mutate(formData);
  };

  // Loading state
  if (isLoading) {
    return (
      <div className="max-w-4xl mx-auto">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">Ajustes de Generación</h1>
        <div className="bg-white rounded-lg shadow p-12 text-center">
          <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-ayg-primary"></div>
          <p className="text-gray-600 mt-4">Cargando configuración...</p>
        </div>
      </div>
    );
  }

  // Error state con opción de retry
  if (error) {
    return (
      <div className="max-w-4xl mx-auto">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">Ajustes de Generación</h1>
        <div className="bg-white rounded-lg shadow p-12">
          <div className="bg-red-50 border border-red-200 rounded-lg p-4 mb-4">
            <p className="text-red-800 font-medium">No se pudo cargar la configuración</p>
            <p className="text-red-600 text-sm mt-1">{(error as any).message}</p>
          </div>
          <button
            onClick={() => refetch()}
            className="px-4 py-2 bg-ayg-primary text-white rounded-lg hover:bg-ayg-primary-dark transition-colors"
          >
            Reintentar
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold text-gray-900 mb-8">Ajustes de Generación</h1>
      
      <div className="bg-white rounded-lg shadow p-8">
        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Campo 1: Prompt del Sistema */}
          <div>
            <label htmlFor="systemPrompt" className="flex items-center gap-2 text-sm font-medium text-gray-700 mb-2">
              <span className="text-xl">📝</span>
              Prompt del Sistema
            </label>
            <textarea
              id="systemPrompt"
              rows={8}
              value={formData.systemPrompt || ''}
              onChange={(e) => handleChange('systemPrompt', e.target.value)}
              placeholder={PLACEHOLDERS.systemPrompt}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ayg-primary focus:border-ayg-primary transition-colors resize-none"
            />
            <p className="text-xs text-gray-500 mt-1">
              Define el contexto general y tono para las presentaciones
            </p>
          </div>

          {/* Campo 2: Orientación del Contenido */}
          <div>
            <label htmlFor="contentOrientation" className="flex items-center gap-2 text-sm font-medium text-gray-700 mb-2">
              <span className="text-xl">🎯</span>
              Orientación del Contenido
            </label>
            <textarea
              id="contentOrientation"
              rows={8}
              value={formData.contentOrientation || ''}
              onChange={(e) => handleChange('contentOrientation', e.target.value)}
              placeholder={PLACEHOLDERS.contentOrientation}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ayg-primary focus:border-ayg-primary transition-colors resize-none"
            />
            <p className="text-xs text-gray-500 mt-1">
              El enfoque que se quiere dar a las presentaciones
            </p>
          </div>

          {/* Campo 3: Estilo Visual */}
          <div>
            <label htmlFor="visualStyle" className="flex items-center gap-2 text-sm font-medium text-gray-700 mb-2">
              <span className="text-xl">🎨</span>
              Estilo Visual
            </label>
            <textarea
              id="visualStyle"
              rows={8}
              value={formData.visualStyle || ''}
              onChange={(e) => handleChange('visualStyle', e.target.value)}
              placeholder={PLACEHOLDERS.visualStyle}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-ayg-primary focus:border-ayg-primary transition-colors resize-none"
            />
            <p className="text-xs text-gray-500 mt-1">
              Describe el aspecto gráfico deseado para las diapositivas
            </p>
          </div>

          {/* Mensaje de feedback (success o error) */}
          {message && (
            <div className={`rounded-lg p-4 ${
              message.type === 'success' 
                ? 'bg-green-50 border border-green-200 text-green-800' 
                : 'bg-red-50 border border-red-200 text-red-800'
            }`}>
              {message.text}
            </div>
          )}

          {/* Botón de submit */}
          <div className="pt-4">
            <button
              type="submit"
              disabled={updateMutation.isPending}
              className="w-full px-6 py-4 text-lg font-bold rounded-lg shadow-lg transition-all
                bg-ayg-primary text-white hover:bg-ayg-primary-dark hover:shadow-xl
                disabled:bg-gray-400 disabled:text-gray-200 disabled:cursor-not-allowed disabled:shadow-none"
            >
              {updateMutation.isPending ? 'Guardando...' : 'Guardar Configuración'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
