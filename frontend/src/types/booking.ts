export type BookingStatus = 'PENDING' | 'CHECKED_IN' | 'COMPLETED' | 'CANCELLED';
export type RoomCategory = 'STANDARD' | 'DELUXE' | 'SUITE';

export interface Booking {
  id: string;
  guestName: string;
  guestCpf: string;
  roomCategory: RoomCategory;
  checkIn: string;
  checkOut: string;
  totalValue: number;
  status: BookingStatus;
}

export interface NewBookingForm {
  guestName: string;
  guestCpf: string;
  roomCategory: RoomCategory;
  checkIn: string;
  checkOut: string;
}

export const ROOM_RATES: Record<RoomCategory, number> = {
  STANDARD: 150,
  DELUXE: 250,
  SUITE: 450,
};

export const ROOM_LABELS: Record<RoomCategory, string> = {
  STANDARD: 'Standard',
  DELUXE: 'Deluxe',
  SUITE: 'Suite',
};

export const STATUS_LABELS: Record<BookingStatus, string> = {
  PENDING: 'Pendente',
  CHECKED_IN: 'Hospedado',
  COMPLETED: 'Concluído',
  CANCELLED: 'Cancelado',
};
