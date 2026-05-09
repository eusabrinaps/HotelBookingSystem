# HotelSystem

Sistema de gerenciamento de reservas de hotel com backend em Spring Boot e frontend em React.

## Estrutura do projeto

```
HotelSystem/
├── src/                  → código-fonte do backend (Spring Boot)
├── frontend/             → interface web (React)
├── hotel.db              → banco de dados SQLite (gerado automaticamente)
└── pom.xml
```

---

## Pré-requisitos

- Java 21
- Maven
- Node.js 18+ e npm

---

## Backend

### Configurar e rodar

```bash
mvn spring-boot:run
```

O servidor sobe na porta `8080`. O banco de dados `hotel.db` é criado automaticamente na raiz do projeto na primeira execução. O Flyway executa automaticamente a migration `V1_mock_data.sql`, populando o banco com dados de exemplo.

### Rodar os testes

```bash
mvn test
```

### Endpoints

Todos os endpoints exigem autenticação via JWT, exceto os de registro e login.

O token deve ser enviado no header:
```
Authorization: Bearer <token>
```

#### Autenticação

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/v1/register` | Cadastrar novo usuário |
| POST | `/api/v1/authenticate` | Login — retorna o token JWT |

Corpo do registro:
```json
{
  "name": "João",
  "lastname": "Silva",
  "email": "joao@email.com",
  "password": "senha123"
}
```

Corpo do login:
```json
{
  "username": "joao@email.com",
  "password": "senha123"
}
```

#### Hóspedes

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/v1/guests` | Listar hóspedes |
| POST | `/api/v1/guests` | Cadastrar hóspede |

#### Reservas

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/v1/bookings` | Listar todas as reservas |
| GET | `/api/v1/bookings/{id}` | Buscar reserva por ID |
| POST | `/api/v1/bookings` | Criar reserva |
| PUT | `/api/v1/bookings/{id}` | Atualizar reserva |
| PATCH | `/api/v1/bookings/{id}/cancel` | Cancelar reserva |
| PATCH | `/api/v1/bookings/{id}/checkin` | Realizar check-in |
| PATCH | `/api/v1/bookings/{id}/checkout` | Realizar check-out |

Corpo para criar reserva:
```json
{
  "guestId": "uuid-do-hospede",
  "roomCategory": "STANDARD",
  "checkIn": "2026-05-20",
  "checkOut": "2026-05-25"
}
```

Categorias disponíveis: `STANDARD`, `DELUXE`, `SUITE`.

### Documentação interativa (Swagger)

Com o backend rodando, acesse:
```
http://localhost:8080/api/v1/api-docs
```

---

## Frontend

### Configurar e rodar

```bash
cd frontend
cp .env.example .env.local
npm install
npm run dev
```

Acesse em `http://localhost:5173`.

O arquivo `.env.local` define o endereço do backend:
```
VITE_API_URL=http://localhost:8080
```

Para mais detalhes, consulte o [README do frontend](frontend/README.md).

---

## Rodar tudo junto

```bash
# Terminal 1 — backend
mvn spring-boot:run

# Terminal 2 — frontend
cd frontend && npm run dev
```

O banco já sobe com dados mockados. Use as credenciais abaixo para acessar:

```
Email: admin@hotel.com
Senha: admin123
```
