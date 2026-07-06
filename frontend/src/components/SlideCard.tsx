import type { Slide } from '../types/slide';

interface SlideCardProps {
  slide: Slide;
}

const statusColors = {
  PENDING: 'bg-gray-100 text-gray-700',
  GENERATING: 'bg-blue-100 text-blue-700',
  COMPLETED: 'bg-green-100 text-green-700',
  ERROR: 'bg-red-100 text-red-700',
};

const statusLabels = {
  PENDING: 'Pendiente',
  GENERATING: 'Generando',
  COMPLETED: 'Completada',
  ERROR: 'Error',
};

export function SlideCard({ slide }: SlideCardProps) {
  return (
    <div className="bg-white border border-gray-200 rounded-lg overflow-hidden hover:shadow-lg transition-shadow">
      {/* Image Placeholder */}
      <div className="bg-gray-100 h-48 flex items-center justify-center">
        {slide.imageUrl ? (
          <img
            src={slide.imageUrl}
            alt={`Slide ${slide.order}`}
            className="w-full h-full object-cover"
          />
        ) : (
          <div className="text-center text-gray-400">
            <svg
              className="w-16 h-16 mx-auto mb-2"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={1.5}
                d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"
              />
            </svg>
            <p className="text-sm">Imagen pendiente</p>
          </div>
        )}
      </div>

      {/* Content */}
      <div className="p-4">
        <div className="flex items-center justify-between mb-3">
          <span className="inline-flex items-center justify-center w-8 h-8 rounded-full bg-blue-600 text-white font-semibold text-sm">
            {slide.order}
          </span>
          <span
            className={`px-2 py-1 rounded-full text-xs font-medium ${
              statusColors[slide.status]
            }`}
          >
            {statusLabels[slide.status]}
          </span>
        </div>

        <div className="space-y-2">
          <h3 className="text-sm font-medium text-gray-900">Prompt</h3>
          <p className="text-sm text-gray-600 leading-relaxed">
            {slide.imagePrompt}
          </p>
        </div>
      </div>
    </div>
  );
}
