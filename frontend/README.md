# Frontend — HotelSystem

Interface web do sistema de gestão de reservas, integrada ao backend via API REST com autenticação JWT.

---

## Pré-requisitos

- Node.js 18+ e npm
- Backend rodando na porta `8080`

---

## Configuração

Instale as dependências e configure a variável de ambiente:

```bash
npm install
cp .env.example .env.local
```

O arquivo `.env.local` define o endereço do backend:

```
VITE_API_URL=http://localhost:8080
```

---

## Executar

```bash
npm run dev
```

Acesse em `http://localhost:5173`.

---

## Build

```bash
npm run build
```

Os arquivos gerados ficam em `dist/`. Para visualizar o build localmente:

```bash
npm run preview
```
