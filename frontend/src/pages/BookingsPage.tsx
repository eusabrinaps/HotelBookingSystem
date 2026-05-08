import { Plus, Search, Eye, Pencil, LogIn, LogOut, Ban, ChevronUp, ChevronDown } from 'lucide-react';
import { useState } from 'react';
import type { Booking, BookingStatus, RoomCategory } from '../types/booking';
import { StatusBadge } from '../components/StatusBadge';
import { RoomBadge } from '../components/RoomBadge';
import { formatCurrency, formatDate, diffDays, shortId } from '../utils/format';

interface BookingsPageProps {
  bookings: Booking[];
  onNew: () => void;
  onView: (b: Booking) => void;
  onEdit: (b: Booking) => void;
  onCancel: (id: string) => void;
  onCheckIn: (id: string) => void;
  onCheckOut: (id: string) => void;
}

type SortField = 'guestName' | 'checkIn' | 'totalValue' | 'status';

const STATUS_OPTIONS: { value: BookingStatus | ''; label: string }[] = [
  { value: '', label: 'Todos os status' },
  { value: 'PENDING', label: 'Pendente' },
  { value: 'CHECKED_IN', label: 'Hospedado' },
  { value: 'COMPLETED', label: 'Concluído' },
  { value: 'CANCELLED', label: 'Cancelado' },
];

const CATEGORY_OPTIONS: { value: RoomCategory | ''; label: string }[] = [
  { value: '', label: 'Todas as categorias' },
  { value: 'STANDARD', label: 'Standard' },
  { value: 'DELUXE', label: 'Deluxe' },
  { value: 'SUITE', label: 'Suite' },
];

export function BookingsPage({ bookings, onNew, onView, onEdit, onCancel, onCheckIn, onCheckOut }: BookingsPageProps) {
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState<BookingStatus | ''>('');
  const [categoryFilter, setCategoryFilter] = useState<RoomCategory | ''>('');
  const [sortField, setSortField] = useState<SortField>('checkIn');
  const [sortAsc, setSortAsc] = useState(true);

  function toggleSort(field: SortField) {
    if (sortField === field) setSortAsc(a => !a);
    else { setSortField(field); setSortAsc(true); }
  }

  const filtered = bookings
    .filter(b =>
      (!search || b.guestName.toLowerCase().includes(search.toLowerCase()) || b.guestCpf.includes(search) || shortId(b.id).includes(search.toUpperCase()))
      && (!statusFilter || b.status === statusFilter)
      && (!categoryFilter || b.roomCategory === categoryFilter)
    )
    .sort((a, b) => {
      let cmp = 0;
      if (sortField === 'guestName') cmp = a.guestName.localeCompare(b.guestName);
      if (sortField === 'checkIn') cmp = a.checkIn.localeCompare(b.checkIn);
      if (sortField === 'totalValue') cmp = a.totalValue - b.totalValue;
      if (sortField === 'status') cmp = a.status.localeCompare(b.status);
      return sortAsc ? cmp : -cmp;
    });

  function SortIcon({ field }: { field: SortField }) {
    if (sortField !== field) return <ChevronUp size={13} className="text-slate-300 dark:text-slate-700" />;
    return sortAsc ? <ChevronUp size={13} className="text-indigo-500 dark:text-indigo-400" /> : <ChevronDown size={13} className="text-indigo-500 dark:text-indigo-400" />;
  }

  const selectClass = 'py-2 px-3 rounded-xl border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900/80 text-sm text-slate-600 dark:text-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 cursor-pointer';

  return (
    <div className="flex flex-col gap-5">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-800 dark:text-white">Reservas</h1>
          <p className="text-sm text-slate-400 dark:text-slate-500 mt-0.5">{bookings.length} reservas no total</p>
        </div>
        <button onClick={onNew} className="flex items-center gap-2 bg-indigo-600 hover:bg-indigo-500 text-white px-4 py-2.5 rounded-xl text-sm font-semibold shadow-lg shadow-indigo-500/20 transition-all cursor-pointer">
          <Plus size={16} /> Nova Reserva
        </button>
      </div>

      <div className="bg-white border border-slate-200 dark:bg-slate-900/60 dark:border-slate-800/80 rounded-2xl p-4 flex items-center gap-3 shadow-sm dark:shadow-none">
        <div className="relative flex-1">
          <Search size={14} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 dark:text-slate-600" />
          <input type="text" placeholder="Buscar por nome, CPF ou ID..." value={search} onChange={e => setSearch(e.target.value)}
            className="w-full pl-9 pr-4 py-2 rounded-xl border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900/80 text-sm text-slate-700 dark:text-slate-300 placeholder:text-slate-400 dark:placeholder:text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500/20 transition-all" />
        </div>
        <select value={statusFilter} onChange={e => setStatusFilter(e.target.value as BookingStatus | '')} className={selectClass}>
          {STATUS_OPTIONS.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
        </select>
        <select value={categoryFilter} onChange={e => setCategoryFilter(e.target.value as RoomCategory | '')} className={selectClass}>
          {CATEGORY_OPTIONS.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
        </select>
      </div>

      <div className="bg-white border border-slate-200 dark:bg-slate-900/60 dark:border-slate-800/80 rounded-2xl shadow-sm dark:shadow-none overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-slate-100 dark:border-slate-800/80 bg-slate-50/50 dark:bg-transparent">
                {[
                  { label: 'ID', field: null },
                  { label: 'Hóspede', field: 'guestName' as SortField },
                  { label: 'Categoria', field: null },
                  { label: 'Check-in', field: 'checkIn' as SortField },
                  { label: 'Check-out', field: null },
                  { label: 'Noites', field: null },
                  { label: 'Total', field: 'totalValue' as SortField },
                  { label: 'Status', field: 'status' as SortField },
                  { label: 'Ações', field: null },
                ].map(col => (
                  <th key={col.label} onClick={() => col.field && toggleSort(col.field)}
                    className={`text-left font-mono text-xs text-slate-400 dark:text-slate-600 px-4 py-3 uppercase tracking-wider whitespace-nowrap ${col.field ? 'cursor-pointer hover:text-slate-600 dark:hover:text-slate-400 select-none' : ''}`}>
                    <div className="flex items-center gap-1">{col.label}{col.field && <SortIcon field={col.field} />}</div>
                  </th>
                ))}
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-50 dark:divide-slate-800/40">
              {filtered.length === 0 ? (
                <tr><td colSpan={9} className="text-center py-12 text-sm text-slate-400 dark:text-slate-600">Nenhuma reserva encontrada.</td></tr>
              ) : filtered.map(b => (
                <tr key={b.id} className="hover:bg-slate-50/60 dark:hover:bg-slate-800/30 transition-colors">
                  <td className="px-4 py-3.5">
                    <span className="text-xs font-mono text-slate-400 dark:text-slate-600 bg-slate-100 dark:bg-slate-800 px-2 py-0.5 rounded">#{shortId(b.id)}</span>
                  </td>
                  <td className="px-4 py-3.5">
                    <p className="text-sm font-medium text-slate-800 dark:text-slate-200">{b.guestName}</p>
                    <p className="text-xs text-slate-400 dark:text-slate-600">{b.guestCpf}</p>
                  </td>
                  <td className="px-4 py-3.5"><RoomBadge category={b.roomCategory} /></td>
                  <td className="px-4 py-3.5 text-sm text-slate-600 dark:text-slate-400">{formatDate(b.checkIn)}</td>
                  <td className="px-4 py-3.5 text-sm text-slate-600 dark:text-slate-400">{formatDate(b.checkOut)}</td>
                  <td className="px-4 py-3.5 text-sm text-slate-500 dark:text-slate-500">{diffDays(b.checkIn, b.checkOut)}n</td>
                  <td className="px-4 py-3.5 text-sm font-semibold text-slate-700 dark:text-slate-300">{formatCurrency(b.totalValue)}</td>
                  <td className="px-4 py-3.5"><StatusBadge status={b.status} /></td>
                  <td className="px-4 py-3.5">
                    <div className="flex items-center gap-1">
                      <ActionBtn icon={<Eye size={13} />} title="Ver detalhes" onClick={() => onView(b)} />
                      {b.status === 'PENDING' && (<>
                        <ActionBtn icon={<Pencil size={13} />} title="Editar" onClick={() => onEdit(b)} />
                        <ActionBtn icon={<LogIn size={13} />} title="Check-in" onClick={() => onCheckIn(b.id)} color="blue" />
                        <ActionBtn icon={<Ban size={13} />} title="Cancelar" onClick={() => onCancel(b.id)} color="red" />
                      </>)}
                      {b.status === 'CHECKED_IN' && <ActionBtn icon={<LogOut size={13} />} title="Check-out" onClick={() => onCheckOut(b.id)} color="emerald" />}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {filtered.length > 0 && (
          <div className="px-4 py-3 border-t border-slate-100 dark:border-slate-800/60 text-xs font-mono text-slate-400 dark:text-slate-600">
            Mostrando {filtered.length} de {bookings.length} reservas
          </div>
        )}
      </div>
    </div>
  );
}

function ActionBtn({ icon, title, onClick, color = 'slate' }: { icon: React.ReactNode; title: string; onClick: () => void; color?: 'slate' | 'blue' | 'red' | 'emerald' }) {
  const colors = {
    slate: 'hover:bg-slate-100 dark:hover:bg-slate-700 text-slate-400 hover:text-slate-600 dark:hover:text-slate-300',
    blue: 'hover:bg-blue-50 dark:hover:bg-blue-500/10 text-blue-400 hover:text-blue-600 dark:hover:text-blue-400',
    red: 'hover:bg-red-50 dark:hover:bg-red-500/10 text-red-400 hover:text-red-600 dark:hover:text-red-400',
    emerald: 'hover:bg-emerald-50 dark:hover:bg-emerald-500/10 text-emerald-400 hover:text-emerald-600 dark:hover:text-emerald-400',
  };
  return (
    <button title={title} onClick={onClick} className={`w-6 h-6 rounded-lg flex items-center justify-center transition-all cursor-pointer ${colors[color]}`}>{icon}</button>
  );
}
