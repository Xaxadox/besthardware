# Guia de Mudancas 2026-06 Semana 4

Este guia registra as mudancas feitas para atender aos requisitos finais da AV2 relacionados a seguranca e validacao de parametros de rota.

## Objetivo

Adicionar autenticacao e autorizacao com Spring Security/JWT e reforcar a validacao de parametros recebidos pelos endpoints REST.

## Itens Previstos

- Adicionar Spring Security ao projeto.
- Criar endpoint de login para emissao de token JWT.
- Proteger endpoints da API com autorizacao por perfil de acesso.
- Manter Swagger/OpenAPI e H2 Console acessiveis para demonstracao.
- Validar parametros de rota e query params com `@Validated`, `@Positive`, `@NotBlank` e anotacoes equivalentes.
- Atualizar a documentacao de uso com exemplo de autenticacao.

## Arquivos Principais

- `pom.xml`: adiciona `spring-boot-starter-security`.
- `application.properties`: define chave e tempo de expiracao do JWT para ambiente local.
- `config/SecurityConfig.java`: configura Spring Security, usuarios em memoria, regras de acesso e filtro JWT.
- `config/OpenApiConfig.java`: registra o esquema Bearer JWT no Swagger/OpenAPI.
- `security/JwtService.java`: gera e valida tokens JWT assinados com HMAC-SHA256.
- `security/JwtAuthenticationFilter.java`: le `Authorization: Bearer ...` e autentica a requisicao.
- `rest/controller/AuthController.java`: expoe `POST /api/auth/login`.
- `rest/dto/request/LoginRequest.java`: payload de login.
- `rest/dto/response/AuthResponse.java`: resposta com token JWT.
- Controllers REST: adicionam `@Validated`, `@Positive`, `@NotBlank` e `@Email` nos parametros de rota e query params.
- DTOs de filtro: adicionam `@Positive` em campos numericos opcionais.
- `ApiExceptionHandler`: trata falhas de autenticacao, acesso negado e validacao de metodo.
- `ApiIntegrationTests`: valida login, bloqueio sem token, acesso autenticado e parametro de rota invalido.

## Credenciais De Demonstracao

```text
admin / admin123
usuario / usuario123
```

O usuario `admin` tem papeis `ROLE_ADMIN` e `ROLE_USER`. Ele pode consultar, criar, alterar e excluir recursos.

O usuario `usuario` tem papel `ROLE_USER`. Ele pode consultar endpoints `GET /api/**` e usar `POST /api/recomendacoes/compatibilidade`.

## Fluxo Para Testar

1. Iniciar a aplicacao.
2. Acessar `http://localhost:8080/swagger-ui.html`.
3. Fazer login em `POST /api/auth/login`.
4. Copiar o token retornado.
5. Clicar em `Authorize` no Swagger e informar o token Bearer.
6. Executar consultas e operacoes protegidas.

## Regras De Acesso

```text
Publico:
/api/auth/**
/v3/api-docs/**
/swagger-ui.html
/swagger-ui/**
/h2-console/**

ROLE_USER ou ROLE_ADMIN:
GET /api/**
POST /api/recomendacoes/compatibilidade

ROLE_ADMIN:
POST /api/**
PUT /api/**
DELETE /api/**
```

## Validacao De Parametros

As controllers passaram a usar `@Validated`, permitindo validar parametros simples das rotas.

Exemplos:

```java
@PathVariable @Positive Integer id
@PathVariable @NotBlank String codigoPerfil
@RequestParam @NotBlank @Email String email
```

Os filtros por query params tambem passaram a validar campos numericos com `@Positive`, evitando valores como IDs, precos, memorias ou potencias negativas.
