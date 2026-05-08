import { CalendarDays, Clock, DoorOpen, CheckCircle2, TrendingUp, Plus } from 'lucide-react';
import type { Booking } from '../types/booking';
import { MetricCard } from '../components/MetricCard';
import { formatCurrency } from '../utils/format';

interface DashboardPageProps {
  bookings: Booking[];
  onNewBooking: () => void;
  onViewBooking: (b: Booking) => void;
}

export function DashboardPage({ bookings, onNewBooking, onViewBooking }: DashboardPageProps) {
  const pending = bookings.filter(b => b.status === 'PENDING');
  const checkedIn = bookings.filter(b => b.status === 'CHECKED_IN');
  const completed = bookings.filter(b => b.status === 'COMPLETED');
  const cancelled = bookings.filter(b => b.status === 'CANCELLED');
  const totalRevenue = bookings.filter(b => b.status !== 'CANCELLED').reduce((acc, b) => acc + b.totalValue, 0);
  const recentBookings = [...bookings].sort((a, b) => b.checkIn.localeCompare(a.checkIn)).slice(0, 5);

  const card = 'bg-white border border-slate-200 dark:bg-slate-900/60 dark:border-slate-800/80 rounded-2xl p-5 dark:backdrop-blur-sm shadow-sm dark:shadow-none';

  return (
    <div className="flex flex-col gap-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-800 dark:text-white">Dashboard</h1>
          <p className="text-sm text-slate-400 dark:text-slate-500 mt-0.5">Visão geral das reservas</p>
        </div>
        <button onClick={onNewBooking} className="flex items-center gap-2 bg-indigo-600 hover:bg-indigo-500 text-white px-4 py-2.5 rounded-xl text-sm font-semibold shadow-lg shadow-indigo-500/20 transition-all cursor-pointer">
          <Plus size={16} /> Nova Reserva
        </button>
      </div>

      <div className="grid grid-cols-4 gap-4">
        <MetricCard label="Total de Reservas" value={bookings.length} icon={<CalendarDays size={17} className="text-indigo-600 dark:text-indigo-400" />} gradient="bg-indigo-100 dark:bg-indigo-500/10" subLabel="todas as reservas" />
        <MetricCard label="Pendentes" value={pending.length} icon={<Clock size={17} className="text-amber-600 dark:text-amber-400" />} gradient="bg-amber-100 dark:bg-amber-500/10" subLabel="aguardando check-in" />
        <MetricCard label="Hospedados" value={checkedIn.length} icon={<DoorOpen size={17} className="text-blue-600 dark:text-blue-400" />} gradient="bg-blue-100 dark:bg-blue-500/10" subLabel="hóspedes no momento" />
        <MetricCard label="Receita Total" value={formatCurrency(totalRevenue)} icon={<TrendingUp size={17} className="text-emerald-600 dark:text-emerald-400" />} gradient="bg-emerald-100 dark:bg-emerald-500/10" subLabel={`${completed.length} estadias concluídas`} />
      </div>

      <div className="grid grid-cols-3 gap-4">
        <div className={`col-span-2 ${card}`}>
          <p className="font-mono text-xs uppercase tracking-wider text-slate-400 dark:text-slate-500 mb-4">Distribuição por status</p>
          <div className="flex gap-1.5 h-2.5 rounded-full overflow-hidden mb-4">
            {pending.length > 0 && <div className="bg-amber-400 dark:bg-amber-500 rounded-full" style={{ flex: pending.length }} />}
            {checkedIn.length > 0 && <div className="bg-blue-400 dark:bg-blue-500 rounded-full" style={{ flex: checkedIn.length }} />}
            {completed.length > 0 && <div className="bg-emerald-400 dark:bg-emerald-500 rounded-full" style={{ flex: completed.length }} />}
            {cancelled.length > 0 && <div className="bg-slate-200 dark:bg-slate-700 rounded-full" style={{ flex: cancelled.length }} />}
            {bookings.length === 0 && <div className="bg-slate-100 dark:bg-slate-800 rounded-full flex-1" />}
          </div>
          <div className="flex gap-5 flex-wrap">
            {[
              { label: 'Pendente', count: pending.length, color: 'bg-amber-400' },
              { label: 'Hospedado', count: checkedIn.length, color: 'bg-blue-400' },
              { label: 'Concluído', count: completed.length, color: 'bg-emerald-400' },
              { label: 'Cancelado', count: cancelled.length, color: 'bg-slate-300 dark:bg-slate-700' },
            ].map(s => (
              <div key={s.label} className="flex items-center gap-1.5">
                <div className={`w-2 h-2 rounded-full ${s.color}`} />
                <span className="text-xs text-slate-500"><span className="text-slate-400 dark:text-slate-500">{s.label}</span> ({s.count})</span>
              </div>
            ))}
          </div>
        </div>

        <div className={card}>
          <p className="font-mono text-xs uppercase tracking-wider text-slate-400 dark:text-slate-500 mb-4">Por categoria</p>
          <div className="flex flex-col gap-3.5">
            {(['STANDARD', 'DELUXE', 'SUITE'] as const).map(cat => {
              const count = bookings.filter(b => b.roomCategory === cat).length;
              const pct = bookings.length > 0 ? Math.round((count / bookings.length) * 100) : 0;
              return (
                <div key={cat} className="flex flex-col gap-1.5">
                  <div className="flex justify-between text-xs">
                    <span className="text-slate-400 dark:text-slate-500">{cat.charAt(0) + cat.slice(1).toLowerCase()}</span>
                    <span className="text-slate-700 dark:text-slate-300 font-semibold">{count}</span>
                  </div>
                  <div className="h-1.5 bg-slate-100 dark:bg-slate-800 rounded-full overflow-hidden">
                    <div className={`h-full rounded-full ${cat === 'STANDARD' ? 'bg-slate-400 dark:bg-slate-500' : cat === 'DELUXE' ? 'bg-violet-400 dark:bg-violet-500' : 'bg-amber-400 dark:bg-amber-500'}`} style={{ width: `${pct}%` }} />
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>

      <div className="bg-white border border-slate-200 dark:bg-slate-900/60 dark:border-slate-800/80 rounded-2xl dark:backdrop-blur-sm overflow-hidden shadow-sm dark:shadow-none">
        <div className="flex items-center justify-between px-5 py-4 border-b border-slate-100 dark:border-slate-800/80">
          <p className="font-mono text-xs uppercase tracking-wider text-slate-400 dark:text-slate-500">Reservas Recentes</p>
          <CheckCircle2 size={14} className="text-slate-300 dark:text-slate-700" />
        </div>
        <div className="divide-y divide-slate-100 dark:divide-slate-800/60">
          {recentBookings.length === 0 ? (
            <p className="text-center py-10 text-sm text-slate-400 dark:text-slate-600">Nenhuma reserva ainda.</p>
          ) : recentBookings.map(b => (
            <button key={b.id} onClick={() => onViewBooking(b)} className="w-full flex items-center gap-4 px-5 py-3.5 hover:bg-slate-50 dark:hover:bg-slate-800/40 transition-colors cursor-pointer text-left">
              <div className={`w-8 h-8 rounded-lg flex items-center justify-center text-xs font-bold
                ${b.status === 'PENDING' ? 'bg-amber-100 text-amber-700 dark:bg-amber-500/10 dark:text-amber-400' :
                  b.status === 'CHECKED_IN' ? 'bg-blue-100 text-blue-700 dark:bg-blue-500/10 dark:text-blue-400' :
                  b.status === 'COMPLETED' ? 'bg-emerald-100 text-emerald-700 dark:bg-emerald-500/10 dark:text-emerald-400' :
                  'bg-slate-100 text-slate-400 dark:bg-slate-800 dark:text-slate-500'}`}>
                {b.guestName.charAt(0)}
              </div>
              <div className="flex-1 min-w-0">
                <p className="text-sm font-medium text-slate-800 dark:text-slate-200 truncate">{b.guestName}</p>
                <p className="text-xs text-slate-400 dark:text-slate-600">{b.checkIn} → {b.checkOut}</p>
              </div>
              <div className="text-right">
                <p className="text-sm font-semibold text-slate-700 dark:text-slate-200">{formatCurrency(b.totalValue)}</p>
                <p className="text-xs text-slate-400 dark:text-slate-600">{b.roomCategory.charAt(0) + b.roomCategory.slice(1).toLowerCase()}</p>
              </div>
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}
