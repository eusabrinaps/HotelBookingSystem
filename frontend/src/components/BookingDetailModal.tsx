import { X, User, CreditCard, CalendarDays, Moon, DollarSign, Clock, DoorOpen, CheckCircle2, XCircle, LogIn, LogOut, Pencil, Ban } from 'lucide-react';
import type { Booking, BookingStatus } from '../types/booking';
import { StatusBadge } from './StatusBadge';
import { RoomBadge } from './RoomBadge';
import { formatCurrency, formatDateLong, diffDays, shortId } from '../utils/format';

interface BookingDetailModalProps {
  booking: Booking | null;
  onClose: () => void;
  onEdit: (booking: Booking) => void;
  onCancel: (id: string) => void;
  onCheckIn: (id: string) => void;
  onCheckOut: (id: string) => void;
}

const timelineSteps: { status: BookingStatus; label: string; icon: React.ReactNode }[] = [
  { status: 'PENDING', label: 'Reservado', icon: <Clock size={14} /> },
  { status: 'CHECKED_IN', label: 'Check-in', icon: <DoorOpen size={14} /> },
  { status: 'COMPLETED', label: 'Concluído', icon: <CheckCircle2 size={14} /> },
];

function getTimelineIndex(status: BookingStatus): number {
  if (status === 'PENDING') return 0;
  if (status === 'CHECKED_IN') return 1;
  if (status === 'COMPLETED') return 2;
  return -1;
}

export function BookingDetailModal({ booking, onClose, onEdit, onCancel, onCheckIn, onCheckOut }: BookingDetailModalProps) {
  if (!booking) return null;

  const nights = diffDays(booking.checkIn, booking.checkOut);
  const timelineIdx = getTimelineIndex(booking.status);
  const isCancelled = booking.status === 'CANCELLED';

  const accentColor =
    isCancelled ? 'bg-slate-300 dark:bg-slate-700' :
    booking.status === 'PENDING' ? 'bg-amber-400 dark:bg-amber-500' :
    booking.status === 'CHECKED_IN' ? 'bg-blue-400 dark:bg-blue-500' :
    'bg-emerald-400 dark:bg-emerald-500';

  const infoCell = 'bg-slate-50 dark:bg-slate-800/60 border border-slate-100 dark:border-slate-700/40 rounded-xl p-3.5';

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div className="absolute inset-0 bg-black/30 dark:bg-black/60 backdrop-blur-sm" onClick={onClose} />
      <div className="relative w-full max-w-lg bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800/80 rounded-2xl shadow-2xl overflow-hidden">
        <div className={`h-0.5 w-full ${accentColor}`} />

        <div className="flex items-start justify-between px-6 pt-5 pb-4 border-b border-slate-100 dark:border-slate-800/80">
          <div>
            <div className="flex items-center gap-2 mb-1.5">
              <span className="text-xs font-mono text-slate-400 dark:text-slate-600 bg-slate-100 dark:bg-slate-800 px-2 py-0.5 rounded">#{shortId(booking.id)}</span>
              <StatusBadge status={booking.status} />
            </div>
            <h2 className="text-xl font-bold text-slate-800 dark:text-white">{booking.guestName}</h2>
          </div>
          <button onClick={onClose} className="w-8 h-8 rounded-full hover:bg-slate-100 dark:hover:bg-slate-800 flex items-center justify-center text-slate-400 hover:text-slate-600 dark:hover:text-slate-300 transition-all cursor-pointer">
            <X size={16} />
          </button>
        </div>

        <div className="px-6 py-5 flex flex-col gap-5">
          <div className="grid grid-cols-2 gap-2.5">
            {[
              { icon: <User size={11} />, label: 'Hóspede', value: booking.guestName },
              { icon: <CreditCard size={11} />, label: 'CPF', value: booking.guestCpf },
              { icon: <CalendarDays size={11} />, label: 'Check-in', value: formatDateLong(booking.checkIn) },
              { icon: <CalendarDays size={11} />, label: 'Check-out', value: formatDateLong(booking.checkOut) },
              { icon: <Moon size={11} />, label: 'Duração', value: `${nights} ${nights === 1 ? 'noite' : 'noites'}` },
            ].map(item => (
              <div key={item.label} className={infoCell}>
                <div className="flex items-center gap-1.5 text-slate-400 dark:text-slate-600 text-xs mb-1.5">{item.icon} {item.label}</div>
                <p className="text-sm font-semibold text-slate-800 dark:text-slate-200">{item.value}</p>
              </div>
            ))}
            <div className={infoCell}>
              <div className="flex items-center gap-1.5 text-slate-400 dark:text-slate-600 text-xs mb-1.5"><DollarSign size={11} /> Total</div>
              <p className="text-sm font-bold text-indigo-600 dark:text-indigo-400">{formatCurrency(booking.totalValue)}</p>
            </div>
          </div>

          <div className="flex items-center gap-2">
            <span className="text-xs text-slate-400 dark:text-slate-600">Categoria:</span>
            <RoomBadge category={booking.roomCategory} />
          </div>

          {!isCancelled ? (
            <div>
              <p className="font-mono text-xs uppercase tracking-wider text-slate-400 dark:text-slate-600 mb-3">Progresso</p>
              <div className="flex items-center">
                {timelineSteps.map((step, idx) => (
                  <div key={step.status} className="flex items-center flex-1">
                    <div className="flex flex-col items-center gap-1">
                      <div className={`w-8 h-8 rounded-full flex items-center justify-center transition-all ${
                        idx <= timelineIdx
                          ? 'bg-indigo-500 text-white shadow-lg shadow-indigo-500/30'
                          : 'bg-slate-100 dark:bg-slate-800 text-slate-300 dark:text-slate-600 border border-slate-200 dark:border-slate-700'
                      }`}>{step.icon}</div>
                      <span className={`text-[10px] font-mono font-medium ${idx <= timelineIdx ? 'text-indigo-600 dark:text-indigo-400' : 'text-slate-300 dark:text-slate-700'}`}>{step.label}</span>
                    </div>
                    {idx < timelineSteps.length - 1 && (
                      <div className={`flex-1 h-px mb-4 mx-1 ${idx < timelineIdx ? 'bg-indigo-300 dark:bg-indigo-500/50' : 'bg-slate-200 dark:bg-slate-800'}`} />
                    )}
                  </div>
                ))}
              </div>
            </div>
          ) : (
            <div className="flex items-center gap-2 bg-slate-50 dark:bg-slate-800/60 border border-slate-100 dark:border-slate-700/40 rounded-xl px-4 py-3">
              <XCircle size={14} className="text-slate-400 dark:text-slate-600" />
              <p className="text-sm text-slate-500 dark:text-slate-500">Esta reserva foi cancelada.</p>
            </div>
          )}
        </div>

        {!isCancelled && booking.status !== 'COMPLETED' && (
          <div className="px-6 pb-5 flex gap-2">
            {booking.status === 'PENDING' && (<>
              <button onClick={() => { onEdit(booking); onClose(); }} className="flex items-center gap-1.5 px-4 py-2 rounded-xl border border-slate-200 dark:border-slate-700 text-sm font-medium text-slate-600 dark:text-slate-400 hover:bg-slate-50 dark:hover:bg-slate-800 transition-all cursor-pointer">
                <Pencil size={13} /> Editar
              </button>
              <button onClick={() => { onCheckIn(booking.id); onClose(); }} className="flex items-center gap-1.5 px-4 py-2 rounded-xl bg-blue-500 hover:bg-blue-600 text-white text-sm font-semibold shadow-lg shadow-blue-500/20 transition-all cursor-pointer">
                <LogIn size={13} /> Check-in
              </button>
              <button onClick={() => { onCancel(booking.id); onClose(); }} className="flex items-center gap-1.5 px-4 py-2 rounded-xl border border-red-200 dark:border-red-500/20 text-sm font-medium text-red-500 dark:text-red-400 hover:bg-red-50 dark:hover:bg-red-500/10 transition-all cursor-pointer ml-auto">
                <Ban size={13} /> Cancelar
              </button>
            </>)}
            {booking.status === 'CHECKED_IN' && (
              <button onClick={() => { onCheckOut(booking.id); onClose(); }} className="flex items-center gap-1.5 px-4 py-2 rounded-xl bg-emerald-500 hover:bg-emerald-600 text-white text-sm font-semibold shadow-lg shadow-emerald-500/20 transition-all cursor-pointer">
                <LogOut size={13} /> Finalizar estadia
              </button>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
