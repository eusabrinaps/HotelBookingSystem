# Backend — HotelSystem

Sistema de gerenciamento de reservas desenvolvido com Spring Boot, seguindo os princípios de Domain-Driven Design (DDD) e Test-Driven Development (TDD).

## Arquitetura

O projeto é dividido em camadas bem definidas:

- **domain** — entidades, value objects e interfaces de repositório. Nenhuma dependência de framework.
- **application** — casos de uso (BookingService, BookingQueryService, BookingUpdateService, BookingCancelService, BookingCheckInOutService).
- **infrastructure** — implementações JPA dos repositórios e entidades de persistência.
- **controller** — endpoints REST.
- **security** — autenticação JWT, filtros e configuração do Spring Security.
- **config** — wiring dos serviços de domínio como beans Spring e configuração de CORS.

## Ciclo de vida de uma reserva

Uma reserva percorre os seguintes estados:

```
PENDING → CHECKED_IN → COMPLETED
    └──→ CANCELLED
```

- Só é possível fazer check-in em reservas PENDING e a partir da data de entrada.
- Reservas CHECKED_IN não podem ser editadas nem canceladas.
- O check-out só é permitido após o check-in.

## Executar os testes

```bash
mvn test
```

## Banco de dados

O banco SQLite (`hotel.db`) é criado automaticamente na raiz do projeto na primeira vez que o backend sobe. O Flyway executa a migration `V1_mock_data.sql` populando o banco com 7 hóspedes e 10 reservas de exemplo (nos status PENDING, CHECKED_IN, COMPLETED e CANCELLED).

Credenciais de acesso:
```
Email: admin@hotel.com
Senha: admin123
```
