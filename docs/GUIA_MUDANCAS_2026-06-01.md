# Guia das mudancas de 2026-06-01

Este guia registra as mudancas feitas no projeto BestHardware em 2026-06-01. Ele serve como controle de mudancas e tambem como material de estudo para entender a evolucao da API REST.

## 1. Controllers REST

As classes da pasta `controllers` deixaram de estar vazias e passaram a expor endpoints HTTP.

Antes:

```java
package com.omni.besthardware.controllers;

public class CpuController {
}
```

Depois:

```java
@RestController
@RequestMapping("/api/cpus")
public class CpuController {
}
```

O que mudou:

- `@RestController`: indica que a classe responde requisicoes HTTP e retorna JSON.
- `@RequestMapping`: define o caminho base da API.
- `@GetMapping`: busca dados.
- `@PostMapping`: cria dados.
- `@PutMapping`: atualiza dados.
- `@DeleteMapping`: remove dados.
- `@Valid`: ativa as validacoes definidas nos models ou DTOs.

## 2. Dependencia web

Foi adicionada a dependencia web no `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Essa dependencia e necessaria para usar controllers REST, rotas HTTP, JSON e servidor web embutido.

Sem ela, anotacoes como `@RestController`, `@GetMapping` e `@RequestBody` nao ficam disponiveis corretamente no projeto.

## 3. Endpoints criados

Foram criados endpoints para os recursos principais do sistema:

```text
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
```

Cada controller segue a mesma ideia geral:

```text
GET    /api/recurso
GET    /api/recurso/{id}
POST   /api/recurso
PUT    /api/recurso/{id}
DELETE /api/recurso/{id}
```

Tambem foram incluidos filtros por parametros de URL quando ja existiam metodos equivalentes nos services.

Exemplos:

```text
GET /api/cpus?socket=AM4
GET /api/gpus?memoriaMinima=8
GET /api/rams?geracao=DDR4
GET /api/fontes?potenciaMinima=650
GET /api/monitores?resolucao=1920x1080
GET /api/orcamentos?usuarioId=1
```

## 4. Perfis de uso do projeto

O `PerfilController` recebeu endpoints especificos para representar a ideia central do BestHardware: montar um projeto de computador conforme o uso.

Endpoints:

```text
GET /api/perfis/escopos
GET /api/perfis/escopos/trabalho
GET /api/perfis/escopos/jogo-inicial
GET /api/perfis/escopos/jogo-intermediario
GET /api/perfis/escopos/jogos-pesados
GET /api/perfis/escopos/profissional
```

Perfis definidos:

- `trabalho`: navegador, office, estudos e tarefas leves; prioriza CPU com GPU integrada.
- `jogo-inicial`: jogos em Full HD com GPU dedicada de entrada.
- `jogo-intermediario`: jogos em 2K com boa qualidade grafica.
- `jogos-pesados`: altos FPS em 2K e jogos em 4K.
- `profissional`: SolidWorks, AutoCAD, renderizacao e apps de alto desempenho.

Esses endpoints ainda nao montam automaticamente o PC, mas deixam documentado no backend quais sao os escopos oficiais do sistema.

## 5. Por que criar DTOs

Depois dos controllers, foi criada uma camada de DTOs para `Orcamento` e `ItemOrcamento`.

DTO significa `Data Transfer Object`. Ele define o formato dos dados que entram e saem da API.

Antes, o controller podia receber uma entidade JPA completa no JSON:

```json
{
  "nome": "PC Gamer",
  "usuario": {
    "id": 1
  },
  "perfil": {
    "id": 1
  }
}
```

Isso funciona para estudo, mas nao e o melhor formato para uma API real. O cliente nao deveria precisar montar objetos JPA completos.

Com DTO, a API recebe apenas os IDs necessarios:

```json
{
  "nome": "PC Gamer Full HD",
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

Vantagens:

- JSON mais simples.
- Menos acoplamento entre API e entidades JPA.
- Evita expor relacionamentos internos do banco.
- Facilita validacao de entrada.
- Facilita montar um PC usando apenas IDs de usuario, perfil e componentes.

## 6. DTOs criados

Foi criada a pasta `dtos` com os seguintes records:

```text
ItemOrcamentoRequest
ItemOrcamentoCadastroRequest
ItemOrcamentoResponse
OrcamentoRequest
OrcamentoAtualizacaoRequest
OrcamentoResponse
```

### ItemOrcamentoRequest

Usado dentro de `OrcamentoRequest`, quando um orcamento e criado com uma lista de componentes.

```java
public record ItemOrcamentoRequest(
        Integer componenteId,
        Integer quantidade
) {
}
```

### ItemOrcamentoCadastroRequest

Usado quando um item e criado ou atualizado diretamente pelo endpoint `/api/itens-orcamento`.

```java
public record ItemOrcamentoCadastroRequest(
        Integer orcamentoId,
        Integer componenteId,
        Integer quantidade
) {
}
```

### OrcamentoRequest

Usado para criar um projeto de computador completo.

```java
public record OrcamentoRequest(
        String nome,
        Integer usuarioId,
        Integer perfilId,
        List<ItemOrcamentoRequest> itens
) {
}
```

### OrcamentoAtualizacaoRequest

Usado para atualizar os dados principais do orcamento sem substituir os itens.

```java
public record OrcamentoAtualizacaoRequest(
        String nome,
        Integer usuarioId,
        Integer perfilId
) {
}
```

### Responses

Os DTOs de resposta devolvem dados ja tratados:

```text
OrcamentoResponse
ItemOrcamentoResponse
```

Eles evitam devolver diretamente entidades JPA com relacionamentos internos.

## 7. Validacoes nos DTOs

Os DTOs tambem receberam anotacoes de validacao.

Exemplo:

```java
@NotNull
@Positive
Integer componenteId
```

Principais anotacoes usadas:

- `@NotBlank`: texto obrigatorio e nao vazio.
- `@Size`: tamanho maximo do texto.
- `@NotNull`: campo obrigatorio.
- `@Positive`: numero maior que zero.
- `@NotEmpty`: lista obrigatoria e com pelo menos um item.
- `@Valid`: valida objetos internos, como os itens dentro do orcamento.

No `OrcamentoRequest`, a lista de itens usa:

```java
@NotEmpty
List<@Valid ItemOrcamentoRequest> itens
```

Isso significa que um orcamento precisa ter pelo menos um componente, e cada item da lista tambem sera validado.

## 8. Criacao de orcamento com DTO

Agora o endpoint `POST /api/orcamentos` cria um orcamento usando IDs.

Exemplo de request:

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
    },
    {
      "componenteId": 4,
      "quantidade": 2
    }
  ]
}
```

Fluxo interno:

```text
Controller recebe OrcamentoRequest
        |
Busca Usuario pelo usuarioId
        |
Busca Perfil pelo perfilId
        |
Busca cada Componente pelo componenteId
        |
Cria OrcamentoModel e ItemOrcamentoModel
        |
Calcula o total
        |
Salva no banco
        |
Retorna OrcamentoResponse
```

## 9. Calculo do total do orcamento

O total do orcamento passou a ser calculado pelos itens:

```text
subtotal = precoUnitario * quantidade
precoTotal = soma dos subtotais
```

Exemplo:

```text
CPU: 899.90 x 1 = 899.90
RAM: 449.90 x 2 = 899.80
GPU: 1899.90 x 1 = 1899.90
Total = 3699.60
```

Isso e melhor do que receber o preco total pronto do cliente, porque a aplicacao passa a controlar a regra de negocio.

## 10. Item de orcamento com DTO

Agora o endpoint `POST /api/itens-orcamento` tambem recebe IDs.

Exemplo:

```json
{
  "orcamentoId": 1,
  "componenteId": 7,
  "quantidade": 1
}
```

O controller:

- busca o orcamento pelo `orcamentoId`;
- busca o componente pelo `componenteId`;
- copia o preco atual do componente para o item;
- salva o item;
- recalcula o preco total do orcamento.

## 11. Atualizacao e exclusao de item

Ao atualizar um item:

```text
PUT /api/itens-orcamento/{id}
```

o sistema recalcula o preco do orcamento.

Ao excluir um item:

```text
DELETE /api/itens-orcamento/{id}
```

o sistema tambem recalcula o preco do orcamento.

Existe uma protecao: se o item for o ultimo do orcamento, a API retorna `409 Conflict`. Isso evita deixar um orcamento sem itens, porque o model exige `preco` positivo e um projeto de computador sem nenhum componente nao faz sentido dentro do escopo atual.

## 12. Diferenca entre Model e DTO

Model representa a entidade persistida no banco:

```text
OrcamentoModel
ItemOrcamentoModel
UsuarioModel
ComponenteModel
```

DTO representa o formato da API:

```text
OrcamentoRequest
OrcamentoResponse
ItemOrcamentoCadastroRequest
ItemOrcamentoResponse
```

Regra pratica:

- Use model para banco e JPA.
- Use DTO para entrada e saida da API.
- Use service para regras de negocio.
- Use controller para receber requisicoes e devolver respostas.

## 13. Exemplo de resposta

Um `OrcamentoResponse` retorna algo neste formato:

```json
{
  "id": 1,
  "nome": "PC jogo inicial Full HD",
  "dataCriacao": "2026-06-01T14:30:00",
  "precoTotal": 3699.60,
  "usuarioId": 1,
  "perfilId": 1,
  "itens": [
    {
      "id": 1,
      "orcamentoId": 1,
      "componenteId": 1,
      "quantidade": 1,
      "precoUnitario": 899.90,
      "subtotal": 899.90
    }
  ]
}
```

## 14. Testes executados

Depois das mudancas, foi executado:

```powershell
.\mvnw.cmd test
```

Resultado:

```text
BUILD SUCCESS
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

## 15. Resumo do aprendizado

O que foi praticado hoje:

- Criacao de controllers REST com Spring Boot.
- Uso de `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping` e `@DeleteMapping`.
- Criacao de endpoints por recurso.
- Uso de filtros via `@RequestParam`.
- Definicao dos escopos de PC por perfil de uso.
- Criacao de DTOs com Java records.
- Diferenca entre model e DTO.
- Entrada de dados por IDs em vez de entidades completas.
- Validacao de DTOs com Bean Validation.
- Calculo do total de um orcamento pelos itens.
- Recalculo do orcamento quando itens sao criados, atualizados ou removidos.
- Uso de commits pequenos como controle de mudancas.
