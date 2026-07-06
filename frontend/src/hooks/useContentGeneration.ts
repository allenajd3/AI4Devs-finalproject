import { useState, useCallback } from 'react';
import { generateContent } from '../services/contentApi';

export function useContentGeneration() {
  const [isGenerating, setIsGenerating] = useState(false);
  const [progress, setProgress] = useState<string[]>([]);
  const [error, setError] = useState<string | null>(null);

  const startGeneration = useCallback(async (projectId: string) => {
    setIsGenerating(true);
    setProgress([]);
    setError(null);

    try {
      await generateContent(
        projectId,
        (message) => {
          setProgress((prev) => [...prev, message]);
        },
        () => {
          setIsGenerating(false);
        },
        (errorMessage) => {
          setError(errorMessage);
          setIsGenerating(false);
        }
      );
    } catch (err) {
      console.error('Generation error:', err);
      if (!error) {
        setError('Error inesperado durante la generación');
      }
      setIsGenerating(false);
    }
  }, [error]);

  const reset = useCallback(() => {
    setIsGenerating(false);
    setProgress([]);
    setError(null);
  }, []);

  return {
    isGenerating,
    progress,
    error,
    startGeneration,
    reset,
  };
}
