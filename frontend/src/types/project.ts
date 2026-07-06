export const ProjectStatus = {
  DRAFT: 'DRAFT',
  GENERATING: 'GENERATING',
  COMPLETED: 'COMPLETED',
  ERROR: 'ERROR',
} as const;

export type ProjectStatus = (typeof ProjectStatus)[keyof typeof ProjectStatus];

export interface Project {
  id: string;
  title: string;
  content: string;
  status: ProjectStatus;
  createdAt: string;
  updatedAt: string;
}

export interface ProjectSummary {
  id: string;
  title: string;
  status: ProjectStatus;
  createdAt: string;
}

export interface ProjectCreateRequest {
  title: string;
  content: string;
}
