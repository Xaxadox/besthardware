# Guia das mudancas de 2026-06 - semana 1

Este guia registra as mudancas feitas no projeto BestHardware na semana 1 de 2026-06. Ele serve como controle de mudancas e tambem como material de estudo para entender a evolucao da API REST.

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

Inicialmente esses endpoints apenas documentavam os escopos oficiais do sistema. Depois, a API tambem passou a gerar recomendacoes automaticas de PC por perfil usando esses mesmos codigos.

## 5. Por que criar DTOs

Depois dos controllers, foi criada uma camada de DTOs. Primeiro ela foi aplicada em `Orcamento` e `ItemOrcamento`; depois foi expandida para os outros controllers.

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

## 14. DTOs nos outros controllers

Depois dos DTOs de orcamento, a mesma ideia foi aplicada nos outros controllers.

Foram criados DTOs de entrada para:

```text
ArmazenamentoRequest
ComponenteRequest
CpuRequest
FonteRequest
GpuRequest
MonitorRequest
PerfilRequest
PlacaMaeRequest
RamRequest
UsuarioRequest
```

Tambem foram criados DTOs de saida para:

```text
ComponenteResponse
PerfilResponse
UsuarioResponse
PerfilUsoResponse
```

O que isso mudou na pratica:

- O JSON enviado para a API ficou separado das entidades JPA.
- Os controllers deixaram de expor diretamente os models em varios endpoints.
- As validacoes ficaram mais claras nos DTOs de request.
- As respostas ficaram mais controladas, principalmente para componentes de hardware.

Exemplo: uma CPU agora e criada usando `CpuRequest`, e a resposta volta como `ComponenteResponse`.

## 15. Mapper de DTOs

Foi criada a classe `DtoMapper`.

Ela centraliza a conversao de models para DTOs de resposta.

Exemplo de responsabilidade:

```text
CpuModel -> ComponenteResponse
GpuModel -> ComponenteResponse
UsuarioModel -> UsuarioResponse
PerfilModel -> PerfilResponse
```

Isso evita repetir a mesma logica de conversao em varios controllers.

Tambem facilita manter um formato unico de resposta para componentes, mesmo tendo varios tipos diferentes:

```text
CPU
GPU
RAM
Fonte
Armazenamento
Monitor
PlacaMae
```

## 16. Recomendacoes automaticas por perfil

Foi criado o controller:

```text
RecomendacaoController
```

Endpoint para listar recomendacoes de todos os perfis:

```text
GET /api/recomendacoes/perfis
```

Endpoint para gerar recomendacao de um perfil especifico:

```text
GET /api/recomendacoes/perfis/{codigoPerfil}
```

Exemplos:

```text
GET /api/recomendacoes/perfis/trabalho
GET /api/recomendacoes/perfis/jogo-inicial
GET /api/recomendacoes/perfis/jogo-intermediario
GET /api/recomendacoes/perfis/jogos-pesados
GET /api/recomendacoes/perfis/profissional
```

O `RecomendacaoService` escolhe componentes conforme o perfil:

- `trabalho`: CPU com video integrado, uso leve, sem GPU dedicada.
- `jogo-inicial`: GPU dedicada de entrada para Full HD.
- `jogo-intermediario`: GPU melhor para 2K.
- `jogos-pesados`: foco em 2K com altos FPS e 4K.
- `profissional`: mais nucleos, mais RAM e GPU forte para apps pesados.

A resposta inclui:

```text
perfil
precoTotal
componentes
compatibilidade
observacoes
```

Assim, a API nao apenas cadastra pecas: ela ja consegue sugerir um projeto de computador coerente com o uso.

## 17. Compatibilidade automatica

Foi criado o `CompatibilidadeService`.

Ele verifica se uma lista de componentes faz sentido para montar um PC.

Endpoint:

```text
POST /api/recomendacoes/compatibilidade
```

Exemplo de request:

```json
{
  "componenteIds": [1, 2, 3, 4, 5]
}
```

Regras implementadas:

- CPU e placa-mae precisam ter o mesmo socket.
- A geracao de RAM precisa combinar com o chipset da placa-mae.
- A fonte precisa ter potencia suficiente para CPU, GPU e margem de seguranca.
- A API avisa quando faltam pecas importantes, como RAM, armazenamento, CPU, placa-mae ou fonte.
- A API avisa quando ha SSD NVME sem placa-mae para validar slot M.2.
- A API avisa quando nao ha GPU dedicada e a CPU nao parece ter video integrado.
- A API considera erro quando ha mais de uma CPU, placa-mae ou fonte no mesmo conjunto.

A resposta separa:

```text
compativel
erros
avisos
componentes
```

Isso e importante porque nem todo problema deve bloquear a montagem. Alguns casos sao erros; outros sao apenas avisos.

## 18. Dados iniciais mais completos

O arquivo `TesteConfig` recebeu mais componentes de exemplo.

Foram adicionados novos dados para:

```text
CPUs
placas-mae
GPUs
RAM
fontes
armazenamentos
monitores
```

Motivo da mudanca:

- As recomendacoes por perfil precisam ter opcoes reais para escolher.
- O H2 em memoria nasce vazio toda vez que a aplicacao reinicia.
- Com mais dados iniciais, fica mais facil testar os endpoints sem cadastrar tudo manualmente.

Exemplos de componentes adicionados:

```text
Ryzen 5 5600G
Ryzen 9 7900
RTX 4060 Ti
RTX 4080 Super
RAM DDR5 32GB
Fonte 850W Gold
SSD NVME 2TB
Monitor 4K 144Hz
```

## 19. Tratamento centralizado de erros

Antes, varios controllers retornavam apenas:

```java
ResponseEntity.notFound().build()
```

Isso gerava um `404` sem mensagem no corpo da resposta.

Agora foi criado um tratamento centralizado com:

```text
ApiExceptionHandler
ErroResponse
RecursoNaoEncontradoException
ConflitoException
ValidacaoNegocioException
```

Com isso, a API retorna erros em JSON padronizado.

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

O handler tambem trata:

- erros de validacao com `@Valid`;
- JSON mal formatado;
- parametro com tipo invalido;
- parametro obrigatorio ausente;
- conflito de banco;
- erro interno inesperado.

Essa mudanca melhora bastante o uso da API, porque o cliente passa a saber por que a requisicao falhou.

## 20. Recomendacoes ja concluidas e pendentes

Da lista de proximas etapas, foram concluidas:

```text
Melhorar DTOs dos outros controllers.
Criar regras reais de compatibilidade automatica.
Gerar recomendacoes de PC por perfil.
Tratar erros com mensagens mais claras.
```

Ainda ficam pendentes:

```text
Criar frontend.
Persistir em banco real, porque H2 em memoria perde os dados ao reiniciar.
```

## 21. Filtros com Specification

Os filtros de listagem dos controllers de hardware, orcamento e item de orcamento foram refatorados para usar `Specification`.

Antes, o controller decidia qual filtro aplicar com varios `if`s:

```java
if (socket != null) {
    return cpuService.buscarPorSocket(socket);
}

if (nucleosMinimos != null) {
    return cpuService.buscarPorNucleosMinimos(nucleosMinimos);
}
```

Esse modelo funciona quando existe apenas um filtro por vez, mas fica limitado quando a API precisa combinar parametros, por exemplo:

```text
GET /api/cpus?socket=AM4&nucleosMinimos=6&precoMaximo=1200
```

Com `Specification`, os filtros viram condicoes dinamicas de consulta. O controller recebe um DTO de filtro:

```java
public List<ComponenteResponse> listar(@ModelAttribute CpuFiltroRequest filtro) {
    return toResponseList(cpuService.buscarComFiltros(filtro));
}
```

O service delega para o repository:

```java
return cpuRepository.findAll(CpuSpecification.comFiltros(filtro));
```

E a classe `CpuSpecification` monta a consulta de acordo com os campos preenchidos.

O que foi criado:

```text
dtos/*FiltroRequest
specifications/*Specification
repositories com JpaSpecificationExecutor
services com buscarComFiltros(...)
controllers usando @ModelAttribute
```

Controllers refatorados:

```text
CpuController
GpuController
RamController
FonteController
ArmazenamentoController
MonitorController
PlacaMaeController
OrcamentoController
ItemOrcamentoController
```

Vantagens:

- Permite combinar varios filtros na mesma requisicao.
- Remove os `if`s de filtro dos controllers.
- Mantem controller focado em HTTP.
- Mantem service focado em coordenar regra e persistencia.
- Centraliza a construcao da consulta na pasta `specifications`.
- Facilita adicionar novos filtros depois sem aumentar a complexidade do controller.

Nao foram alterados nesta etapa:

```text
UsuarioController
PerfilController
RecomendacaoController
```

Isso foi proposital, porque esses controllers nao estavam no escopo da refatoracao escolhida.

## 22. Controllers mais magros

Depois da refatoracao com `Specification`, os controllers de `Orcamento` e `ItemOrcamento` ainda tinham muita logica interna.

Antes, o `OrcamentoController` fazia tarefas como:

```text
buscar usuario
buscar perfil
buscar componentes
montar OrcamentoModel
montar ItemOrcamentoModel
calcular preco total
montar OrcamentoResponse
```

Isso funciona, mas contraria a ideia do controller "magro": o controller deve receber a requisicao, chamar o service e devolver a resposta HTTP.

Depois da mudanca, o controller ficou assim:

```java
@PostMapping
public ResponseEntity<OrcamentoResponse> criar(@Valid @RequestBody OrcamentoRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(orcamentoService.criar(request));
}
```

A regra foi para o service:

```text
OrcamentoService
ItemOrcamentoService
```

O que foi movido para os services:

- criacao de orcamento por IDs de usuario, perfil e componentes;
- atualizacao dos dados principais do orcamento;
- criacao e atualizacao de item de orcamento;
- exclusao de item com validacao para nao remover o ultimo item;
- recalculo do preco total do orcamento;
- montagem dos DTOs de resposta de orcamento e item.

Essa mudanca deixa os controllers mais alinhados com os slides de Controllers e Injecao de Dependencia.

## 23. Swagger/OpenAPI

Foi adicionada documentacao interativa com springdoc OpenAPI.

Dependencia adicionada ao `pom.xml`:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>3.0.3</version>
</dependency>
```

Tambem foi criada a classe:

```text
OpenApiConfig
```

Ela define os metadados principais da API:

```text
titulo
versao
descricao
```

Endpoints de documentacao:

```text
http://localhost:8080/swagger-ui.html
http://localhost:8080/v3/api-docs
```

O Swagger ajuda na apresentacao do projeto porque permite testar os endpoints pelo navegador.

## 24. Testes de integracao

Foi criada a classe:

```text
ApiIntegrationTests
```

Ela sobe a aplicacao em uma porta aleatoria e faz requisicoes HTTP reais usando `HttpClient`.

Cenarios testados:

- documentacao OpenAPI disponivel em `/v3/api-docs`;
- filtro de CPUs por socket e quantidade minima de nucleos;
- criacao de orcamento com itens;
- retorno de erro padronizado quando uma CPU nao existe.

Esses testes verificam o comportamento da API de fora para dentro, mais perto do uso real do sistema.

## 25. Avaliacao sobre SpecificationUtils

A ideia de criar uma `SpecificationUtils` foi avaliada depois das melhorias principais.

Decisao tomada:

```text
Nao implementar agora.
```

Motivo:

- as `Specifications` atuais ainda sao explicitas e boas para aprendizado;
- a utility reduziria repeticao, mas adicionaria mais uma abstracao;
- para a disciplina, e mais importante conseguir explicar `Controller`, `Service`, `Repository`, DTO, validacao e DI;
- a prioridade atual era controller magro, Swagger, testes e documentacao.

Ela continua sendo uma melhoria possivel para uma etapa futura, desde que seja pequena e simples.

## 26. Planilha CSV de precos

Foi criada uma planilha CSV de exemplo:

```text
docs/ofertas_precos_exemplo.csv
```

Ela organiza pesquisas de preco por componente, com campos como:

```text
codigo
perfil_sugerido
categoria
modelo
marca
loja
preco_avista
preco_parcelado
parcelas
cupom
url_produto
fonte
data_coleta
observacoes
```

A planilha usa os componentes de `TesteConfig` como base. Ela ainda nao e importada automaticamente pela API; por enquanto, serve como material de apoio para registrar precos pesquisados manualmente e preparar uma futura funcionalidade de importacao.

Motivo da decisao:

- preco de hardware muda muito rapido;
- o mesmo componente pode aparecer em varias lojas;
- registrar `fonte`, `url_produto` e `data_coleta` deixa a pesquisa mais rastreavel;
- evitar scraping automatico sem autorizacao reduz risco tecnico e juridico.

## 27. Tabela de ofertas de preco

Foi adicionada a entidade:

```text
OfertaPrecoModel
```

Ela representa uma oferta de preco de um componente em uma loja ou fonte especifica.

Campos principais:

```text
id
loja
precoAvista
precoParcelado
parcelas
cupom
urlProduto
fonte
observacoes
dataColeta
componente
```

Cardinalidade no DER:

```text
Componente 1 ---- N OfertaPreco
```

Isso significa que um componente pode ter varias ofertas, mas cada oferta pertence a um unico componente.

Foram criados:

```text
OfertaPrecoModel
OfertaPrecoRepository
OfertaPrecoService
OfertaPrecoController
OfertaPrecoRequest
OfertaPrecoResponse
OfertaPrecoFiltroRequest
OfertaPrecoSpecification
```

Endpoint principal:

```text
/api/ofertas-preco
```

Endpoints especificos:

```text
GET /api/ofertas-preco?componenteId=1
GET /api/ofertas-preco/componentes/1/menor-preco
```

Regra de preco preferencial:

```text
1. Se o componente tiver ofertas, usa a menor precoAvista.
2. Se nao tiver ofertas, usa ComponenteModel.preco como fallback.
3. ItemOrcamentoModel.preco continua congelando o preco usado no momento da criacao do orcamento.
```

Essa regra foi aplicada em:

```text
OrcamentoService
ItemOrcamentoService
RecomendacaoService
```

O `ComponenteModel.preco` foi mantido como preco base, porque remover esse campo agora quebraria filtros, DTOs, recomendacoes, dados iniciais e orcamentos existentes.

Tambem foram adicionados o novo DER em:

```text
docs/DER_oferta.png
docs/DER_oferta.pdf
```

O DER antigo foi mantido para registrar a evolucao do projeto.

## 28. README enxuto e documentacao separada

O `README.md` foi reduzido para funcionar como porta de entrada do projeto.

Antes, ele misturava:

```text
apresentacao do projeto
manual completo da API
explicacao de arquitetura
controle de mudancas
```

Depois, os detalhes foram separados em:

```text
docs/API.md
docs/ARQUITETURA.md
docs/GUIA_MUDANCAS_2026-05-SEMANA-5.md
docs/GUIA_MUDANCAS_2026-06-SEMANA-1.md
```

Motivo:

- deixar o README mais facil de ler no GitHub;
- manter os detalhes tecnicos sem apagar conteudo;
- separar documentacao de uso, arquitetura e historico de mudancas;
- facilitar a apresentacao do projeto para professor ou avaliador.

## 29. Excecoes concentradas nos services

Os controllers foram simplificados para evitar tratamento direto de recurso nao encontrado.

Antes, varios controllers faziam:

```java
return service.buscarPorId(id)
        .map(...)
        .orElseThrow(() -> new RecursoNaoEncontradoException(...));
```

Ou:

```java
if (!service.excluirPorId(id)) {
    throw new RecursoNaoEncontradoException(...);
}
```

Depois, os services passaram a ter metodos obrigatorios, por exemplo:

```java
buscarObrigatorio(id)
atualizarObrigatorio(id, model)
excluirObrigatorio(id)
```

Motivo:

- deixar os controllers focados em receber requisicoes e devolver respostas HTTP;
- concentrar regra de busca obrigatoria na camada de service;
- manter o `ApiExceptionHandler` como unico ponto de montagem da resposta de erro;
- evitar repeticao do mesmo `orElseThrow` em varios controllers.

Essa mudanca foi aplicada nos controllers de componentes, hardware, usuario, perfil e recomendacoes.

## 30. Testes executados

Depois das mudancas, foi executado:

```powershell
.\mvnw.cmd test
```

Resultado:

```text
BUILD SUCCESS
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
```

## 31. Resumo do aprendizado

O que foi praticado nesta semana:

- Criacao de controllers REST com Spring Boot.
- Uso de `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping` e `@DeleteMapping`.
- Criacao de endpoints por recurso.
- Uso de filtros via query params.
- Uso de DTOs de filtro com `@ModelAttribute`.
- Uso de `Specification` e `JpaSpecificationExecutor` para filtros dinamicos.
- Definicao dos escopos de PC por perfil de uso.
- Criacao de DTOs com Java records.
- Diferenca entre model e DTO.
- Entrada de dados por IDs em vez de entidades completas.
- Validacao de DTOs com Bean Validation.
- Calculo do total de um orcamento pelos itens.
- Recalculo do orcamento quando itens sao criados, atualizados ou removidos.
- Refatoracao para deixar controllers mais magros.
- Concentracao de regras de orcamento e item de orcamento nos services.
- Expansao de DTOs para outros controllers.
- Criacao de mapper para evitar repeticao de conversao.
- Criacao de recomendacoes automaticas por perfil de uso.
- Criacao de regras de compatibilidade entre componentes.
- Separacao entre erros e avisos de compatibilidade.
- Tratamento centralizado de erros com `@RestControllerAdvice`.
- Padronizacao de respostas de erro com DTO.
- Concentracao de buscas obrigatorias nos services.
- Reducao de tratamento de excecao dentro dos controllers.
- Documentacao interativa com Swagger/OpenAPI.
- Criacao de testes de integracao com requisicoes HTTP reais.
- Avaliacao tecnica de `SpecificationUtils` sem aplicar complexidade extra.
- Criacao de uma planilha CSV para organizar pesquisas de preco.
- Criacao de uma tabela de ofertas de preco por componente.
- Uso de preco preferencial em orcamentos e recomendacoes.
- Organizacao do README e divisao da documentacao por assunto.
- Uso de commits pequenos como controle de mudancas.
