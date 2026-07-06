import { useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { settingsApi } from '../services/api';

/**
 * Syncs theme from global settings to the document root (class "dark" for Tailwind).
 * Must be rendered inside QueryClientProvider so useQuery works.
 */
export default function ThemeSync() {
  const { data: settings } = useQuery({
    queryKey: ['settings'],
    queryFn: settingsApi.getSettings,
    staleTime: 30_000,
  });

  useEffect(() => {
    const root = document.documentElement;
    if (settings?.darkMode) {
      root.classList.add('dark');
    } else {
      root.classList.remove('dark');
    }
  }, [settings?.darkMode]);

  return null;
}
