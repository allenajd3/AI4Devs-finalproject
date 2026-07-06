import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCreateProject } from '../hooks/useProjects';

export default function CrearPage() {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const navigate = useNavigate();
  const createProject = useCreateProject();

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
    if (!isValidContent || !title.trim()) return;

    try {
      const project = await createProject.mutateAsync({ title, content });
      navigate(`/proyectos/${project.id}`);
    } catch (error) {
      console.error('Error creating project:', error);
    }
  };

  return (
    <div className="max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold text-gray-900 mb-8">Crear Nueva Presentación</h1>

      <form onSubmit={handleSubmit} className="space-y-6">
        <div>
          <label htmlFor="title" className="block text-sm font-medium text-gray-700 mb-2">
            Título del Proyecto
          </label>
          <input
            type="text"
            id="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            placeholder="Nombre del proyecto"
            required
          />
        </div>

        <div>
          <label htmlFor="content" className="block text-sm font-medium text-gray-700 mb-2">
            Transcripción
          </label>
          <textarea
            id="content"
            value={content}
            onChange={(e) => setContent(e.target.value)}
            rows={12}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            placeholder="Pega tu transcripción aquí (mínimo 100 caracteres)..."
          />
          <div className="mt-2 text-sm text-gray-500">
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
              file:bg-blue-50 file:text-blue-700
              hover:file:bg-blue-100"
          />
        </div>

        <button
          type="submit"
          disabled={!isValidContent || !title.trim() || createProject.isPending}
          className="w-full px-6 py-3 bg-blue-600 text-white font-semibold rounded-lg
            hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed
            transition-colors"
        >
          {createProject.isPending ? 'Generando...' : 'Generar Presentación'}
        </button>
      </form>
    </div>
  );
}
