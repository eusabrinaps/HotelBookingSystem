import { useState, type FormEvent } from 'react';
import { ConciergeBell, Eye, EyeOff, Loader2 } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { useTheme } from '../context/ThemeContext';

type Tab = 'login' | 'register';

const labelClass = 'font-mono text-xs uppercase tracking-wider text-slate-500 dark:text-slate-500';
const inputClass =
  'w-full bg-white dark:bg-slate-900/80 border border-slate-200 dark:border-slate-700/50 rounded-lg px-3.5 py-2.5 text-sm text-slate-800 dark:text-white placeholder:text-slate-400 dark:placeholder:text-slate-600 focus:outline-none focus:ring-2 focus:ring-indigo-500/30 focus:border-indigo-400 dark:focus:border-indigo-500/50 transition-all';

function PasswordField({
  value,
  onChange,
  placeholder = '••••••••',
  autoComplete = 'current-password',
}: {
  value: string;
  onChange: (v: string) => void;
  placeholder?: string;
  autoComplete?: string;
}) {
  const [show, setShow] = useState(false);
  return (
    <div className="relative">
      <input
        type={show ? 'text' : 'password'}
        required
        autoComplete={autoComplete}
        value={value}
        onChange={e => onChange(e.target.value)}
        placeholder={placeholder}
        className={inputClass + ' pr-10'}
      />
      <button
        type="button"
        tabIndex={-1}
        onClick={() => setShow(s => !s)}
        className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 dark:text-slate-500 hover:text-slate-600 dark:hover:text-slate-300 transition-colors cursor-pointer"
      >
        {show ? <EyeOff size={15} /> : <Eye size={15} />}
      </button>
    </div>
  );
}

function ErrorMsg({ msg }: { msg: string }) {
  return (
    <p className="text-xs font-mono text-red-500 dark:text-red-400 border border-red-300 dark:border-red-500/30 bg-red-50 dark:bg-red-500/10 rounded-lg px-3 py-2">
      {msg}
    </p>
  );
}

export function LoginPage() {
  const { login, register } = useAuth();
  const { theme } = useTheme();
  const [tab, setTab] = useState<Tab>('login');

  const [loginEmail, setLoginEmail] = useState('');
  const [loginPassword, setLoginPassword] = useState('');
  const [loginError, setLoginError] = useState('');
  const [loginLoading, setLoginLoading] = useState(false);

  const [regForm, setRegForm] = useState({ name: '', lastname: '', email: '', password: '', confirm: '' });
  const [regError, setRegError] = useState('');
  const [regLoading, setRegLoading] = useState(false);

  function regField(key: keyof typeof regForm) {
    return (e: React.ChangeEvent<HTMLInputElement>) => setRegForm(f => ({ ...f, [key]: e.target.value }));
  }

  function switchTab(t: Tab) {
    setTab(t);
    setLoginError('');
    setRegError('');
  }

  async function handleLogin(e: FormEvent) {
    e.preventDefault();
    setLoginError('');
    setLoginLoading(true);
    try {
      await login({ username: loginEmail, password: loginPassword });
    } catch {
      setLoginError('E-mail ou senha incorretos.');
    } finally {
      setLoginLoading(false);
    }
  }

  async function handleRegister(e: FormEvent) {
    e.preventDefault();
    setRegError('');
    if (regForm.password !== regForm.confirm) { setRegError('As senhas não coincidem.'); return; }
    if (regForm.password.length < 6) { setRegError('A senha deve ter pelo menos 6 caracteres.'); return; }
    setRegLoading(true);
    try {
      await register({ name: regForm.name, lastname: regForm.lastname, email: regForm.email, password: regForm.password });
      await login({ username: regForm.email, password: regForm.password });
    } catch (err: unknown) {
      const status = (err as { response?: { status?: number } })?.response?.status;
      setRegError(status === 409 ? 'Este e-mail já está cadastrado.' : 'Erro ao criar conta. Tente novamente.');
    } finally {
      setRegLoading(false);
    }
  }

  const isDark = theme === 'dark';

  return (
    <div
      className="min-h-screen relative flex items-center justify-center overflow-hidden px-4 py-10"
      style={isDark ? { background: 'radial-gradient(ellipse at 60% 0%, #1e1b4b 0%, #0f0f1a 50%, #000008 100%)' } : undefined}
    >
      {/* Background — dark only */}
      {isDark && (
        <div className="absolute inset-0 pointer-events-none">
          <div className="absolute top-[-10%] left-[30%] w-[700px] h-[500px] bg-indigo-600/20 blur-[140px] rounded-full" />
          <div className="absolute top-[20%] right-[-10%] w-[400px] h-[400px] bg-violet-600/15 blur-[120px] rounded-full" />
          <div className="absolute bottom-[-5%] left-[-5%] w-[500px] h-[400px] bg-indigo-900/30 blur-[120px] rounded-full" />
          <div className="absolute bottom-[10%] right-[10%] w-[300px] h-[300px] bg-blue-700/10 blur-[100px] rounded-full" />
        </div>
      )}
      {/* Light background */}
      {!isDark && <div className="absolute inset-0 bg-slate-100 pointer-events-none" />}

      <div className="relative w-full max-w-md">
        {/* Logo */}
        <div className="flex justify-center mb-6">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl flex items-center justify-center shadow-lg shadow-indigo-500/30" style={{ background: 'linear-gradient(135deg, #6366f1 0%, #4f46e5 60%, #3730a3 100%)' }}>
              <ConciergeBell size={18} className="text-white" />
            </div>
            <div className="flex items-baseline gap-1">
              <span className={`font-bold text-xl tracking-tight ${isDark ? 'text-white' : 'text-slate-800'}`}>Hotel</span>
              <span className="font-light text-xl tracking-widest" style={{ color: '#6366f1' }}>System</span>
            </div>
          </div>
        </div>

        {/* Card */}
        <div className={`rounded-2xl p-6 sm:p-8 shadow-2xl ${isDark ? 'bg-slate-900/50 border border-slate-800/80 shadow-black/40 backdrop-blur-sm' : 'bg-white border border-slate-200 shadow-slate-200/60'}`}>
          {/* Tab switcher */}
          <div className={`grid grid-cols-2 w-full rounded-xl p-1 mb-6 ${isDark ? 'bg-slate-800/50' : 'bg-slate-100'}`}>
            {(['login', 'register'] as Tab[]).map(t => (
              <button
                key={t}
                onClick={() => switchTab(t)}
                className={`py-2 font-mono text-xs uppercase tracking-wider rounded-lg transition-all cursor-pointer ${
                  tab === t
                    ? isDark ? 'bg-slate-700 text-white shadow' : 'bg-white text-slate-800 shadow-sm'
                    : isDark ? 'text-slate-500 hover:text-slate-300' : 'text-slate-400 hover:text-slate-600'
                }`}
              >
                {t === 'login' ? 'Entrar' : 'Criar conta'}
              </button>
            ))}
          </div>

          {/* Login */}
          {tab === 'login' && (
            <>
              <h1 className={`text-xl sm:text-2xl font-bold text-center mb-1 ${isDark ? 'text-white' : 'text-slate-800'}`}>Bem-vindo de volta</h1>
              <p className="text-sm text-slate-500 text-center mb-6">Acesse o painel de gerenciamento</p>
              <form onSubmit={handleLogin} className="flex flex-col gap-3">
                <div className="flex flex-col gap-1.5">
                  <label className={labelClass}>E-mail</label>
                  <input type="email" required autoComplete="email" value={loginEmail} onChange={e => setLoginEmail(e.target.value)} placeholder="seu@email.com" className={inputClass} />
                </div>
                <div className="flex flex-col gap-1.5">
                  <label className={labelClass}>Senha</label>
                  <PasswordField value={loginPassword} onChange={setLoginPassword} />
                </div>
                {loginError && <ErrorMsg msg={loginError} />}
                <button type="submit" disabled={loginLoading} className="flex items-center justify-center gap-2 w-full mt-1 bg-indigo-600 hover:bg-indigo-500 disabled:opacity-40 disabled:cursor-not-allowed text-white font-mono text-xs uppercase tracking-wider py-2.5 rounded-lg shadow-lg shadow-indigo-500/20 transition-all cursor-pointer">
                  {loginLoading ? <Loader2 size={15} className="animate-spin" /> : 'Entrar'}
                </button>
              </form>
            </>
          )}

          {/* Register */}
          {tab === 'register' && (
            <>
              <h1 className={`text-xl sm:text-2xl font-bold text-center mb-1 ${isDark ? 'text-white' : 'text-slate-800'}`}>Criar conta</h1>
              <p className="text-sm text-slate-500 text-center mb-6">Preencha os dados para criar seu acesso</p>
              <form onSubmit={handleRegister} className="flex flex-col gap-3">
                <div className="grid grid-cols-2 gap-3">
                  <div className="flex flex-col gap-1.5">
                    <label className={labelClass}>Nome</label>
                    <input type="text" required value={regForm.name} onChange={regField('name')} placeholder="João" className={inputClass} />
                  </div>
                  <div className="flex flex-col gap-1.5">
                    <label className={labelClass}>Sobrenome</label>
                    <input type="text" required value={regForm.lastname} onChange={regField('lastname')} placeholder="Silva" className={inputClass} />
                  </div>
                </div>
                <div className="flex flex-col gap-1.5">
                  <label className={labelClass}>E-mail</label>
                  <input type="email" required value={regForm.email} onChange={regField('email')} placeholder="seu@email.com" className={inputClass} />
                </div>
                <div className="flex flex-col gap-1.5">
                  <label className={labelClass}>Senha</label>
                  <PasswordField value={regForm.password} onChange={v => setRegForm(f => ({ ...f, password: v }))} autoComplete="new-password" />
                </div>
                <div className="flex flex-col gap-1.5">
                  <label className={labelClass}>Confirmar senha</label>
                  <PasswordField value={regForm.confirm} onChange={v => setRegForm(f => ({ ...f, confirm: v }))} autoComplete="new-password" />
                </div>
                {regError && <ErrorMsg msg={regError} />}
                <button type="submit" disabled={regLoading} className="flex items-center justify-center gap-2 w-full mt-1 bg-indigo-600 hover:bg-indigo-500 disabled:opacity-40 disabled:cursor-not-allowed text-white font-mono text-xs uppercase tracking-wider py-2.5 rounded-lg shadow-lg shadow-indigo-500/20 transition-all cursor-pointer">
                  {regLoading ? <Loader2 size={15} className="animate-spin" /> : 'Criar conta'}
                </button>
              </form>
            </>
          )}
        </div>

        {/* Footer */}
        <div className="mt-6 flex flex-col items-center gap-2">
          <a
            href="https://github.com/leomsfreitas/HotelBookingSystem"
            target="_blank"
            rel="noreferrer"
            className={`flex items-center gap-1.5 transition-colors ${isDark ? 'text-slate-600 hover:text-slate-400' : 'text-slate-400 hover:text-slate-600'}`}
          >
            <svg width="15" height="15" viewBox="0 0 24 24" fill="currentColor" aria-hidden>
              <path d="M12 .5C5.6.5.5 5.6.5 12c0 5.1 3.3 9.4 7.9 10.9.6.1.8-.3.8-.6v-2c-3.2.7-3.9-1.5-3.9-1.5-.5-1.3-1.3-1.7-1.3-1.7-1-.7.1-.7.1-.7 1.2.1 1.8 1.2 1.8 1.2 1 1.8 2.7 1.3 3.4 1 .1-.8.4-1.3.8-1.6-2.6-.3-5.3-1.3-5.3-5.7 0-1.3.4-2.3 1.2-3.1-.1-.3-.5-1.5.1-3.2 0 0 1-.3 3.2 1.2.9-.3 1.9-.4 2.9-.4 1 0 2 .1 2.9.4 2.2-1.5 3.2-1.2 3.2-1.2.6 1.7.2 2.9.1 3.2.7.8 1.2 1.8 1.2 3.1 0 4.4-2.7 5.4-5.3 5.7.4.4.8 1.1.8 2.2v3.3c0 .3.2.7.8.6 4.6-1.5 7.9-5.8 7.9-10.9C23.5 5.6 18.4.5 12 .5z" />
            </svg>
            <span className="font-mono text-[10px] uppercase tracking-wider">HotelBookingSystem</span>
          </a>
          <p className={`font-mono text-[10px] tracking-wide ${isDark ? 'text-slate-700' : 'text-slate-400'}`}>
            desenvolvido por{' '}
            <a href="https://github.com/leomsfreitas" target="_blank" rel="noreferrer" className={`transition-colors ${isDark ? 'text-slate-500 hover:text-slate-300' : 'text-slate-500 hover:text-slate-700'}`}>@leomsfreitas</a>
            {' '}e{' '}
            <a href="https://github.com/bernardoffilho" target="_blank" rel="noreferrer" className={`transition-colors ${isDark ? 'text-slate-500 hover:text-slate-300' : 'text-slate-500 hover:text-slate-700'}`}>@bernardoffilho</a>
          </p>
        </div>
      </div>
    </div>
  );
}
