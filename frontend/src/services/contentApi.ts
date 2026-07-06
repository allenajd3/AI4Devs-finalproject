import axios from './api';
import type { Slide } from '../types/slide';

export async function generateContent(
  projectId: string,
  onProgress: (message: string) => void,
  onComplete: () => void,
  onError: (error: string) => void
): Promise<void> {
  return new Promise((resolve, reject) => {
    const eventSource = new EventSource(
      `/api/projects/${projectId}/generate-content`
    );

    eventSource.addEventListener('progress', (event) => {
      onProgress(event.data);
    });

    eventSource.addEventListener('complete', () => {
      eventSource.close();
      onComplete();
      resolve();
    });

    eventSource.addEventListener('error', (event) => {
      const errorMessage = (event as MessageEvent).data || 'Error durante la generación';
      console.error('SSE Error message:', errorMessage);
      eventSource.close();
      onError(errorMessage);
      reject(new Error(errorMessage));
    });

    eventSource.onerror = (error) => {
      console.error('SSE Connection Error:', error);
      eventSource.close();
      onError('Error de conexión con el servidor. Por favor, intenta de nuevo.');
      reject(error);
    };

    // Timeout after 10 minutes
    setTimeout(() => {
      if (eventSource.readyState !== EventSource.CLOSED) {
        eventSource.close();
        onError('La generación está tardando demasiado. Por favor, verifica el estado del proyecto.');
        reject(new Error('Timeout'));
      }
    }, 600000);
  });
}

export async function getSlides(projectId: string): Promise<Slide[]> {
  const response = await axios.get<Slide[]>(`/projects/${projectId}/slides`);
  return response.data;
}
