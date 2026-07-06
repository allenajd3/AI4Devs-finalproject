import { useState } from 'react';
import { Link, Outlet, useLocation } from 'react-router-dom';
import { Logo } from './Logo';

export default function Layout() {
  const location = useLocation();
  const [collapsed, setCollapsed] = useState<boolean>(
    () => localStorage.getItem('sidebar-collapsed') === 'true'
  );

  function toggleSidebar() {
    setCollapsed((prev) => {
      const next = !prev;
      localStorage.setItem('sidebar-collapsed', String(next));
      return next;
    });
  }

  const menuItems = [
    {
      path: '/crear',
      label: 'Crear',
      icon: (
        <svg className="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
        </svg>
      )
    },
    {
      path: '/proyectos',
      label: 'Proyectos',
      icon: (
        <svg className="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z" />
        </svg>
      )
    },
    {
      path: '/ajustes',
      label: 'Ajustes',
      icon: (
        <svg className="w-5 h-5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
        </svg>
      )
    },
  ];

  return (
    <div className="flex h-screen bg-gray-50 dark:bg-gray-900">
      {/* Sidebar */}
      <aside className={`
        ${collapsed ? 'w-16' : 'w-64'}
        transition-all duration-300 overflow-hidden
        bg-gradient-to-b from-[#5F8FA3] to-[#4A7289] dark:from-gray-900 dark:to-gray-800
        shadow-xl flex flex-col
      `}>
        {/* Header con Logo y Título */}
        <div className="p-4 bg-white/10 backdrop-blur-sm">
          <div className="flex flex-col items-center gap-2">
            <Logo size={collapsed ? 'sm' : 'md'} variant="white" className="drop-shadow-lg transition-all duration-300" />
            {!collapsed && (
              <h2 className="text-white font-semibold text-sm tracking-wide text-center">
                Presentaciones IA
              </h2>
            )}
          </div>
        </div>

        {/* Navegación Principal */}
        <nav className="flex-1 px-2 py-4 space-y-1">
          {menuItems.map((item) => {
            const isActive = location.pathname === item.path ||
                            (item.path === '/proyectos' && location.pathname.startsWith('/proyectos/'));
            return (
              <Link
                key={item.path}
                to={item.path}
                title={collapsed ? item.label : undefined}
                className={`
                  flex items-center py-3 rounded-lg
                  font-medium text-sm transition-all duration-200
                  ${collapsed ? 'justify-center px-0' : 'gap-3 px-4'}
                  ${isActive
                    ? 'bg-white text-[#5F8FA3] shadow-md'
                    : 'text-white/90 hover:bg-white/20 hover:text-white'
                  }
                  ${!collapsed && !isActive ? 'hover:translate-x-1' : ''}
                  ${isActive && !collapsed ? 'transform scale-105' : ''}
                `}
              >
                <span className={isActive ? 'text-[#5F8FA3]' : 'text-white/80'}>
                  {item.icon}
                </span>
                {!collapsed && (
                  <span className="font-medium">{item.label}</span>
                )}
              </Link>
            );
          })}
        </nav>

        {/* Botón de toggle */}
        <div className="px-2 pb-3">
          <button
            onClick={toggleSidebar}
            className="w-full flex items-center justify-center py-2 rounded-lg text-white/70 hover:bg-white/20 hover:text-white transition-all duration-200"
            title={collapsed ? 'Expandir menú' : 'Colapsar menú'}
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              {collapsed
                ? <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 5l7 7-7 7M5 5l7 7-7 7" />
                : <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 19l-7-7 7-7m8 14l-7-7 7-7" />
              }
            </svg>
          </button>
        </div>

        {/* Footer con información */}
        {!collapsed && (
          <div className="p-4 border-t border-white/20">
            <div className="text-white/70 text-xs text-center">
              <p className="font-medium">AyG © 2026</p>
            </div>
          </div>
        )}
      </aside>

      {/* Main Content */}
      <main className="flex-1 overflow-auto bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-gray-100">
        <div className="max-w-7xl mx-auto p-8">
          <Outlet />
        </div>
      </main>
    </div>
  );
}
