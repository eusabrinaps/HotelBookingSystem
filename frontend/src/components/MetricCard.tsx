import React from 'react';

interface MetricCardProps {
  label: string;
  value: number | string;
  icon: React.ReactNode;
  gradient: string;
  subLabel?: string;
}

export function MetricCard({ label, value, icon, gradient, subLabel }: MetricCardProps) {
  return (
    <div className="bg-white border border-slate-200 dark:bg-slate-900/60 dark:border-slate-800/80 rounded-2xl p-5 dark:backdrop-blur-sm flex flex-col gap-3 hover:border-slate-300 dark:hover:border-slate-700 transition-colors shadow-sm dark:shadow-none">
      <div className="flex items-center justify-between">
        <span className="font-mono text-xs uppercase tracking-wider text-slate-400 dark:text-slate-500">{label}</span>
        <div className={`w-9 h-9 rounded-xl flex items-center justify-center ${gradient}`}>
          {icon}
        </div>
      </div>
      <div>
        <p className="text-3xl font-bold text-slate-800 dark:text-white tracking-tight">{value}</p>
        {subLabel && <p className="text-xs text-slate-400 dark:text-slate-600 mt-0.5">{subLabel}</p>}
      </div>
    </div>
  );
}
