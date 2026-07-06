export const SlideStatus = {
  PENDING: 'PENDING',
  GENERATING: 'GENERATING',
  COMPLETED: 'COMPLETED',
  ERROR: 'ERROR',
} as const;

export type SlideStatus = (typeof SlideStatus)[keyof typeof SlideStatus];

export interface Slide {
  id: string;
  order: number;
  imagePrompt: string;
  imageUrl: string | null;
  status: SlideStatus;
}
