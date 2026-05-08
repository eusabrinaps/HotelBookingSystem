import { api } from './api';

export interface LoginData { username: string; password: string; }
export interface RegisterData { name: string; lastname: string; email: string; password: string; }

export async function login(data: LoginData): Promise<string> {
  const res = await api.post<{ token: string }>('/api/v1/authenticate', data);
  return res.data.token;
}

export async function register(data: RegisterData): Promise<{ id: string }> {
  const res = await api.post<{ id: string }>('/api/v1/register', data);
  return res.data;
}
