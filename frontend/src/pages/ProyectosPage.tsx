import { useProjects } from '../hooks/useProjects';
import ProjectTile from '../components/ProjectTile';

export default function ProyectosPage() {
  const { data: projects, isLoading, error } = useProjects();

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-ayg-primary"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 rounded-lg p-4">
        <p className="text-red-800 dark:text-red-200">Error al cargar los proyectos. Por favor, intenta de nuevo.</p>
      </div>
    );
  }

  if (!projects || projects.length === 0) {
    return (
      <div className="text-center py-12">
        <h2 className="text-2xl font-bold text-gray-900 dark:text-gray-100 mb-4">No hay proyectos aún</h2>
        <p className="text-gray-600 dark:text-gray-400 mb-6">Crea tu primera presentación para comenzar</p>
        <a
          href="/crear"
          className="inline-block px-6 py-3 bg-ayg-primary text-white font-semibold rounded-lg hover:bg-ayg-primary-dark"
        >
          Crear Presentación
        </a>
      </div>
    );
  }

  return (
    <div>
      <h1 className="text-3xl font-bold text-gray-900 dark:text-gray-100 mb-8">Mis Proyectos</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {projects.map((project) => (
          <ProjectTile key={project.id} project={project} />
        ))}
      </div>
    </div>
  );
}
