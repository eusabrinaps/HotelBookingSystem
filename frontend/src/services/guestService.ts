import { api } from './api';

export interface GuestResponse { id: string; name: string; cpf: string; }
export interface CreateGuestData { name: string; cpf: string; }

export async function listGuests(): Promise<GuestResponse[]> {
  const res = await api.get<GuestResponse[]>('/api/v1/guests');
  return res.data;
}

export async function createGuest(data: CreateGuestData): Promise<GuestResponse> {
  const res = await api.post<GuestResponse>('/api/v1/guests', data);
  return res.data;
}
