# Best Hardware API

API REST em Spring Boot para gerenciar componentes de hardware, perfis de uso e orçamentos de computadores. O projeto permite cadastrar peças, consultar filtros por especificações, montar orçamentos por IDs de componentes, validar compatibilidade e gerar recomendações de PC por perfil de uso.

## Domínio do problema

O Best Hardware trabalha com um catálogo de peças para montagem de computadores. A API organiza componentes como CPU, GPU, RAM, fonte, armazenamento, monitor e placa-mãe, além de usuários, perfis e orçamentos.

O sistema atende estes fluxos principais:

- cadastro e consulta de componentes de hardware;
- associação de componentes a perfis;
- criação de orçamentos com itens e cálculo automático do total;
- validação de compatibilidade entre peças;
- recomendação automática de configurações por perfil de uso.

## Diagrama ER

![Diagrama ER](docs/DER.png)

## Tecnologias

- Java 21
- Spring Boot 4.0.6
- Spring Web
- Spring Data JPA
- Bean Validation
- Swagger/OpenAPI com springdoc
- H2 Database em memória
- Lombok
- Maven Wrapper

## Banco de dados

O projeto usa H2 em memória:

```properties
spring.datasource.url=jdbc:h2:mem:hardware_db
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Console do H2:

```text
http://localhost:8080/h2-console
```

Dados de conexão:

```text
JDBC URL: jdbc:h2:mem:hardware_db
User: sa
Password: vazio
```

Como o banco é em memória, os dados são perdidos quando a aplicação é reiniciada. A classe `TesteConfig` carrega dados iniciais para facilitar testes locais.

## Como executar

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Rodar testes:

```powershell
.\mvnw.cmd test
```

A aplicação sobe por padrão em:

```text
http://localhost:8080
```

Documentação interativa da API:

```text
http://localhost:8080/swagger-ui.html
```

Especificação OpenAPI em JSON:

```text
http://localhost:8080/v3/api-docs
```

## Estrutura principal

```text
src/main/java/com/omni/besthardware
├── config
├── controllers
├── dtos
├── exceptions
├── handlers
├── mappers
├── models
├── repositories
├── specifications
└── services
```

Responsabilidades:

- `models`: entidades JPA.
- `repositories`: consultas com Spring Data JPA.
- `services`: regras de negócio e acesso aos repositories.
- `controllers`: endpoints REST.
- `dtos`: objetos de entrada e saída da API.
- `mappers`: conversão entre models e DTOs.
- `handlers`: tratamento centralizado de erros.
- `exceptions`: exceções específicas da API.
- `specifications`: filtros dinâmicos para consultas JPA com vários parâmetros opcionais.

## Entidades principais

Componentes usam herança JPA com `ComponenteModel` como classe base.

```text
ComponenteModel
├── CpuModel
├── GpuModel
├── RamModel
├── FonteModel
├── ArmazenamentoModel
├── MonitorModel
└── PlacaMaeModel
```

Outras entidades:

```text
UsuarioModel
PerfilModel
OrcamentoModel
ItemOrcamentoModel
```

Relacionamentos principais:

- `PerfilModel` possui vários componentes.
- `ComponenteModel` pode ter componentes compatíveis.
- `OrcamentoModel` pertence a um usuário e a um perfil.
- `OrcamentoModel` possui vários itens.
- `ItemOrcamentoModel` referencia um orçamento e um componente.

## Endpoints principais

Os recursos principais seguem o padrão REST:

```text
GET    /api/recurso
GET    /api/recurso/{id}
POST   /api/recurso
PUT    /api/recurso/{id}
DELETE /api/recurso/{id}
```

Recursos disponíveis:

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

Também existem endpoints específicos:

```text
GET  /api/usuarios/email/{email}
GET  /api/usuarios/existe-email?email={email}

GET  /api/perfis/nome/{nome}
GET  /api/perfis/escopos
GET  /api/perfis/escopos/{codigo}

GET  /api/recomendacoes/perfis
GET  /api/recomendacoes/perfis/{codigoPerfil}
POST /api/recomendacoes/compatibilidade
```

## Filtros de consulta

Os endpoints de listagem aceitam filtros por query params.

Exemplos:

```text
GET /api/cpus?socket=AM4
GET /api/cpus?nucleosMinimos=6
GET /api/gpus?memoriaMinima=8
GET /api/rams?geracao=DDR5
GET /api/fontes?potenciaMinima=650
GET /api/monitores?resolucao=3840x2160
GET /api/orcamentos?usuarioId=1
```

Nos endpoints de hardware, orçamento e itens de orçamento, esses filtros são organizados com DTOs de filtro e `Specification`. Com isso, o controller apenas recebe os parâmetros e delega a busca para o service, enquanto a montagem das condições de consulta fica na pasta `specifications`.

## DTOs

A API usa DTOs para separar o formato HTTP das entidades JPA.

Exemplo de criação de orçamento:

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

O total do orçamento é calculado pela API:

```text
subtotal = preço unitário * quantidade
preço total = soma dos subtotais
```

## Perfis de uso

Os escopos oficiais do sistema são:

```text
trabalho
jogo-inicial
jogo-intermediario
jogos-pesados
profissional
```

Ideia de cada perfil:

- `trabalho`: navegador, office, estudos e tarefas leves; prioriza CPU com vídeo integrado.
- `jogo-inicial`: jogos em Full HD com GPU dedicada de entrada.
- `jogo-intermediario`: jogos em 2K.
- `jogos-pesados`: altos FPS em 2K e jogos em 4K.
- `profissional`: SolidWorks, AutoCAD, renderização e aplicações pesadas.

## Recomendações de PC

O endpoint abaixo gera recomendações para todos os perfis:

```text
GET /api/recomendacoes/perfis
```

Para um perfil específico:

```text
GET /api/recomendacoes/perfis/trabalho
```

A resposta inclui:

```text
perfil
precoTotal
componentes
compatibilidade
observacoes
```

## Compatibilidade

A API valida compatibilidade por lista de IDs de componentes:

```text
POST /api/recomendacoes/compatibilidade
```

Exemplo:

```json
{
  "componenteIds": [1, 2, 3, 4, 5]
}
```

Regras atuais:

- CPU e placa-mãe precisam ter o mesmo socket.
- RAM precisa combinar com a geração inferida pelo chipset da placa-mãe.
- Fonte precisa atender ao consumo estimado de CPU, GPU e margem de segurança.
- A API avisa quando faltam peças importantes.
- A API avisa quando há SSD NVME sem placa-mãe para validar slot M.2.
- A API avisa quando não há GPU dedicada e a CPU não parece ter vídeo integrado.
- Mais de uma CPU, placa-mãe ou fonte no mesmo conjunto é tratado como erro.

Observação: o modelo possui relação de componentes compatíveis, mas a API atual não possui um endpoint específico para cadastrar manualmente essa relação.

## Tratamento de erros

Os erros são tratados por `ApiExceptionHandler` e retornam em formato padronizado.

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

São tratados casos como:

- recurso não encontrado;
- conflito de regra ou banco;
- validação com `@Valid`;
- JSON mal formatado;
- parâmetro com tipo inválido;
- erro interno inesperado.

## Roteiro de demonstração

Este fluxo mostra a ideia principal do projeto usando os dados carregados em memória pela classe `TesteConfig`.

1. Subir a aplicação:

```powershell
.\mvnw.cmd spring-boot:run
```

2. Abrir o Swagger:

```text
http://localhost:8080/swagger-ui.html
```

3. Ver componentes disponíveis:

```text
GET /api/cpus?socket=AM4&nucleosMinimos=6
GET /api/gpus?memoriaMinima=8
GET /api/rams?geracao=DDR4
```

4. Gerar recomendações por perfil:

```text
GET /api/recomendacoes/perfis
GET /api/recomendacoes/perfis/jogo-inicial
```

5. Validar compatibilidade de um conjunto:

```text
POST /api/recomendacoes/compatibilidade
```

```json
{
  "componenteIds": [1, 2, 3, 4, 5, 6]
}
```

6. Criar um orçamento usando IDs:

```text
POST /api/orcamentos
```

```json
{
  "nome": "PC demonstracao",
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

7. Conferir erro padronizado:

```text
GET /api/cpus/999999
```

## Testes

Além do teste de contexto da aplicação, existe uma suíte básica de integração em `ApiIntegrationTests`.

Ela cobre:

- geração da documentação OpenAPI;
- filtro de CPUs por socket e núcleos;
- criação de orçamento com itens;
- resposta padronizada para recurso inexistente.

Para executar:

```powershell
.\mvnw.cmd test
```

## Documentação adicional

Guias de mudanças do projeto:

```text
docs/GUIA_MUDANCAS_2026-05-28.md
docs/GUIA_MUDANCAS_2026-06-01.md
```

## Estado atual

Implementado:

- models JPA;
- repositories com consultas derivadas;
- services;
- controllers REST;
- DTOs;
- filtros dinâmicos com `Specification`;
- Swagger/OpenAPI;
- testes de integração básicos;
- recomendações por perfil;
- validação de compatibilidade;
- tratamento centralizado de erros;
- dados iniciais para testes com H2.

Ainda não implementado:

- frontend;
- banco persistente real no lugar do H2 em memória.
