import { Bed, Star, Crown } from 'lucide-react';
import type { RoomCategory } from '../types/booking';
import { ROOM_LABELS } from '../types/booking';

const config: Record<RoomCategory, { icon: React.ReactNode; light: string; dark: string }> = {
  STANDARD: {
    icon: <Bed size={11} strokeWidth={2.5} />,
    light: 'bg-slate-100 text-slate-600 border border-slate-200',
    dark: 'bg-slate-800 text-slate-400 border border-slate-700',
  },
  DELUXE: {
    icon: <Star size={11} strokeWidth={2.5} />,
    light: 'bg-violet-50 text-violet-700 border border-violet-200',
    dark: 'bg-violet-500/10 text-violet-400 border border-violet-500/20',
  },
  SUITE: {
    icon: <Crown size={11} strokeWidth={2.5} />,
    light: 'bg-amber-50 text-amber-700 border border-amber-200',
    dark: 'bg-amber-500/10 text-amber-400 border border-amber-500/20',
  },
};

export function RoomBadge({ category }: { category: RoomCategory }) {
  const { icon, light, dark } = config[category];
  return (
    <span className={`inline-flex items-center gap-1 px-2 py-0.5 rounded-md text-xs font-mono font-semibold ${light} dark:${dark}`}>
      {icon}
      {ROOM_LABELS[category]}
    </span>
  );
}
