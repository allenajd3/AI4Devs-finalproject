import { useEffect, useRef } from 'react';

interface GenerationProgressProps {
  progress: string[];
  error: string | null;
  onRetry?: () => void;
  onClose?: () => void;
}

export function GenerationProgress({
  progress,
  error,
  onRetry,
  onClose,
}: GenerationProgressProps) {
  const listRef = useRef<HTMLDivElement>(null);

  // Auto-scroll to bottom when new progress message arrives
  useEffect(() => {
    if (listRef.current) {
      listRef.current.scrollTop = listRef.current.scrollHeight;
    }
  }, [progress]);

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl max-w-2xl w-full mx-4 p-6">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xl font-semibold text-gray-900">
            {error ? 'Error en la Generación' : 'Generando Presentación'}
          </h2>
          {!error && (
            <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-600"></div>
          )}
        </div>

        {error ? (
          <div className="space-y-4">
            <div className="bg-red-50 border border-red-200 rounded-lg p-4">
              <p className="text-red-800">{error}</p>
            </div>
            <div className="flex gap-3 justify-end">
              {onRetry && (
                <button
                  onClick={onRetry}
                  className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
                >
                  Reintentar
                </button>
              )}
              {onClose && (
                <button
                  onClick={onClose}
                  className="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition-colors"
                >
                  Cerrar
                </button>
              )}
            </div>
          </div>
        ) : (
          <div
            ref={listRef}
            className="bg-gray-50 rounded-lg p-4 max-h-96 overflow-y-auto space-y-2"
          >
            {progress.length === 0 ? (
              <p className="text-gray-500 text-center">Iniciando...</p>
            ) : (
              progress.map((message, index) => (
                <div
                  key={index}
                  className="flex items-start gap-2 text-sm text-gray-700"
                >
                  <span className="text-blue-600 font-medium">
                    {index + 1}.
                  </span>
                  <span>{message}</span>
                </div>
              ))
            )}
          </div>
        )}

        {!error && progress.length > 0 && (
          <p className="mt-4 text-sm text-gray-500 text-center">
            Este proceso puede tardar unos minutos...
          </p>
        )}
      </div>
    </div>
  );
}
