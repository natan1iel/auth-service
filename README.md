# auth-service

[![CI](https://github.com/natan1iel/auth-service/actions/workflows/ci.yml/badge.svg?branch=develop)](https://github.com/natan1iel/auth-service/actions/workflows/ci.yml)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=natan1iel_auth-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=natan1iel_auth-service)

Serviço de autenticação do **Sistema de Reserva de Quadras de Vôlei**.

> A documentação geral do projeto — arquitetura, pipeline, ambientes e execução local — está no repositório [api-gateway](https://github.com/natan1iel/api-gateway).

---

## Responsabilidade

Cadastro de usuários, login e emissão do JWT consumido por todo o sistema. É o único serviço que conhece senhas.

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| POST | `/auth/registrar` | Cria usuário com papel `USUARIO` |
| POST | `/auth/login` | Devolve o token JWT |

## O token

| Claim | Conteúdo |
|---|---|
| `sub` | id do usuário |
| `nome` | nome do usuário |
| `papel` | `USUARIO` ou `ADMIN` |
| `exp` | 2 horas após a emissão |

O claim `nome` existe para que o `reservas-service` grave o nome na tabela de leitura do CQRS sem precisar chamar este serviço.

## Regras

- Senha armazenada com BCrypt, nunca em texto puro
- E-mail duplicado retorna `409`
- E-mail inexistente e senha incorreta retornam a mesma mensagem genérica, para não revelar se a conta existe
- O papel `ADMIN` não pode ser obtido pelo cadastro público

## Ambientes

| Ambiente | Branch | URL |
|---|---|---|
| DEV | `develop` | https://auth-service-dev-3aq6.onrender.com |
| HOMOL | `main` | https://auth-homol-devops.onrender.com |

As chamadas de verdade passam pelo gateway; essas URLs servem para o Swagger e o health check.

## Observabilidade

`/actuator/health` · `/actuator/prometheus` · `/swagger-ui.html` (habilitado por `SWAGGER_ENABLED`)

As métricas também são enviadas ao Grafana Cloud por OTLP, com as tags `application=auth-service` e `ambiente`. Detalhes no [api-gateway](https://github.com/natan1iel/api-gateway#observabilidade).

## Variáveis de ambiente

`PORT` · `DB_URL` · `DB_USER` · `DB_PASSWORD` · `JWT_SECRET` · `SWAGGER_ENABLED` · `AMBIENTE` · `OTLP_ENABLED` · `OTLP_URL` · `OTLP_AUTH`
