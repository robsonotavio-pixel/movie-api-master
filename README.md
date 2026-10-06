# Movie API

API REST desenvolvida em Java 21 + Spring Boot 3.5.16, criada para o desafio **"Desafio Pessoa Desenvolvedora Java"**. Simula o backend consultado pelo IMDb: administradores gerenciam usuários e filmes, usuários votam nos filmes (nota de 0 a 4).

## 🛠 Stack

- **Java 21** + **Spring Boot 3.5.16**
- **Spring Data JPA** + **Hibernate**
- **MySQL 8** (via Docker)
- **Flyway** (migrations versionadas)
- **Spring Security** + **JWT** (autenticação bearer token)
- **springdoc-openapi** (Swagger / documentação viva)
- **Lombok**
- **JUnit 5 + Mockito** (testes unitários) e teste de integração ponta a ponta

## 📋 Pré-requisitos

- Java 21
- Docker e Docker Compose
- Maven (ou use o `./mvnw` incluso, não precisa instalar nada)

## 🚀 Como rodar

**1. Suba o banco de dados MySQL:**

```bash
docker compose up -d
```

**2. Rode a aplicação:**

```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`. Na primeira inicialização, o Flyway cria automaticamente todas as tabelas e um **administrador padrão** é criado para você conseguir testar a API sem precisar inserir nada manualmente no banco:

```
email: admin@movieapi.com
senha: admin123
```

**3. Rode os testes (opcional):**

```bash
./mvnw test
```
> Os testes de integração usam o banco real, então o MySQL precisa estar rodando (`docker compose up -d`) antes de rodar os testes.

## 📖 Documentação da API (Swagger)

Com a aplicação rodando, acesse:

```
http://localhost:8080/swagger-ui.html
```

Lá estão todos os endpoints documentados, com opção de testar direto no navegador. Para chamar rotas protegidas, faça login em `POST /auth/login`, copie o `token` da resposta e clique em **"Authorize"** no topo da página, colando o token.

## 🔐 Autenticação

O login é feito em `POST /auth/login`, com email e senha. A resposta traz um token JWT que deve ser enviado em toda requisição protegida no header:

```
Authorization: Bearer <token>
```

O sistema distingue automaticamente **Administrador** e **Usuário** no momento do login (cada um tem sua própria tabela) e embute essa informação (role) no token, usada para liberar ou bloquear ações específicas de cada perfil.

## 🗺 Endpoints principais

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| POST | `/auth/login` | Público | Login (admin ou usuário) |
| POST | `/usuarios` | Público | Cadastro de usuário |
| PUT | `/usuarios/{id}` | Autenticado | Edição de usuário |
| DELETE | `/usuarios/{id}` | Autenticado | Exclusão lógica de usuário |
| POST | `/admins` | Admin | Cadastro de administrador |
| PUT | `/admins/{id}` | Admin | Edição de administrador |
| DELETE | `/admins/{id}` | Admin | Exclusão lógica de administrador |
| GET | `/admins/usuarios` | Admin | Lista usuários ativos, paginado e em ordem alfabética |
| POST | `/filmes` | Admin | Cadastro de filme |
| GET | `/filmes` | Público | Listagem de filmes, com filtros (`diretor`, `nome`, `genero`, `ator`), paginação e ordenação (ex: `?sort=totalVotos,desc`) |
| GET | `/filmes/{id}` | Público | Detalhe do filme, incluindo média de votos |
| POST | `/filmes/{id}/votos` | Usuário | Vota no filme (nota de 0 a 4) |

## 🗄 Modelagem do banco

Administrador e Usuário ficam em tabelas separadas (cada um com seu próprio login), Filme se relaciona com Ator em um relacionamento muitos-para-muitos (`filme_ator`), e Voto guarda a nota de cada usuário por filme, com uma constraint única garantindo um voto por usuário por filme. Os scripts de criação estão em `src/main/resources/db/migration`.

## 🏗 Arquitetura

O projeto segue uma organização em camadas:

```
controller  → endpoints REST
service     → regras de negócio
repository  → acesso ao banco (Spring Data JPA)
entity      → mapeamento das tabelas
dto         → objetos de entrada/saída da API (nunca expõe a entidade/senha diretamente)
security    → autenticação JWT e configuração do Spring Security
exception   → tratamento centralizado de erros (respostas HTTP padronizadas)
```

## ✅ Testes

- **Unitários** (`UsuarioServiceTest`, `AuthServiceTest`): testam a lógica de negócio isolada, usando Mockito para simular os repositories.
- **Integração** (`FilmeApiIntegrationTest`): sobe a aplicação inteira e testa o fluxo completo via HTTP — login, cadastro de filme, cadastro de usuário, votação e verificação de permissões.

## 📝 Observações

- Senhas são armazenadas com hash BCrypt, nunca em texto puro.
- O segredo do JWT em `application.yaml` é um placeholder para ambiente de desenvolvimento — em produção deveria vir de variável de ambiente.
- O admin padrão criado automaticamente é apenas para facilitar a avaliação deste desafio.
