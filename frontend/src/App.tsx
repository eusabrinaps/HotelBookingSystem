import { useState, useCallback, useEffect } from 'react';
import { AuthProvider, useAuth } from './context/AuthContext';
import { ThemeProvider } from './context/ThemeContext';
import { Sidebar } from './components/Sidebar';
import { DashboardPage } from './pages/DashboardPage';
import { BookingsPage } from './pages/BookingsPage';
import { LoginPage } from './pages/LoginPage';
import { BookingDrawer } from './components/BookingDrawer';
import { BookingDetailModal } from './components/BookingDetailModal';
import { ConfirmDialog } from './components/ConfirmDialog';
import { Toast } from './components/Toast';
import type { Booking, NewBookingForm } from './types/booking';
import * as bookingApi from './services/bookingService';
import * as guestApi from './services/guestService';
import type { GuestResponse } from './services/guestService';

type Page = 'dashboard' | 'bookings' | 'guests';
type ToastState = { message: string; type: 'success' | 'error' } | null;
type ConfirmState = { title: string; message: string; onConfirm: () => void } | null;
function mapBooking(b: bookingApi.BookingResponse): Booking {
  return {
    id: b.id,
    guestName: b.guestName,
    guestCpf: b.guestCpf,
    roomCategory: b.roomCategory as Booking['roomCategory'],
    checkIn: b.checkIn,
    checkOut: b.checkOut,
    totalValue: b.totalValue,
    status: b.status as Booking['status'],
  };
}

function AppContent() {
  const { isAuthenticated, logout } = useAuth();
  const [page, setPage] = useState<Page>('dashboard');

  const [bookings, setBookings] = useState<Booking[]>([]);
  const [guests, setGuests] = useState<GuestResponse[]>([]);
  const [loading, setLoading] = useState(false);

  const [drawerOpen, setDrawerOpen] = useState(false);
  const [editBooking, setEditBooking] = useState<Booking | null>(null);
  const [detailBooking, setDetailBooking] = useState<Booking | null>(null);
  const [toast, setToast] = useState<ToastState>(null);
  const [confirm, setConfirm] = useState<ConfirmState>(null);

  const showToast = useCallback((message: string, type: 'success' | 'error' = 'success') => {
    setToast({ message, type });
  }, []);

  async function fetchBookings() {
    try {
      const data = await bookingApi.listBookings();
      setBookings(data.map(mapBooking));
    } catch {
      showToast('Erro ao carregar reservas.', 'error');
    }
  }

  async function fetchGuests() {
    try {
      const data = await guestApi.listGuests();
      setGuests(data);
    } catch { /* ignore */ }
  }

  useEffect(() => {
    if (isAuthenticated) {
      fetchBookings();
      fetchGuests();
    }
  }, [isAuthenticated]);

  if (!isAuthenticated) {
    return <LoginPage />;
  }

  function handleNewBooking() { setEditBooking(null); setDrawerOpen(true); }
  function handleEditBooking(b: Booking) { setEditBooking(b); setDrawerOpen(true); }

  async function handleSaveBooking(form: NewBookingForm) {
    setLoading(true);
    try {
      if (editBooking) {
        await bookingApi.updateBooking(editBooking.id, {
          roomCategory: form.roomCategory,
          checkIn: form.checkIn,
          checkOut: form.checkOut,
        });
        showToast('Reserva atualizada com sucesso!');
      } else {
        let guestId = guests.find(g => g.cpf === form.guestCpf)?.id;
        if (!guestId) {
          const created = await guestApi.createGuest({ name: form.guestName, cpf: form.guestCpf });
          guestId = created.id;
          setGuests(prev => [...prev, created]);
        }
        await bookingApi.createBooking({
          guestId,
          roomCategory: form.roomCategory,
          checkIn: form.checkIn,
          checkOut: form.checkOut,
        });
        showToast('Reserva criada com sucesso!');
      }
      await fetchBookings();
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } })?.response?.data?.message;
      showToast(msg ?? 'Erro ao salvar reserva.', 'error');
    } finally {
      setLoading(false);
      setDrawerOpen(false);
      setEditBooking(null);
    }
  }

  function handleCancel(id: string) {
    setConfirm({
      title: 'Cancelar reserva',
      message: 'Tem certeza? Esta ação não pode ser desfeita.',
      onConfirm: async () => {
        try {
          await bookingApi.cancelBooking(id);
          await fetchBookings();
          showToast('Reserva cancelada.');
        } catch {
          showToast('Erro ao cancelar.', 'error');
        }
        setConfirm(null);
      },
    });
  }

  async function handleCheckIn(id: string) {
    try {
      await bookingApi.checkInBooking(id);
      await fetchBookings();
      showToast('Check-in realizado com sucesso!');
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } })?.response?.data?.message;
      showToast(msg ?? 'Erro ao realizar check-in.', 'error');
    }
  }

  function handleCheckOut(id: string) {
    setConfirm({
      title: 'Finalizar estadia',
      message: 'Confirmar o check-out e finalizar a estadia do hóspede?',
      onConfirm: async () => {
        try {
          await bookingApi.checkOutBooking(id);
          await fetchBookings();
          showToast('Estadia concluída com sucesso!');
        } catch {
          showToast('Erro ao finalizar estadia.', 'error');
        }
        setConfirm(null);
      },
    });
  }

  const syncedDetail = detailBooking ? bookings.find(b => b.id === detailBooking.id) ?? null : null;

  return (
    <div className="flex min-h-screen bg-slate-100 dark:bg-transparent dark:[background:radial-gradient(ellipse_at_60%_0%,#1e1b4b_0%,#0f0f1a_50%,#000008_100%)]">
      <Sidebar activePage={page} onNavigate={p => setPage(p as Page)} onLogout={logout} />

      <main className="ml-60 flex-1 p-7 min-h-screen">
        {loading && (
          <div className="fixed inset-0 z-[80] bg-slate-900/20 flex items-center justify-center">
            <div className="w-8 h-8 border-2 border-indigo-500/30 border-t-indigo-500 rounded-full animate-spin" />
          </div>
        )}

        {page === 'dashboard' && (
          <DashboardPage bookings={bookings} onNewBooking={handleNewBooking} onViewBooking={setDetailBooking} />
        )}
        {page === 'bookings' && (
          <BookingsPage
            bookings={bookings}
            onNew={handleNewBooking}
            onView={setDetailBooking}
            onEdit={handleEditBooking}
            onCancel={handleCancel}
            onCheckIn={handleCheckIn}
            onCheckOut={handleCheckOut}
          />
        )}
        {page === 'guests' && (
          <div className="flex flex-col gap-5">
            <div>
              <h1 className="text-2xl font-bold text-slate-800 dark:text-white">Hóspedes</h1>
              <p className="text-sm text-slate-400 dark:text-slate-500 mt-0.5">{guests.length} hóspedes cadastrados</p>
            </div>
            <div className="bg-white border border-slate-200 dark:bg-slate-900/60 dark:border-slate-800/80 rounded-2xl dark:backdrop-blur-sm overflow-hidden shadow-sm dark:shadow-none">
              {guests.length === 0 ? (
                <div className="p-12 text-center text-sm text-slate-400 dark:text-slate-600">Nenhum hóspede cadastrado ainda.</div>
              ) : (
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-slate-100 dark:border-slate-800/80 bg-slate-50/50 dark:bg-transparent">
                      <th className="text-left font-mono text-xs text-slate-400 dark:text-slate-600 px-4 py-3 uppercase tracking-wider">Nome</th>
                      <th className="text-left font-mono text-xs text-slate-400 dark:text-slate-600 px-4 py-3 uppercase tracking-wider">CPF</th>
                      <th className="text-left font-mono text-xs text-slate-400 dark:text-slate-600 px-4 py-3 uppercase tracking-wider">ID</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-50 dark:divide-slate-800/40">
                    {guests.map(g => (
                      <tr key={g.id} className="hover:bg-slate-50/60 dark:hover:bg-slate-800/30 transition-colors">
                        <td className="px-4 py-3.5 text-sm font-medium text-slate-800 dark:text-slate-200">{g.name}</td>
                        <td className="px-4 py-3.5 text-sm text-slate-500 dark:text-slate-500">{g.cpf}</td>
                        <td className="px-4 py-3.5">
                          <span className="text-xs font-mono text-slate-400 dark:text-slate-600 bg-slate-100 dark:bg-slate-800 px-2 py-0.5 rounded">
                            {g.id.split('-')[0].toUpperCase()}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}
            </div>
          </div>
        )}
      </main>

      <BookingDrawer
        open={drawerOpen}
        onClose={() => { setDrawerOpen(false); setEditBooking(null); }}
        onSave={handleSaveBooking}
        editData={editBooking}
      />
      <BookingDetailModal
        booking={syncedDetail}
        onClose={() => setDetailBooking(null)}
        onEdit={handleEditBooking}
        onCancel={handleCancel}
        onCheckIn={handleCheckIn}
        onCheckOut={handleCheckOut}
      />
      <ConfirmDialog
        open={!!confirm}
        title={confirm?.title ?? ''}
        message={confirm?.message ?? ''}
        onConfirm={confirm?.onConfirm ?? (() => {})}
        onCancel={() => setConfirm(null)}
      />
      {toast && <Toast message={toast.message} type={toast.type} onClose={() => setToast(null)} />}
    </div>
  );
}

export default function App() {
  return (
    <ThemeProvider>
      <AuthProvider>
        <AppContent />
      </AuthProvider>
    </ThemeProvider>
  );
}
