# API

Este documento concentra os detalhes de uso da API REST do Best Hardware.

## URLs Locais

```text
Aplicacao: http://localhost:8080
Swagger: http://localhost:8080/swagger-ui.html
OpenAPI JSON: http://localhost:8080/v3/api-docs
H2 Console: http://localhost:8080/h2-console
```

Dados do H2:

```text
JDBC URL: jdbc:h2:mem:hardware_db
User: sa
Password: vazio
```

## Autenticacao

A API usa JWT. O login fica publico:

```text
POST /api/auth/login
```

Payload:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Resposta:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "expiraEmSegundos": 3600,
  "usuario": "admin",
  "permissoes": ["ROLE_ADMIN", "ROLE_USER"]
}
```

Use o token nas demais chamadas:

```text
Authorization: Bearer {token}
```

Usuarios de demonstracao:

```text
admin / admin123: consulta, cria, altera e exclui.
usuario / usuario123: consulta e valida compatibilidade.
```

Swagger, OpenAPI JSON, H2 Console e `/api/auth/login` ficam liberados para facilitar a demonstracao.

## Padrao REST

Os recursos principais seguem o padrao:

```text
GET    /api/recurso
GET    /api/recurso/{id}
POST   /api/recurso
PUT    /api/recurso/{id}
DELETE /api/recurso/{id}
```

Recursos:

```text
/api/auth/login
/api/usuarios
/api/perfis
/api/componentes
/api/cpus
/api/gpus
/api/rams
/api/fontes
/api/armazenamentos
/api/monitores
/api/placas-mae
/api/orcamentos
/api/itens-orcamento
/api/ofertas-preco
```

## Endpoints Especificos

```text
GET  /api/usuarios/email/{email}
GET  /api/usuarios/existe-email?email={email}

GET  /api/perfis/nome/{nome}
GET  /api/perfis/escopos
GET  /api/perfis/escopos/{codigo}

GET  /api/recomendacoes/perfis
GET  /api/recomendacoes/perfis/{codigoPerfil}
POST /api/recomendacoes/compatibilidade

GET  /api/ofertas-preco/componentes/{componenteId}/menor-preco
```

## Filtros

Os endpoints de listagem aceitam filtros por query params.
Os parametros numericos de rota e filtros numericos devem ser positivos.

Exemplos:

```text
GET /api/cpus?socket=AM4
GET /api/cpus?nucleosMinimos=6
GET /api/gpus?memoriaMinima=8
GET /api/rams?geracao=DDR5
GET /api/fontes?potenciaMinima=650
GET /api/monitores?resolucao=3840x2160
GET /api/orcamentos?usuarioId=1
GET /api/ofertas-preco?componenteId=1
GET /api/ofertas-preco?loja=KaBuM&precoAvistaMaximo=1000
```

Nos endpoints de hardware, orcamento, itens de orcamento e ofertas de preco, os filtros usam DTOs de filtro com `@ModelAttribute` e `Specification`.

## Criar Orcamento

```text
POST /api/orcamentos
```

```json
{
  "nome": "PC jogo inicial Full HD",
  "usuarioId": 1,
  "perfilId": 1,
  "itens": [
    {
      "componenteId": 1,
      "quantidade": 1
    },
    {
      "componenteId": 3,
      "quantidade": 1
    }
  ]
}
```

O total e calculado pela API:

```text
subtotal = preco unitario * quantidade
preco total = soma dos subtotais
```

O preco unitario usa a menor `precoAvista` disponivel em `ofertaPreco`. Se nao houver oferta, usa `ComponenteModel.preco`.

## Criar Oferta De Preco

```text
POST /api/ofertas-preco
```

```json
{
  "loja": "KaBuM",
  "precoAvista": 829.90,
  "precoParcelado": 879.90,
  "parcelas": 10,
  "cupom": null,
  "urlProduto": "https://exemplo.com/produto",
  "fonte": "Pesquisa manual",
  "observacoes": "Preco a vista sem frete",
  "dataColeta": "2026-06-02",
  "componenteId": 1
}
```

## Recomendacoes

Perfis oficiais:

```text
trabalho
jogo-inicial
jogo-intermediario
jogos-pesados
profissional
```

Listar todas:

```text
GET /api/recomendacoes/perfis
```

Buscar por perfil:

```text
GET /api/recomendacoes/perfis/trabalho
GET /api/recomendacoes/perfis/jogo-inicial
GET /api/recomendacoes/perfis/jogo-intermediario
GET /api/recomendacoes/perfis/jogos-pesados
GET /api/recomendacoes/perfis/profissional
```

A resposta inclui:

```text
perfil
precoTotal
componentes
compatibilidade
observacoes
```

O `precoTotal` usa a menor oferta a vista quando ela existe.

## Compatibilidade

```text
POST /api/recomendacoes/compatibilidade
```

```json
{
  "componenteIds": [1, 2, 3, 4, 5]
}
```

Regras atuais:

- CPU e placa-mae precisam ter o mesmo socket.
- RAM precisa combinar com a geracao inferida pelo chipset da placa-mae.
- Fonte precisa atender ao consumo estimado de CPU, GPU e margem de seguranca.
- A API avisa quando faltam pecas importantes.
- A API avisa quando ha SSD NVME sem placa-mae para validar slot M.2.
- A API avisa quando nao ha GPU dedicada e a CPU nao parece ter video integrado.
- Mais de uma CPU, placa-mae ou fonte no mesmo conjunto e tratado como erro.

## Erros

Os erros sao tratados por `ApiExceptionHandler` e retornam em formato padronizado.

Exemplo:

```json
{
  "timestamp": "2026-06-01T16:00:00",
  "status": 404,
  "erro": "Not Found",
  "mensagem": "CPU nao encontrado para identificador 99.",
  "caminho": "/api/cpus/99",
  "detalhes": []
}
```

Sao tratados casos como:

- recurso nao encontrado;
- conflito de regra ou banco;
- validacao com `@Valid`;
- JSON mal formatado;
- parametro com tipo invalido;
- erro interno inesperado.
