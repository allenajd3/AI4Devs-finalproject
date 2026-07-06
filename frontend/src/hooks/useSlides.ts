import { useQuery } from '@tanstack/react-query';
import { getSlides } from '../services/contentApi';

export function useSlides(projectId: string | undefined) {
  return useQuery({
    queryKey: ['slides', projectId],
    queryFn: () => getSlides(projectId!),
    enabled: !!projectId,
  });
}
