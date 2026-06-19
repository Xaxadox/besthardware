# Arquitetura

Este documento descreve a organizacao interna do Best Hardware.

## Estrutura Principal

```text
src/main/java/com/omni/besthardware
├── config
├── exception
├── mappers
├── model
├── repository
├── rest
│   ├── controller
│   └── dto
│       ├── request
│       └── response
├── service
└── specifications
```

Responsabilidades:

- `model`: entidades JPA.
- `repository`: acesso ao banco com Spring Data JPA.
- `service`: regras de negocio e coordenacao da camada `repository`. O fluxo CRUD padrao (listar, buscar, salvar, atualizar, excluir) fica concentrado em `AbstractCrudService`; cada service concreto de componente (Cpu, Gpu, Ram, Fonte, Armazenamento, Monitor, PlacaMae) e tambem Usuario estendem essa classe e implementam apenas suas consultas especificas.
- `rest/controller`: endpoints REST.
- `rest/dto/request`: objetos de entrada da API.
- `rest/dto/response`: objetos de saida da API.
- `mappers`: conversao entre entidades e DTOs.
- `exception`: excecoes especificas e tratamento centralizado de erros.
- `specifications`: filtros dinamicos para consultas JPA.

## Entidades

Componentes usam heranca JPA com `ComponenteModel` como classe base.

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
OfertaPrecoModel
```

Relacionamentos principais:

- `PerfilModel` possui varios componentes.
- `ComponenteModel` pode ter componentes compativeis.
- `OrcamentoModel` pertence a um usuario e a um perfil.
- `OrcamentoModel` possui varios itens.
- `ItemOrcamentoModel` referencia um orcamento e um componente.
- `OfertaPrecoModel` referencia um componente.

## DER

DER base:

![Diagrama ER](DER.png)

DER com ofertas de preco:

![Diagrama ER com ofertas](DER_oferta.png)

## Banco De Dados

O projeto usa H2 em memoria:

```properties
spring.datasource.url=jdbc:h2:mem:hardware_db
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Como o banco e em memoria, os dados sao perdidos quando a aplicacao e reiniciada. A classe `TesteConfig` carrega dados iniciais para facilitar testes locais.

## Ofertas De Preco

A tabela `ofertaPreco` registra precos encontrados para componentes em lojas ou fontes diferentes.

Campos principais:

```text
loja
precoAvista
precoParcelado
parcelas
cupom
urlProduto
fonte
observacoes
dataColeta
idComponente
```

Cardinalidade:

```text
Componente 1 ---- N OfertaPreco
```

Regra usada pela API:

- se o componente tiver ofertas cadastradas, orcamento e recomendacao usam a menor `precoAvista`;
- se o componente nao tiver oferta, a API usa `ComponenteModel.preco` como fallback;
- `ItemOrcamentoModel.preco` continua guardando o preco congelado no momento da criacao do orcamento.

## Preco Base E Preco De Oferta

`ComponenteModel.preco` continua existindo como preco base.

`OfertaPrecoModel.precoAvista` representa uma oferta pesquisada em uma loja/fonte especifica.

`ItemOrcamentoModel.preco` representa o preco congelado no momento em que o item entrou no orcamento.

Essa separacao evita que um orcamento antigo mude quando uma oferta nova for cadastrada.

## DTOs

A API usa DTOs para separar o formato HTTP das entidades JPA.

Exemplos:

```text
OrcamentoRequest
OrcamentoResponse
ItemOrcamentoCadastroRequest
ItemOrcamentoResponse
OfertaPrecoRequest
OfertaPrecoResponse
ComponenteResponse
```

Vantagens:

- JSON mais simples.
- Menos acoplamento com entidades JPA.
- Validacao de entrada mais clara.
- Respostas mais controladas.

### Resposta polimorfica de componente

`ComponenteResponse` e uma interface selada (`sealed interface`), nao um record unico. Cada tipo de componente tem seu proprio record de resposta, com somente os campos que fazem sentido para ele:

```text
ComponenteResponse (sealed interface)
├── CpuResponse
├── GpuResponse
├── RamResponse
├── FonteResponse
├── ArmazenamentoResponse
├── MonitorResponse
├── PlacaMaeResponse
└── ComponenteGenericoResponse
```

Os controllers especificos (`CpuController`, `GpuController` etc.) devolvem o record do seu proprio tipo (`CpuResponse`, `GpuResponse`...). O `DtoMapper` tem um metodo de conversao tipado para cada um (`toCpuResponse`, `toGpuResponse`...) e um metodo generico, `toComponenteResponse(ComponenteModel, BigDecimal)`, que despacha para o metodo correto via `switch` com pattern matching.

O metodo generico e usado nos pontos da API que lidam com listas heterogeneas de componentes, onde o tipo concreto so e conhecido em tempo de execucao: `PerfilResponse.componentes`, `CompatibilidadeResponse.componentes`, `RecomendacaoResponse.componentes` e o `ComponenteController` (endpoints da base `/api/componentes`).

## Specifications

Os filtros dinamicos usam `Specification` e `JpaSpecificationExecutor`.

Isso permite combinar filtros na mesma requisicao:

```text
GET /api/cpus?socket=AM4&nucleosMinimos=6&precoMaximo=1200
GET /api/ofertas-preco?loja=KaBuM&precoAvistaMaximo=1000
```

Controllers recebem os parametros como DTO de filtro e delegam ao service.

## Planilha CSV

Existe uma planilha de apoio:

```text
docs/ofertas_precos_exemplo.csv
```

Ela organiza pesquisas de preco com campos como loja, preco a vista, preco parcelado, cupom, URL, fonte e data de coleta.

No estado atual, a API ainda nao importa esse CSV automaticamente.


## Atualizações arquiteturais da semana 3.2

A evolução da arquitetura continuou após a introdução inicial do `AbstractCrudService`.

### Services que utilizam AbstractCrudService

Atualmente utilizam a abstração:

```text
CpuService
GpuService
RamService
FonteService
ArmazenamentoService
MonitorService
PlacaMaeService
ComponenteService
UsuarioService
PerfilService
OrcamentoService
OfertaPrecoService
ItemOrcamentoService
```

`ItemOrcamentoService` mantém sobrescrita de operações específicas para preservar regras de negócio.

### Estratégia de mapeamento

O uso direto do `DtoMapper` foi removido dos controllers e services principais.

A conversão passou a ser centralizada em mappers especializados:

```text
ComponenteMapper
UsuarioMapper
PerfilMapper
ArquivoMapper
```

O `PerfilMapper` é responsável por converter `PerfilModel` em `PerfilResponse`, incluindo a transformação da coleção de componentes utilizando `ComponenteMapper`.
