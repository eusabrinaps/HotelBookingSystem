import { LayoutDashboard, CalendarDays, Users, ConciergeBell, LogOut, Sun, Moon } from 'lucide-react';
import { useTheme } from '../context/ThemeContext';

interface SidebarProps {
  activePage: string;
  onNavigate: (page: string) => void;
  onLogout: () => void;
}

const navItems = [
  { id: 'dashboard', icon: LayoutDashboard, label: 'Dashboard' },
  { id: 'bookings', icon: CalendarDays, label: 'Reservas' },
  { id: 'guests', icon: Users, label: 'Hóspedes' },
];

export function Sidebar({ activePage, onNavigate, onLogout }: SidebarProps) {
  const { theme, toggle } = useTheme();

  return (
    <aside className="w-60 min-h-screen flex flex-col fixed left-0 top-0 bottom-0 z-10 border-r bg-white border-slate-200 dark:border-slate-800/80" style={{ background: theme === 'dark' ? '#0a0a14' : undefined }}>
      {/* Logo */}
      <div className="flex items-center gap-3 px-5 py-5 border-b border-slate-200 dark:border-slate-800/80">
        <div className="w-9 h-9 rounded-xl flex items-center justify-center shadow-lg shadow-indigo-500/30" style={{ background: 'linear-gradient(135deg, #6366f1 0%, #4f46e5 60%, #3730a3 100%)' }}>
          <ConciergeBell size={17} className="text-white" />
        </div>
        <div className="flex items-baseline gap-1">
          <span className="font-bold text-base tracking-tight text-slate-900 dark:text-white">Hotel</span>
          <span className="font-light text-base tracking-widest" style={{ color: '#818cf8' }}>System</span>
        </div>
      </div>

      {/* Nav */}
      <nav className="flex-1 p-3 flex flex-col gap-0.5 mt-2">
        {navItems.map(({ id, icon: Icon, label }) => (
          <button
            key={id}
            onClick={() => onNavigate(id)}
            className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl text-sm font-medium transition-all text-left cursor-pointer
              ${activePage === id
                ? 'bg-indigo-50 text-indigo-700 border border-indigo-200 dark:bg-indigo-500/15 dark:text-indigo-300 dark:border-indigo-500/20'
                : 'text-slate-500 hover:bg-slate-100 hover:text-slate-800 dark:hover:bg-slate-800/60 dark:hover:text-slate-200'
              }`}
          >
            <Icon size={17} strokeWidth={activePage === id ? 2.5 : 2} />
            {label}
          </button>
        ))}
      </nav>

      {/* Bottom */}
      <div className="p-3 border-t border-slate-200 dark:border-slate-800/80 flex flex-col gap-1">
        {/* Theme toggle */}
        <button
          onClick={toggle}
          className="w-full flex items-center gap-3 px-3 py-2.5 rounded-xl text-sm font-medium text-slate-500 hover:bg-slate-100 dark:hover:bg-slate-800/60 hover:text-slate-700 dark:hover:text-slate-300 transition-all cursor-pointer text-left"
        >
          {theme === 'dark' ? <Sun size={17} strokeWidth={2} /> : <Moon size={17} strokeWidth={2} />}
          {theme === 'dark' ? 'Tema claro' : 'Tema escuro'}
        </button>

        <button
          onClick={onLogout}
          className="w-full flex items-center gap-3 px-3 py-2.5 rounded-xl text-sm font-medium text-slate-500 hover:bg-red-50 hover:text-red-500 dark:hover:bg-red-500/10 dark:hover:text-red-400 transition-all cursor-pointer text-left"
        >
          <LogOut size={17} strokeWidth={2} />
          Sair
        </button>
      </div>
    </aside>
  );
}
