import { Link, Outlet, useLocation } from 'react-router-dom';

export default function Layout() {
  const location = useLocation();

  const menuItems = [
    { path: '/crear', label: 'Crear' },
    { path: '/proyectos', label: 'Proyectos' },
    { path: '/ajustes', label: 'Ajustes' },
  ];

  return (
    <div className="flex h-screen bg-gray-100">
      {/* Sidebar */}
      <aside className="w-64 bg-white shadow-md">
        <div className="p-6">
          <h1 className="text-2xl font-bold text-gray-800">AyG Presentaciones</h1>
        </div>
        <nav className="mt-6">
          {menuItems.map((item) => {
            const isActive = location.pathname === item.path || 
                            (item.path === '/proyectos' && location.pathname.startsWith('/proyectos/'));
            return (
              <Link
                key={item.path}
                to={item.path}
                className={`block px-6 py-3 text-sm font-medium transition-colors ${
                  isActive
                    ? 'bg-blue-50 text-blue-600 border-r-4 border-blue-600'
                    : 'text-gray-700 hover:bg-gray-50'
                }`}
              >
                {item.label}
              </Link>
            );
          })}
        </nav>
      </aside>

      {/* Main Content */}
      <main className="flex-1 overflow-auto p-8">
        <Outlet />
      </main>
    </div>
  );
}
