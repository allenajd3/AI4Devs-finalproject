import { Link, Outlet, useLocation } from 'react-router-dom';
import { Logo } from './Logo';

export default function Layout() {
  const location = useLocation();

  const menuItems = [
    { 
      path: '/crear', 
      label: 'Crear',
      icon: (
        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
        </svg>
      )
    },
    { 
      path: '/proyectos', 
      label: 'Proyectos',
      icon: (
        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z" />
        </svg>
      )
    },
    { 
      path: '/ajustes', 
      label: 'Ajustes',
      icon: (
        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
        </svg>
      )
    },
  ];

  return (
    <div className="flex h-screen bg-gray-50 dark:bg-gray-900">
      {/* Sidebar con diseño profesional */}
      <aside className="w-64 bg-gradient-to-b from-[#5F8FA3] to-[#4A7289] dark:from-gray-900 dark:to-gray-800 shadow-xl flex flex-col">
        {/* Header con Logo y Título */}
        <div className="p-6 bg-white/10 backdrop-blur-sm">
          <div className="flex flex-col items-center gap-3">
            <Logo size="md" variant="white" className="drop-shadow-lg" />
            <div className="text-center">
              <h2 className="text-white font-semibold text-sm tracking-wide">
                Presentaciones IA
              </h2>
            </div>
          </div>
        </div>
        
        {/* Navegación Principal */}
        <nav className="flex-1 px-3 py-6 space-y-1">
          {menuItems.map((item) => {
            const isActive = location.pathname === item.path || 
                            (item.path === '/proyectos' && location.pathname.startsWith('/proyectos/'));
            return (
              <Link
                key={item.path}
                to={item.path}
                className={`
                  flex items-center gap-3 px-4 py-3 rounded-lg
                  font-medium text-sm transition-all duration-200
                  ${isActive
                    ? 'bg-white text-[#5F8FA3] shadow-md transform scale-105'
                    : 'text-white/90 hover:bg-white/20 hover:text-white hover:translate-x-1'
                  }
                `}
              >
                <span className={isActive ? 'text-[#5F8FA3]' : 'text-white/80'}>
                  {item.icon}
                </span>
                <span className="font-medium">
                  {item.label}
                </span>
              </Link>
            );
          })}
        </nav>

        {/* Footer con información */}
        <div className="p-4 border-t border-white/20">
          <div className="text-white/70 text-xs text-center">
            <p className="font-medium">AyG © 2026</p>
          </div>
        </div>
      </aside>

      {/* Main Content con diseño mejorado */}
      <main className="flex-1 overflow-auto bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-gray-100">
        <div className="max-w-7xl mx-auto p-8">
          <Outlet />
        </div>
      </main>
    </div>
  );
}
