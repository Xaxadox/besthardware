# Guia das mudancas de 2026-05 - semana 5

Este guia resume as mudancas feitas na semana 5 de 2026-05 no projeto BestHardware e explica o motivo tecnico de cada uma. A ideia e servir como material de estudo para entender a evolucao das camadas `model`, `repository`, `service` e `rest/controller`.

## 1. Padronizacao dos nomes das classes

As classes Java foram ajustadas para seguir a convencao da linguagem: nomes de classes usam `PascalCase`, ou seja, a primeira letra de cada palavra fica maiuscula.

Exemplos:

```java
public class CpuModel {
}

public interface CpuRepository {
}

public class CpuService {
}
```

Por que isso importa:

- Facilita a leitura por outros desenvolvedores Java.
- Mantem o projeto alinhado com IDEs, frameworks e padroes do ecossistema.
- Evita confusao entre nomes de classes, variaveis e metodos.

Convencao usada:

- Classe: `CpuModel`, `UsuarioModel`, `PlacaMaeModel`
- Interface: `CpuRepository`, `UsuarioRepository`
- Metodo/variavel: `findByTipoIgnoreCase`, `cpuRepository`
- Package: `com.omni.besthardware.model`

## 2. Desenvolvimento da camada repository

As interfaces Repository deixaram de ser apenas interfaces genericas e passaram a ter metodos de consulta especificos para cada entidade.

Exemplo em um repository:

```java
List<CpuModel> findByTipoIgnoreCase(String tipo);

List<CpuModel> findByPrecoBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);

List<CpuModel> findByModeloContainingIgnoreCase(String modelo);
```

No Spring Data JPA, a parte depois de `findBy` descreve o filtro da consulta.

Exemplos:

- `findByTipoIgnoreCase`: busca pelo campo `tipo`, ignorando maiusculas e minusculas.
- `findByPrecoBetween`: busca registros com `preco` entre dois valores.
- `findByModeloContainingIgnoreCase`: busca onde `modelo` contem um texto, ignorando maiusculas e minusculas.
- `findByFrequenciaGreaterThanEqual`: busca onde `frequencia` e maior ou igual ao valor informado.
- `findByConsumoLessThanEqual`: busca onde `consumo` e menor ou igual ao valor informado.

O Spring interpreta o nome do metodo e monta a query automaticamente, desde que os nomes dos campos existam no model.

## 3. Criacao da camada de services

Foi criada uma classe de service para cada entidade principal do dominio.

Exemplos:

- `CpuService`
- `GpuService`
- `UsuarioService`
- `OrcamentoService`
- `ComponenteService`

O service fica entre o controller e o repository:

```text
Controller -> Service -> Repository -> Banco de dados
```

Responsabilidade de cada camada:

- Controller: recebe requisicoes HTTP e devolve respostas.
- Service: concentra regras de negocio e organiza operacoes.
- Repository: acessa o banco de dados.
- Model: representa as entidades e seus relacionamentos.

Mesmo que alguns services ainda tenham apenas chamadas simples para o repository, eles ja deixam o projeto preparado para regras futuras, como:

- Validar compatibilidade entre componentes.
- Calcular preco total de um orcamento.
- Impedir exclusao de registros em uso.
- Aplicar filtros de busca reutilizaveis.

## 4. Criacao das classes de controllers vazias

Foi criada a pasta `controllers` com uma classe para cada entidade, mas sem implementar endpoints ainda.

Exemplos:

```java
public class CpuController {
}
```

Essas classes foram deixadas vazias de proposito, como os repositories estavam antes. A proxima evolucao natural sera adicionar anotacoes como:

```java
@RestController
@RequestMapping("/cpus")
public class CpuController {
}
```

Depois disso, os controllers poderao expor endpoints como:

- `GET /cpus`
- `GET /cpus/{id}`
- `POST /cpus`
- `PUT /cpus/{id}`
- `DELETE /cpus/{id}`

## 5. Validacoes de texto nos models

Foram adicionadas anotacoes de validacao nos campos `String` obrigatorios.

Exemplo:

```java
@NotBlank
@Size(max = 128)
@Column(name = "modelo", nullable = false, length = 128)
private String modelo;
```

Papel de cada anotacao:

- `@NotBlank`: impede `null`, texto vazio e texto so com espacos.
- `@Size(max = 128)`: valida o tamanho maximo aceito pela aplicacao.
- `@Column(nullable = false, length = 128)`: configura a coluna no banco de dados.

Essas anotacoes se complementam. `@Column` cuida do banco; `@NotBlank` e `@Size` cuidam da validacao na aplicacao.

Por que manter os dois:

- A aplicacao consegue barrar dados invalidos antes de tentar salvar no banco.
- O banco continua protegido caso algum dado seja inserido por outro caminho.
- A regra fica clara no codigo Java e no schema gerado.

## 6. Validacoes numericas nos models

Depois das validacoes de texto, os campos numericos obrigatorios tambem receberam validacoes.

Exemplo:

```java
@NotNull
@Positive
@Column(name = "frequencia", nullable = false)
private Integer frequencia;
```

Papel de cada anotacao:

- `@NotNull`: impede valor `null`.
- `@Positive`: exige numero maior que zero.
- `@Column(nullable = false)`: exige valor no banco de dados.

Campos como `frequencia`, `consumo`, `memoria`, `potencia`, `quantidade` e `preco` foram ajustados quando representavam valores obrigatorios e naturalmente positivos.

Os campos `id` nao receberam `@NotNull` nem `@Positive`, porque sao gerados automaticamente pelo banco:

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;
```

Antes de salvar um novo objeto, o `id` ainda pode ser `null`. Quem define esse valor e o banco.

## 7. Dependencia de validacao

Para usar anotacoes como `@NotBlank`, `@Size`, `@NotNull` e `@Positive`, o projeto precisa da dependencia de validacao do Spring Boot.

Ela foi adicionada no `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

Essa dependencia habilita a Bean Validation no projeto.

## 8. Sobre o `application.properties`

Durante os testes, foi explicado que:

```properties
spring.jpa.show-sql=true
```

faz o Hibernate mostrar os SQLs no console.

Para um console mais limpo, pode usar:

```properties
spring.jpa.show-sql=false
```

Tambem foi explicado o aviso do H2:

```text
Database is already closed
```

Esse aviso acontece no encerramento da aplicacao com banco H2 em memoria. Uma forma comum de evitar isso e adicionar `DB_CLOSE_ON_EXIT=FALSE` na URL do banco.

No final, o arquivo `application.properties` nao ficou com alteracao de conteudo. O Git apenas marcou uma diferenca temporaria de indice/metadados, que foi limpa sem commit.

## 9. Sobre linhas do GitHub como `@@ -48,3 +52,4 @@`

No GitHub, linhas assim aparecem em diffs:

```diff
@@ -48,3 +52,4 @@ public class ComponenteModel {
```

Isso nao faz parte do codigo. E apenas um marcador do Git indicando onde a alteracao aconteceu.

Leitura:

- `-48,3`: no arquivo antigo, o trecho comecava na linha 48 e tinha 3 linhas.
- `+52,4`: no arquivo novo, o trecho comeca na linha 52 e tem 4 linhas.
- `public class ComponenteModel {`: contexto para mostrar em qual parte do arquivo esta a mudanca.

## 10. Ordem dos commits feitos nesta semana

Commits criados em 2026-05-28:

```text
8b84889 Padroniza nomes Java e adiciona consultas nos repositorios
25af0c5 Adiciona services para entidades do dominio
e67c5e9 Cria classes de controllers vazias
29aa6a2 Adiciona validacoes nos models
8a8ceed Adiciona validacoes numericas nos models
```

Cada commit representa uma etapa da arquitetura:

```text
Models e nomes corretos
        |
Repositories com consultas
        |
Services com regras e acesso organizado
        |
Controllers preparados
        |
Validacoes nos dados
```

## 11. Proximas etapas recomendadas

A proxima parte natural do projeto e implementar os controllers.

Ordem sugerida:

1. Adicionar `@RestController` e `@RequestMapping`.
2. Injetar os services nos controllers.
3. Criar endpoints CRUD basicos.
4. Usar `@Valid` nos endpoints que recebem body.
5. Testar as rotas com Postman, Insomnia ou Swagger.
6. Criar DTOs se os models comecarem a expor dados demais.

Exemplo futuro:

```java
@PostMapping
public CpuModel criar(@Valid @RequestBody CpuModel cpu) {
    return cpuService.salvar(cpu);
}
```

O `@Valid` e importante porque aciona as anotacoes colocadas nos models, como `@NotBlank`, `@Size`, `@NotNull` e `@Positive`.

## 12. Resumo do aprendizado

O que foi praticado nesta semana:

- Convencao de nomes Java com `PascalCase`.
- Queries automaticas do Spring Data JPA pelo nome dos metodos.
- Separacao de responsabilidades em camadas.
- Criacao de services para preparar regras de negocio.
- Preparacao dos controllers para endpoints REST.
- Validacao de textos com `@NotBlank` e `@Size`.
- Validacao de numeros com `@NotNull` e `@Positive`.
- Diferenca entre validacao da aplicacao e restricao do banco.
- Leitura basica de diffs do GitHub.
- Uso de commits pequenos e organizados.
