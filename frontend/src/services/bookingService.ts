import { api } from './api';

export interface BookingResponse {
  id: string;
  guestId: string;
  guestName: string;
  guestCpf: string;
  roomCategory: string;
  checkIn: string;
  checkOut: string;
  totalValue: number;
  status: string;
}

export interface CreateBookingData {
  guestId: string;
  roomCategory: string;
  checkIn: string;
  checkOut: string;
}

export interface UpdateBookingData {
  roomCategory: string;
  checkIn: string;
  checkOut: string;
}

export async function listBookings(): Promise<BookingResponse[]> {
  const res = await api.get<BookingResponse[]>('/api/v1/bookings');
  return res.data;
}

export async function getBooking(id: string): Promise<BookingResponse> {
  const res = await api.get<BookingResponse>(`/api/v1/bookings/${id}`);
  return res.data;
}

export async function createBooking(data: CreateBookingData): Promise<string> {
  const res = await api.post<string>('/api/v1/bookings', data);
  return res.data;
}

export async function updateBooking(id: string, data: UpdateBookingData): Promise<void> {
  await api.put(`/api/v1/bookings/${id}`, data);
}

export async function cancelBooking(id: string): Promise<void> {
  await api.patch(`/api/v1/bookings/${id}/cancel`);
}

export async function checkInBooking(id: string): Promise<void> {
  await api.patch(`/api/v1/bookings/${id}/checkin`);
}

export async function checkOutBooking(id: string): Promise<void> {
  await api.patch(`/api/v1/bookings/${id}/checkout`);
}
