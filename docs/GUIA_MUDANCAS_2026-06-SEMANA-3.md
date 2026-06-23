# Guia das mudancas de 2026-06 - semana 3.2

Este guia registra uma refatoracao de arquitetura no projeto BestHardware, focada em dois pontos: duplicacao de codigo CRUD nos services e o DTO de resposta de componente que tinha campos demais.

## 1. Duplicacao de CRUD nos services

Antes, `ComponenteService`, `CpuService`, `GpuService`, `RamService`, `FonteService`, `ArmazenamentoService`, `MonitorService`, `PlacaMaeService` e `UsuarioService` repetiam o mesmo bloco de codigo:

```java
public List<T> listarTodos() { ... }
public Optional<T> buscarPorId(Integer id) { ... }
public T buscarObrigatorio(Integer id) { ... }
public T salvar(T entidade) { ... }
public Optional<T> atualizar(Integer id, T entidade) { ... }
public T atualizarObrigatorio(Integer id, T entidade) { ... }
public boolean excluirPorId(Integer id) { ... }
public void excluirObrigatorio(Integer id) { ... }
```

Isso dava cerca de 30 linhas identicas em 9 classes diferentes. Qualquer ajuste nesse fluxo (por exemplo, mudar a mensagem de erro padrao) precisava ser repetido em todas elas.

### AbstractCrudService

Foi criada a classe `service/AbstractCrudService<T, ID>`. Ela concentra o fluxo CRUD padrao e usa o repositorio (`JpaRepository`), o nome do recurso (para mensagens de erro) e uma referencia ao setter de id da entidade:

```java
public abstract class AbstractCrudService<T, ID> {
    protected AbstractCrudService(JpaRepository<T, ID> repository, String nomeRecurso, BiConsumer<T, ID> idSetter) { ... }

    public List<T> listarTodos() { ... }
    public Optional<T> buscarPorId(ID id) { ... }
    public T buscarObrigatorio(ID id) { ... }
    public T salvar(T entidade) { ... }
    public Optional<T> atualizar(ID id, T entidade) { ... }
    public T atualizarObrigatorio(ID id, T entidade) { ... }
    public boolean excluirPorId(ID id) { ... }
    public void excluirObrigatorio(ID id) { ... }
}
```

Cada service concreto passou a estender essa classe e ficou responsavel apenas pelas suas consultas especificas:

```java
@Service
public class CpuService extends AbstractCrudService<CpuModel, Integer> {

    private final CpuRepository cpuRepository;

    public CpuService(CpuRepository cpuRepository) {
        super(cpuRepository, "CPU", CpuModel::setId);
        this.cpuRepository = cpuRepository;
    }

    public List<CpuModel> buscarComFiltros(CpuFiltroRequest filtro) { ... }
    public List<CpuModel> buscarPorSocket(String socket) { ... }
    // ... demais consultas especificas de CPU
}
```

O service mantem seu repositorio tipado (`CpuRepository`) para usar metodos especificos como `findBySocketIgnoreCase` e `findAll(Specification)`, que nao fazem parte do contrato generico.

### Resultado

```text
CpuService:            129 -> ~70 linhas
GpuService:             118 -> ~55 linhas
RamService:             118 -> ~55 linhas
FonteService:           113 -> ~50 linhas
ArmazenamentoService:   123 -> ~60 linhas
MonitorService:         123 -> ~60 linhas
PlacaMaeService:        118 -> ~55 linhas
ComponenteService:      108 -> ~40 linhas
UsuarioService:         101 -> ~40 linhas
```

`OrcamentoService`, `ItemOrcamentoService` e `OfertaPrecoService` nao foram alterados porque tem regra de negocio propria (calculo de total, preco preferencial, congelamento de preco) que vai alem do CRUD generico.

## 2. DTO de resposta de componente

Antes, todos os controllers de componente devolviam o mesmo record `ComponenteResponse`, com cerca de 20 campos:

```java
public record ComponenteResponse(
        Integer id, String categoria, String tipo, BigDecimal preco,
        String modelo, String marca, String socket, Integer frequencia,
        Integer consumo, LocalDate anoLancamento, Integer nucleos, Integer memoria,
        String geracao, String certificacao, Integer potencia, String tecnologia,
        String padrao, Integer velocidadeEscrita, Integer velocidadeLeitura,
        String tamanho, String resolucao, String chipset, String formato
) {}
```

Como cada tipo de componente usa poucos desses campos, a maior parte vinha `null` em todo retorno (uma CPU nao tem `marca`, uma GPU nao tem `anoLancamento`, e assim por diante). O `DtoMapper` tinha um bloco `if (componente instanceof XModel x)` para cada tipo, sempre preenchendo os campos que nao se aplicavam com `null`.

### ComponenteResponse como interface selada

`ComponenteResponse` deixou de ser um record e passou a ser uma interface selada (`sealed interface`), com um record proprio por tipo de componente:

```java
public sealed interface ComponenteResponse
        permits CpuResponse, GpuResponse, RamResponse, FonteResponse,
        ArmazenamentoResponse, MonitorResponse, PlacaMaeResponse, ComponenteGenericoResponse {

    Integer id();
    String categoria();
    String tipo();
    BigDecimal preco();
}

public record CpuResponse(
        Integer id, String categoria, String tipo, BigDecimal preco,
        String modelo, String socket, Integer frequencia, Integer consumo,
        LocalDate anoLancamento, Integer nucleos
) implements ComponenteResponse {}
```

Cada record tem somente os campos que fazem sentido para o seu tipo (`GpuResponse` nao tem `socket`, `MonitorResponse` nao tem `nucleos` etc.). `ComponenteGenericoResponse` cobre o caso de um `ComponenteModel` sem subtipo conhecido (usado pelo `/api/componentes`).

### ComponenteMapper com metodo tipado por componente

O `ComponenteMapper` passou a concentrar um metodo de conversao dedicado para cada tipo (`toCpuResponse`, `toGpuResponse` etc.). Os controllers especificos (`CpuController`, `GpuController`...) usam o metodo do seu proprio tipo e devolvem o record especifico, em vez da interface generica:

```java
@GetMapping("/{id}")
public ResponseEntity<CpuResponse> buscarPorId(@PathVariable Integer id) {
    return ResponseEntity.ok(componenteMapper.toCpuResponse(cpuService.buscarObrigatorio(id)));
}
```

O metodo generico `ComponenteMapper.toComponenteResponse(ComponenteModel, BigDecimal)` tambem existe, mas apenas delega para os metodos tipados via `switch` com pattern matching (Java 21):

```java
public ComponenteResponse toComponenteResponse(ComponenteModel componente, BigDecimal preco) {
    return switch (componente) {
        case CpuModel cpu -> toCpuResponse(cpu, preco);
        case GpuModel gpu -> toGpuResponse(gpu, preco);
        // ...
        default -> new ComponenteGenericoResponse(componente.getId(), "Componente", componente.getTipo(), preco);
    };
}
```

Esse metodo generico continua sendo usado nos pontos da API que lidam com listas heterogeneas de componentes, onde o tipo concreto so e conhecido em tempo de execucao:

```text
PerfilResponse.componentes
CompatibilidadeResponse.componentes
RecomendacaoResponse.componentes
ComponenteController (endpoints da base /api/componentes)
```

### O que isso muda no JSON

Antes, uma CPU vinha assim (varios campos `null` omitidos pelo `@JsonInclude(NON_NULL)`, mas ainda fora do lugar conceitualmente):

```json
{
  "id": 1,
  "categoria": "CPU",
  "tipo": "processador",
  "preco": 899.90,
  "modelo": "Ryzen 5 5600G",
  "socket": "AM4",
  "frequencia": 3900,
  "consumo": 65,
  "anoLancamento": "2021-04-01",
  "nucleos": 6
}
```

O JSON de uma CPU buscada em `/api/cpus/{id}` continua igual a esse exemplo, porque os campos que sobravam ja eram omitidos. A diferenca fica no tipo Java: agora o controller de CPU expoe `CpuResponse`, e nao um record gigante compartilhado por sete tipos diferentes. Isso aparece no Swagger/OpenAPI: cada endpoint de componente passa a documentar exatamente os campos que ele devolve, em vez de um schema unico com quase tudo opcional.

## 3. Testes executados

Os testes de integracao existentes (`ApiIntegrationTests`) nao dependem de nenhum campo removido do DTO antigo (eles checam `socket`, `nucleos`, `precoTotal` etc., que continuam presentes nos records especificos). Nao foi necessario alterar nenhum teste.

## 4. Resumo do aprendizado

- Identificar duplicacao de codigo entre classes "irmas" (varios services com a mesma estrutura) e extrair uma classe base generica.
- Usar `BiConsumer` para repassar comportamento especifico (o setter de id) para uma classe base sem acoplar a um tipo concreto.
- Reconhecer um DTO "deus" (muitos campos opcionais, a maioria nula) como sinal de falta de polimorfismo no modelo de resposta.
- Modelar uma interface selada (`sealed interface`) em Java 21 para representar um conjunto fechado de variantes.
- Usar `switch` com pattern matching sobre tipos para substituir uma cadeia de `instanceof`.
- Avaliar onde vale a pena manter um tipo polimorfico generico (listas heterogeneas) e onde vale a pena expor o tipo concreto (endpoints especificos por componente).


## 5. Ajustes adicionais realizados na mesma semana

Após a primeira refatoração, a padronização foi expandida para outros pontos do sistema.

### Remoção do DtoMapper dos pontos de uso

Foi concluída a migração para mappers especializados, removendo o uso direto do `DtoMapper` nos seguintes componentes:

- CpuController
- GpuController
- RamController
- FonteController
- ArmazenamentoController
- MonitorController
- PlacaMaeController
- ComponenteController
- UsuarioController
- PerfilController
- CompatibilidadeService
- RecomendacaoService

Os mappers especializados passaram a ser injetados onde necessário:

```text
ComponenteMapper
UsuarioMapper
PerfilMapper
```

### Correção do PerfilMapper

O `PerfilMapper` foi revisado porque continha estrutura derivada do `UsuarioMapper`.

Agora realiza corretamente o mapeamento:

```text
PerfilModel -> PerfilResponse
```

Incluindo a conversão da lista de componentes através do `ComponenteMapper`.

### Expansão do AbstractCrudService

A abstração baseada em `AbstractCrudService` também passou a ser utilizada por:

```text
PerfilService
OrcamentoService
OfertaPrecoService
ItemOrcamentoService
```

#### PerfilService

Passou a herdar:

- buscarObrigatorio
- excluirObrigatorio
- atualizar
- atualizarObrigatorio

Mantendo apenas a busca específica:

```java
buscarPorNomeExatoObrigatorio(...)
```

#### OrcamentoService

Os `orElseThrow(...)` utilizados para validar usuário e perfil foram substituídos por:

```java
usuarioService.buscarObrigatorio(...)
perfilService.buscarObrigatorio(...)
```

#### OfertaPrecoService

Foi removido o método auxiliar de validação de componente existente.

O fluxo passou a utilizar:

```java
buscarObrigatorio(id)
```

fornecido pela classe base.

#### ItemOrcamentoService

Passou a herdar o CRUD padrão, mas continua sobrescrevendo:

```java
@Override
excluirObrigatorio(...)
```

para preservar a regra de negócio que impede a remoção do único item de um orçamento.
