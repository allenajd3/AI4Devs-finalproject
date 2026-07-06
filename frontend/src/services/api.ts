import axios from 'axios';
import type { Project, ProjectSummary, ProjectCreateRequest } from '../types/project';

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const projectsApi = {
  createProject: async (data: ProjectCreateRequest): Promise<Project> => {
    const response = await api.post<Project>('/projects', data);
    return response.data;
  },

  getProjects: async (): Promise<ProjectSummary[]> => {
    const response = await api.get<ProjectSummary[]>('/projects');
    return response.data;
  },

  getProjectById: async (id: string): Promise<Project> => {
    const response = await api.get<Project>(`/projects/${id}`);
    return response.data;
  },

  deleteProject: async (id: string): Promise<void> => {
    await api.delete(`/projects/${id}`);
  },
};

export default api;
