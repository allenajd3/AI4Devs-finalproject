import { useNavigate } from 'react-router-dom';
import type { ProjectSummary } from '../types/project';

interface ProjectTileProps {
  project: ProjectSummary;
}

export default function ProjectTile({ project }: ProjectTileProps) {
  const navigate = useNavigate();

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'COMPLETED':
        return 'bg-green-100 text-green-800';
      case 'GENERATING':
        return 'bg-yellow-100 text-yellow-800';
      case 'ERROR':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <div
      onClick={() => navigate(`/proyectos/${project.id}`)}
      className="bg-white dark:bg-gray-800 rounded-lg shadow hover:shadow-lg transition-shadow cursor-pointer p-6"
    >
      <h3 className="text-lg font-semibold text-gray-900 dark:text-gray-100 mb-2 line-clamp-2">
        {project.title}
      </h3>
      <div className="flex items-center justify-between mt-4">
        <span className="text-sm text-gray-500 dark:text-gray-400">{formatDate(project.createdAt)}</span>
        <span className={`px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(project.status)}`}>
          {project.status}
        </span>
      </div>
    </div>
  );
}
