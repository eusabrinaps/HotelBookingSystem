import { useState, type FormEvent } from 'react';
import { Hotel, Eye, EyeOff, UserPlus } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

interface RegisterPageProps {
  onGoToLogin: () => void;
}

export function RegisterPage({ onGoToLogin }: RegisterPageProps) {
  const { register, login } = useAuth();
  const [form, setForm] = useState({ name: '', lastname: '', email: '', password: '', confirm: '' });
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  function field(key: keyof typeof form) {
    return (e: React.ChangeEvent<HTMLInputElement>) => setForm(f => ({ ...f, [key]: e.target.value }));
  }

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setError('');
    if (form.password !== form.confirm) {
      setError('As senhas não coincidem.');
      return;
    }
    if (form.password.length < 6) {
      setError('A senha deve ter pelo menos 6 caracteres.');
      return;
    }
    setLoading(true);
    try {
      await register({ name: form.name, lastname: form.lastname, email: form.email, password: form.password });
      await login({ username: form.email, password: form.password });
      setSuccess(true);
    } catch (err: unknown) {
      const status = (err as { response?: { status?: number } })?.response?.status;
      setError(status === 409 ? 'Este e-mail já está cadastrado.' : 'Erro ao criar conta. Tente novamente.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="min-h-screen bg-slate-950 flex items-center justify-center p-8">
      <div className="w-full max-w-md">
        {/* Logo */}
        <div className="flex items-center gap-2 mb-8">
          <div className="w-9 h-9 bg-indigo-500 rounded-xl flex items-center justify-center shadow-lg shadow-indigo-500/40">
            <Hotel size={18} className="text-white" />
          </div>
          <span className="text-white font-semibold text-lg">HotelSystem</span>
        </div>

        <h2 className="text-2xl font-bold text-white mb-1">Criar conta</h2>
        <p className="text-slate-400 text-sm mb-8">Preencha os dados para criar seu acesso</p>

        {success ? (
          <div className="bg-emerald-500/10 border border-emerald-500/30 rounded-2xl p-6 text-center">
            <p className="text-emerald-400 font-semibold">Conta criada com sucesso!</p>
            <p className="text-slate-400 text-sm mt-1">Redirecionando para o login...</p>
          </div>
        ) : (
          <form onSubmit={handleSubmit} className="flex flex-col gap-4">
            <div className="grid grid-cols-2 gap-3">
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-slate-300">Nome</label>
                <input
                  type="text"
                  required
                  value={form.name}
                  onChange={field('name')}
                  placeholder="João"
                  className="bg-slate-800/60 border border-slate-700 rounded-xl px-4 py-3 text-sm text-white placeholder:text-slate-500 focus:outline-none focus:ring-2 focus:ring-indigo-500/50 focus:border-indigo-500 transition-all"
                />
              </div>
              <div className="flex flex-col gap-1.5">
                <label className="text-sm font-medium text-slate-300">Sobrenome</label>
                <input
                  type="text"
                  required
                  value={form.lastname}
                  onChange={field('lastname')}
                  placeholder="Silva"
                  className="bg-slate-800/60 border border-slate-700 rounded-xl px-4 py-3 text-sm text-white placeholder:text-slate-500 focus:outline-none focus:ring-2 focus:ring-indigo-500/50 focus:border-indigo-500 transition-all"
                />
              </div>
            </div>

            <div className="flex flex-col gap-1.5">
              <label className="text-sm font-medium text-slate-300">E-mail</label>
              <input
                type="email"
                required
                value={form.email}
                onChange={field('email')}
                placeholder="seu@email.com"
                className="bg-slate-800/60 border border-slate-700 rounded-xl px-4 py-3 text-sm text-white placeholder:text-slate-500 focus:outline-none focus:ring-2 focus:ring-indigo-500/50 focus:border-indigo-500 transition-all"
              />
            </div>

            <div className="flex flex-col gap-1.5">
              <label className="text-sm font-medium text-slate-300">Senha</label>
              <div className="relative">
                <input
                  type={showPassword ? 'text' : 'password'}
                  required
                  value={form.password}
                  onChange={field('password')}
                  placeholder="••••••••"
                  className="w-full bg-slate-800/60 border border-slate-700 rounded-xl px-4 py-3 pr-11 text-sm text-white placeholder:text-slate-500 focus:outline-none focus:ring-2 focus:ring-indigo-500/50 focus:border-indigo-500 transition-all"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(v => !v)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-200 cursor-pointer"
                >
                  {showPassword ? <EyeOff size={16} /> : <Eye size={16} />}
                </button>
              </div>
            </div>

            <div className="flex flex-col gap-1.5">
              <label className="text-sm font-medium text-slate-300">Confirmar senha</label>
              <input
                type={showPassword ? 'text' : 'password'}
                required
                value={form.confirm}
                onChange={field('confirm')}
                placeholder="••••••••"
                className="bg-slate-800/60 border border-slate-700 rounded-xl px-4 py-3 text-sm text-white placeholder:text-slate-500 focus:outline-none focus:ring-2 focus:ring-indigo-500/50 focus:border-indigo-500 transition-all"
              />
            </div>

            {error && (
              <div className="bg-red-500/10 border border-red-500/30 rounded-xl px-4 py-3">
                <p className="text-red-400 text-sm">{error}</p>
              </div>
            )}

            <button
              type="submit"
              disabled={loading}
              className="flex items-center justify-center gap-2 w-full bg-indigo-600 hover:bg-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed text-white font-semibold py-3 rounded-xl shadow-lg shadow-indigo-500/20 transition-all cursor-pointer mt-1"
            >
              {loading ? (
                <span className="w-4 h-4 border-2 border-white/30 border-t-white rounded-full animate-spin" />
              ) : (
                <UserPlus size={16} />
              )}
              {loading ? 'Criando conta...' : 'Criar conta'}
            </button>
          </form>
        )}

        <p className="text-center text-sm text-slate-400 mt-6">
          Já tem conta?{' '}
          <button
            onClick={onGoToLogin}
            className="text-indigo-400 hover:text-indigo-300 font-medium transition-colors cursor-pointer"
          >
            Entrar
          </button>
        </p>
      </div>
    </div>
  );
}
