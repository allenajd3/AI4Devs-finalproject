import { useParams, useNavigate } from 'react-router-dom';
import { useProjectById, useDeleteProject } from '../hooks/useProjects';

export default function ProyectoDetallePage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { data: project, isLoading, error } = useProjectById(id!);
  const deleteProject = useDeleteProject();

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

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
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
          <p className="text-gray-600 text-lg font-medium">Coming Soon</p>
          <p className="text-gray-500 text-sm mt-2">Las diapositivas se generarán próximamente</p>
        </div>
      </div>

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
