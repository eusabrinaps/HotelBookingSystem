import { Clock, CheckCircle2, XCircle, DoorOpen } from 'lucide-react';
import type { BookingStatus } from '../types/booking';
import { STATUS_LABELS } from '../types/booking';

const config: Record<BookingStatus, { icon: React.ReactNode; light: string; dark: string }> = {
  PENDING: {
    icon: <Clock size={11} strokeWidth={2.5} />,
    light: 'bg-amber-50 text-amber-700 border border-amber-200',
    dark: 'bg-amber-500/10 text-amber-400 border border-amber-500/20',
  },
  CHECKED_IN: {
    icon: <DoorOpen size={11} strokeWidth={2.5} />,
    light: 'bg-blue-50 text-blue-700 border border-blue-200',
    dark: 'bg-blue-500/10 text-blue-400 border border-blue-500/20',
  },
  COMPLETED: {
    icon: <CheckCircle2 size={11} strokeWidth={2.5} />,
    light: 'bg-emerald-50 text-emerald-700 border border-emerald-200',
    dark: 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20',
  },
  CANCELLED: {
    icon: <XCircle size={11} strokeWidth={2.5} />,
    light: 'bg-slate-100 text-slate-500 border border-slate-200',
    dark: 'bg-slate-800 text-slate-500 border border-slate-700',
  },
};

export function StatusBadge({ status }: { status: BookingStatus }) {
  const { icon, light, dark } = config[status];
  return (
    <span className={`inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-xs font-mono font-medium ${light} dark:${dark}`}>
      {icon}
      {STATUS_LABELS[status]}
    </span>
  );
}
