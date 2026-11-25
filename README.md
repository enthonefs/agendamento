# Sistema de Agendamento

Backend REST para um site de agendamento de serviços. O fluxo principal é: o cliente escolhe serviço, profissional e horário; depois consegue consultar, cancelar ou reagendar seus compromissos.

## Stack e camadas

- Java 21, Spring Boot 4, Spring Web, Spring Data JPA e Bean Validation.
- MySQL em produção/desenvolvimento e H2 nos testes.
- `controller`: entrada HTTP e transformação para DTOs.
- `business`: casos de uso e regras de agenda.
- `infrastructure/entity`: persistência e estados armazenados.
- `infrastructure/repository`: acesso ao banco.
- `infrastructure/config` e `infrastructure/exceptions`: configuração técnica e erros HTTP.

## Regras de negócio atuais

- Só barbeiros ativos podem receber novos agendamentos.
- Um barbeiro não pode ter dois agendamentos no mesmo horário; profissionais diferentes podem trabalhar no mesmo horário.
- Horários cancelados liberam o slot.
- Apenas agendamentos `AGUARDANDO` podem ser reagendados ou cancelados pelo cliente.
- Transições administrativas permitidas: `AGUARDANDO -> EM_ATENDIMENTO -> FINALIZADO` ou `AGUARDANDO -> CANCELADO`.
- O horário precisa estar no futuro e o telefone do cliente é armazenado apenas com 10 ou 11 dígitos.

## Principais endpoints

| Fluxo | Método | Rota |
|---|---|---|
| Listar agendamentos administrativos | GET | `/agendamento?page=0&size=20&sort=horario,asc` |
| Consultar agenda do dia | GET | `/agendamento?data=2026-11-26` |
| Consultar compromissos do cliente | GET | `/agendamento/cliente/{clienteId}` |
| Criar agendamento | POST | `/agendamento` |
| Reagendar | PUT | `/agendamento/{id}` |
| Cancelar sem apagar histórico | DELETE | `/agendamento/{id}` |
| Alterar status operacional | PATCH | `/agendamento/{id}/status?status=EM_ATENDIMENTO` |
| Listar profissionais disponíveis para escolha | GET | `/barbeiro/ativos` |

O cadastro e a manutenção de clientes, serviços e barbeiros continuam disponíveis nos recursos `/cliente`, `/servico` e `/barbeiro`.

## Executar localmente

Configure as propriedades de banco por variáveis `SPRING_DATASOURCE_*` ou em um profile local. Para subir banco e aplicação com Docker:

```bash
export DB_NAME=agendamento
export DB_USER=agendamento
export DB_PASSWORD=altere-esta-senha
docker compose up --build
```

Para executar apenas os testes:

```bash
./mvnw test
```

`APP_CORS_ALLOWED_ORIGINS` aceita uma lista separada por vírgulas, por exemplo `http://localhost:5173,http://localhost:3000`. A API retorna erros padronizados com `status`, `message`, `path` e `fieldErrors`.

## Próximas evoluções antes de produção

- Adicionar autenticação/autorização e substituir o `clienteId` exposto na URL por uma identidade autenticada.
- Configurar política de cancelamento, duração do serviço, expediente e intervalos de disponibilidade.
- Adicionar notificações de confirmação/lembrete.
