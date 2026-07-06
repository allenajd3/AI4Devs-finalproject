import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCreateProject } from '../hooks/useProjects';
import { useContentGeneration } from '../hooks/useContentGeneration';
import { GenerationProgress } from '../components/GenerationProgress';

export default function CrearPage() {
  const [content, setContent] = useState('');
  const navigate = useNavigate();
  const createProject = useCreateProject();
  const { isGenerating, progress, error, startGeneration, reset } = useContentGeneration();

  const isValidContent = content.length >= 100;

  const handleFileUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file && file.type === 'text/plain') {
      const reader = new FileReader();
      reader.onload = (event) => {
        const text = event.target?.result as string;
        setContent(text);
      };
      reader.readAsText(file);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!isValidContent) return;

    try {
      // Create project with temporary title
      const project = await createProject.mutateAsync({ 
        title: 'Nuevo Proyecto', 
        content 
      });

      // Start content generation
      await startGeneration(project.id);

      // Navigate to project detail
      navigate(`/proyectos/${project.id}`);
    } catch (error) {
      console.error('Error creating project:', error);
    }
  };

  const handleRetry = async () => {
    // Retry logic could be implemented here if needed
    reset();
  };

  const handleClose = () => {
    reset();
  };

  return (
    <div className="max-w-4xl mx-auto pb-12">
      <h1 className="text-3xl font-bold text-gray-900 dark:text-gray-100 mb-8">Crear Nueva Presentación</h1>

      <form onSubmit={handleSubmit} className="space-y-6">
        <div>
          <label htmlFor="content" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Transcripción
          </label>
          <textarea
            id="content"
            value={content}
            onChange={(e) => setContent(e.target.value)}
            rows={12}
            className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-ayg-primary focus:border-transparent bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
            placeholder="Pega tu transcripción aquí (mínimo 100 caracteres)..."
          />
          <div className="mt-2 text-sm text-gray-500 dark:text-gray-400">
            {content.length} / 100 caracteres
          </div>
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            O sube un archivo .txt
          </label>
          <input
            type="file"
            accept=".txt"
            onChange={handleFileUpload}
            className="block w-full text-sm text-gray-500
              file:mr-4 file:py-2 file:px-4
              file:rounded-lg file:border-0
              file:text-sm file:font-semibold
              file:bg-ayg-primary-light file:text-ayg-primary-dark
              hover:file:bg-ayg-primary"
          />
        </div>

        <div className="pt-4">
          <button
            type="submit"
            disabled={!isValidContent || createProject.isPending || isGenerating}
            className="w-full px-6 py-4 text-lg font-bold rounded-lg shadow-lg transition-all
              bg-ayg-primary text-white hover:bg-ayg-primary-dark hover:shadow-xl
              disabled:bg-gray-400 disabled:text-gray-200 disabled:cursor-not-allowed disabled:shadow-none"
          >
            {createProject.isPending ? 'Creando proyecto...' : 'Generar Presentación'}
          </button>
        </div>
      </form>

      {/* Generation Progress Modal */}
      {isGenerating && (
        <GenerationProgress
          progress={progress}
          error={error}
          onRetry={handleRetry}
          onClose={handleClose}
        />
      )}
    </div>
  );
}
