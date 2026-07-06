import { useParams, useNavigate } from 'react-router-dom';
import { useProjectById, useDeleteProject } from '../hooks/useProjects';
import { useSlides } from '../hooks/useSlides';
import { useContentGeneration } from '../hooks/useContentGeneration';
import { SlideCard } from '../components/SlideCard';
import { GenerationProgress } from '../components/GenerationProgress';

export default function ProyectoDetallePage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { data: project, isLoading, error } = useProjectById(id!);
  const { data: slides, isLoading: slidesLoading } = useSlides(id);
  const deleteProject = useDeleteProject();
  const { isGenerating, progress, error: genError, startGeneration, reset } = useContentGeneration();

  const handleDelete = async () => {
    if (!window.confirm('¿Estás seguro de que deseas eliminar este proyecto?')) {
      return;
    }

    try {
      await deleteProject.mutateAsync(id!);
      navigate('/proyectos');
    } catch (error) {
      console.error('Error deleting project:', error);
    }
  };

  const handleGenerateContent = async () => {
    if (!id) return;
    await startGeneration(id);
    // Refresh the page after generation
    window.location.reload();
  };

  const handleRetry = () => {
    reset();
  };

  const handleClose = () => {
    reset();
  };

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-ayg-primary"></div>
      </div>
    );
  }

  if (error || !project) {
    return (
      <div className="bg-red-50 border border-red-200 rounded-lg p-4">
        <p className="text-red-800">Error al cargar el proyecto.</p>
      </div>
    );
  }

  return (
    <div className="max-w-5xl mx-auto">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-4">{project.title}</h1>
        <div className="flex gap-4">
          <button
            disabled
            className="px-6 py-2 bg-gray-300 text-gray-500 font-semibold rounded-lg cursor-not-allowed"
          >
            Descargar PDF
          </button>
          <button
            onClick={handleDelete}
            disabled={deleteProject.isPending}
            className="px-6 py-2 bg-red-600 text-white font-semibold rounded-lg hover:bg-red-700 disabled:bg-gray-300"
          >
            {deleteProject.isPending ? 'Eliminando...' : 'Eliminar'}
          </button>
        </div>
      </div>

      {/* Slides Section */}
      <div className="bg-white rounded-lg shadow p-6 mb-8">
        <h2 className="text-xl font-semibold text-gray-900 mb-4">Diapositivas</h2>
        
        {slidesLoading ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {[1, 2, 3].map((i) => (
              <div
                key={i}
                className="bg-gray-100 rounded-lg h-80 animate-pulse"
              ></div>
            ))}
          </div>
        ) : slides && slides.length > 0 ? (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {slides.map((slide) => (
              <SlideCard key={slide.id} slide={slide} />
            ))}
          </div>
        ) : (
          <div className="bg-gray-50 border-2 border-dashed border-gray-300 rounded-lg p-12 text-center">
            <svg
              className="mx-auto h-16 w-16 text-gray-400 mb-4"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
              />
            </svg>
            <p className="text-gray-600 text-lg font-medium mb-2">Sin diapositivas generadas</p>
            <p className="text-gray-500 text-sm mb-4">
              Este proyecto aún no tiene diapositivas. Genera el contenido para crear las diapositivas.
            </p>
            <button
              onClick={handleGenerateContent}
              disabled={isGenerating}
              className="px-6 py-2 bg-ayg-primary text-white font-semibold rounded-lg hover:bg-ayg-primary-dark disabled:bg-gray-300 transition-colors"
            >
              {isGenerating ? 'Generando...' : 'Generar Contenido'}
            </button>
          </div>
        )}
      </div>

      {/* Generation Progress Modal */}
      {isGenerating && (
        <GenerationProgress
          progress={progress}
          error={genError}
          onRetry={handleRetry}
          onClose={handleClose}
        />
      )}

      {/* Original Content */}
      <div className="bg-white rounded-lg shadow p-6">
        <h2 className="text-xl font-semibold text-gray-900 mb-4">Contenido Original</h2>
        <div className="prose max-w-none">
          <pre className="whitespace-pre-wrap text-sm text-gray-700 bg-gray-50 p-4 rounded-lg">
            {project.content}
          </pre>
        </div>
      </div>
    </div>
  );
}
