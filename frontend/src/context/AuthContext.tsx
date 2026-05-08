import { createContext, useContext, useState, useCallback, type ReactNode } from 'react';
import { login as loginApi, register as registerApi, type LoginData, type RegisterData } from '../services/authService';

interface AuthContextType {
  isAuthenticated: boolean;
  login: (data: LoginData) => Promise<void>;
  register: (data: RegisterData) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [isAuthenticated, setIsAuthenticated] = useState(() => !!localStorage.getItem('token'));

  const login = useCallback(async (data: LoginData) => {
    const token = await loginApi(data);
    localStorage.setItem('token', token);
    setIsAuthenticated(true);
  }, []);

  const register = useCallback(async (data: RegisterData) => {
    await registerApi(data);
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    setIsAuthenticated(false);
  }, []);

  return (
    <AuthContext.Provider value={{ isAuthenticated, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
