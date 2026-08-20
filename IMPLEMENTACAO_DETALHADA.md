# 📚 Documentação Detalhada de Implementação
## Repositório: quarkus-overview-chapter-general

---

## 1. **Visão Geral da Implementação**

Este documento descreve a implementação específica do projeto, focando na **API de Transações Itaú** que demonstra um modelo completo de arquitetura hexagonal (ports & adapters) em uma aplicação Quarkus.

### Domínio Implementado: **Transações Financeiras (Itaú)**

A aplicação gerencia transações financeiras com requisitos de:
- ✅ Criptografia de dados sensíveis (CPF, token de cartão)
- ✅ Persistência em banco de dados (H2/MySQL)
- ✅ Armazenamento em memória (cache)
- ✅ Estatísticas e relatórios de transações
- ✅ Tratamento robusto de exceções
- ✅ Logging estruturado

---

## 2. **Arquitetura Hexagonal (Ports & Adapters)**

```
┌─────────────────────────────────────────────────────────────────┐
│                     ADAPTER IN (HTTP REST)                       │
│  TransactionItauController | StatiticsItauController             │
│  (@Path, @GET, @POST, @DELETE)                                  │
└────────────────┬──────────────────────────────────────────────────┘
                 │
┌────────────────▼──────────────────────────────────────────────────┐
│                      USE CASES (Negócio)                          │
│  TransactionItauCreateUseCase        (CQRS Write)                 │
│  TransactionItauGetsUseCase          (CQRS Read)                  │
│  TransactionItauRemoveUseCase        (CQRS Delete)                │
│  StatiticsTransactionItauUseCase     (Analytics)                  │
└────────────────┬──────────────────────────────────────────────────┘
                 │
┌────────────────▼──────────────────────────────────────────────────┐
│                      DOMAIN (Núcleo)                              │
│  TransactionItau (Objeto de Domínio)                             │
│  Input/Output (DTOs)                                             │
└────────────────┬──────────────────────────────────────────────────┘
                 │
┌────────────────▼──────────────────────────────────────────────────┐
│                   PORTS (Interfaces)                              │
│  TransactionItauPort       (Interface de Persistência)            │
│  TransactionItauMemoryPort (Interface de Cache)                   │
└────────────────┬──────────────────────────────────────────────────┘
                 │
      ┌──────────┴──────────┐
      │                     │
┌─────▼──────────┐  ┌──────▼──────────────┐
│ ADAPTER OUT    │  │ ADAPTER OUT        │
│ (Database)     │  │ (Memory)           │
└─────┬──────────┘  └──────┬──────────────┘
      │                     │
┌─────▼──────────┐  ┌──────▼──────────────┐
│TransactionItau │  │TransactionItauMemory│
│Adapter         │  │Adapter              │
│(H2/MySQL)      │  │(Cache)              │
└────────────────┘  └─────────────────────┘
```

---

## 3. **Entidades e Domínio**

### 3.1 Entidade JPA: `TransactionItauEntity`

**Localização:** `adapter/out/entities/TransactionItauEntity.java`

```java
@Entity
@Table(name = "tb_itau_transaction")
public class TransactionItauEntity extends PanacheEntityBase implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "id_transacao")
    private String transactionId;              // UUID único da transação
    
    @Column(name = "num_documento_usuario")
    private String encryptedUserDocument;      // CPF (criptografado)
    
    @Column(name = "token_cartao_credito")
    private String encryptedCreditCardToken;   // Token do cartão (criptografado)
    
    @Column(name = "valor_transacao")
    private Long transactionValue;             // Valor em centavos
    
    @Column(name = "quantia")
    private BigDecimal amount;                 // BigDecimal para precisão
    
    @Column(name = "dt_criacao")
    private LocalDateTime createdAt;           // Timestamp da criação
    
    @Transient
    private String rawUserDocument;            // CPF em texto plano (não persiste)
    
    @Transient
    private String rawCreditCardToken;         // Token em texto plano (não persiste)
    
    @PrePersist
    public void prePersist() {
        // Criptografa dados sensíveis ANTES de persistir
        this.encryptedUserDocument = EncryptoManagerConfig.encrypt(rawUserDocument);
        this.encryptedCreditCardToken = EncryptoManagerConfig.encrypt(rawCreditCardToken);
    }
    
    @PostLoad
    public void postLoad() {
        // Descriptografa dados APÓS carregar do banco
        this.rawUserDocument = EncryptoManagerConfig.decrypt(encryptedUserDocument);
        this.rawCreditCardToken = EncryptoManagerConfig.decrypt(encryptedCreditCardToken);
    }
}
```

**Características:**
- Herda de `PanacheEntityBase` para uso de Panache (abstração sobre JPA)
- Suporta `@PrePersist` e `@PostLoad` para criptografia automática
- Usa `@Transient` para campos que não são persistidos
- BigDecimal para precisão monetária

### 3.2 Objeto de Domínio: `TransactionItau`

**Localização:** `core/domain/TransactionItau.java`

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionItau implements Serializable {
    
    private Long id;
    private String transactionId;              // UUID da transação
    private BigDecimal amount;
    private String encryptedUserDocument;
    private String encryptedCreditCardToken;
    private Long transactionValue;
    private LocalDateTime createdAt;
    private String rawUserDocument;            // Dados descriptografados
    private String rawCreditCardToken;
    
    // Construtor que valida e criptografa dados ao criar do Input
    public TransactionItau(TransactionItauInput input) {
        try {
            BeanUtils.copyProperties(this, input);
            this.createdAt = input.createdAt().toLocalDateTime();
            this.rawUserDocument = input.documentNumber();
            this.rawCreditCardToken = input.creditCardToken();
            
            // Criptografa dados sensíveis
            if (StringUtils.isNotEmpty(this.rawUserDocument)) {
                this.encryptedUserDocument = EncryptoManagerConfig.encrypt(this.rawUserDocument);
            }
            
            if (StringUtils.isNotEmpty(this.rawCreditCardToken)) {
                this.encryptedCreditCardToken = EncryptoManagerConfig.encrypt(this.rawCreditCardToken);
            }
            
            // Garante que amount e transactionValue estão sincronizados
            if (Objects.isNull(this.amount)) {
                this.amount = input.amount();
                this.transactionValue = this.amount.longValue();
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ParseEntityFailedException(e);
        }
    }
    
    // Construtor que converte Entity JPA para Domain
    public TransactionItau(TransactionItauEntity entity) {
        try {
            BeanUtils.copyProperties(this, entity);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ParseEntityFailedException(e);
        }
    }
}
```

**Responsabilidades:**
- Representação do domínio (regras de negócio)
- Conversão de Input e Entity
- Criptografia de dados sensíveis
- Validação de invariantes

### 3.3 DTO de Entrada: `TransactionItauInput`

**Localização:** `core/usecase/input/TransactionItauInput.java`

```java
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record TransactionItauInput(
    @NotNull 
    BigDecimal amount,
    
    @NotNull 
    OffsetDateTime createdAt,
    
    @NotBlank(message = "O numero documento é obrigatorio") 
    String documentNumber,
    
    @NotBlank(message = "O valor referente ao token do cartao de credito é obrigatorio") 
    String creditCardToken
) {
}
```

**Exemplo de Requisição JSON:**
```json
{
  "amount": 150.50,
  "createdAt": "2026-08-20T19:49:56Z",
  "documentNumber": "12345678900",
  "creditCardToken": "4111111111111111"
}
```

### 3.4 DTO de Saída: `TransactionItauOutput`

```java
public record TransactionItauOutput(
    Long id,
    String transactionId,
    BigDecimal amount,
    String encryptedUserDocument,
    String encryptedCreditCardToken,
    LocalDateTime createdAt
) {
    public static TransactionItauOutput from(TransactionItau domain) {
        return new TransactionItauOutput(
            domain.getId(),
            domain.getTransactionId(),
            domain.getAmount(),
            domain.getEncryptedUserDocument(),
            domain.getEncryptedCreditCardToken(),
            domain.getCreatedAt()
        );
    }
}
```

**Exemplo de Resposta JSON:**
```json
{
  "id": 1,
  "transactionId": "550e8400-e29b-41d4-a716-446655440000",
  "amount": 150.50,
  "encryptedUserDocument": "U3RyaW5nPTEyMzQ1Njc4OTAw",
  "encryptedCreditCardToken": "U3RyaW5nPTQxMTExMTExMTExMTExMTE=",
  "createdAt": "2026-08-20T19:49:56"
}
```

---

## 4. **Ports (Interfaces)**

### 4.1 Port de Persistência: `TransactionItauPort`

**Localização:** `core/port/TransactionItauPort.java`

```java
public interface TransactionItauPort {
    
    /**
     * Retorna todas as transações armazenadas
     */
    List<TransactionItau> getAllTransactions();
    
    /**
     * Cria uma nova transação
     */
    @Transactional
    void createTransactionBy(TransactionItau transactionItau);
    
    /**
     * Filtra transações por intervalo de data/hora
     */
    List<TransactionItau> getTransactionsByDateTime(OffsetDateTime dateTimeRange);
    
    /**
     * Cria e retorna a transação criada
     */
    TransactionItau createTransaction(TransactionItau transactionItau);
    
    /**
     * Busca por ID
     */
    Optional<TransactionItau> getById(Long transactionId);
    
    /**
     * Deleta por ID
     */
    void deleteById(final Long transactionId);
    
    /**
     * Deleta todas
     */
    void deleteAll();
}
```

**Implementação:** `TransactionItauAdapter`

### 4.2 Port de Memória: `TransactionItauMemoryPort`

```java
public interface TransactionItauMemoryPort {
    
    /**
     * Todas as transações em cache de memória
     */
    List<TransactionItau> getAllTransactions();
    
    /**
     * Cria em memória (sem banco)
     */
    TransactionItau createTransaction(TransactionItau transactionItau);
    
    /**
     * Busca por ID da transação (UUID)
     */
    Optional<TransactionItau> getById(final String transactionId);
    
    /**
     * Deleta por ID
     */
    void deleteById(String transactionId);
    
    /**
     * Deleta todas
     */
    void deleteAll();
}
```

**Implementação:** `TransactionItauMemoryAdapter`

---

## 5. **Adapters de Saída (Out)**

### 5.1 Adapter de Database: `TransactionItauAdapter`

**Localização:** `adapter/out/database/TransactionItauAdapter.java`

```java
@ApplicationScoped
@Slf4j
public class TransactionItauAdapter implements TransactionItauPort {
    
    @Inject
    TransactionItauRepository transactionItauRepository;
    
    @Override
    public List<TransactionItau> getAllTransactions() {
        log.info("Buscando todas as transações existentes.");
        return transactionItauRepository.listAll()
                .stream()
                .map(TransactionItau::new)
                .toList();
    }
    
    @Transactional
    @Override
    public TransactionItau createTransaction(TransactionItau transactionItau) {
        log.info("Salvando nova transação no banco de dados. Payload: {}", transactionItau);
        var transactionItauEntity = getTransactionItauEntity(transactionItau);
        persistirTransactionEntity(transactionItauEntity);
        return new TransactionItau(transactionItauEntity);
    }
    
    @Override
    public Optional<TransactionItau> getById(Long transactionId) {
        log.info("Buscar transação por ID: {}", transactionId);
        return transactionItauRepository.findByIdOptional(transactionId)
                .map(TransactionItau::new);
    }
    
    @Transactional
    @Override
    public void deleteById(Long transactionId) {
        transactionItauRepository.deleteById(transactionId);
    }
    
    private TransactionItauEntity getTransactionItauEntity(TransactionItau transactionItau) {
        var transactionItauEntity = new TransactionItauEntity(transactionItau);
        transactionItauEntity.setTransactionId(UUID.randomUUID().toString());
        return transactionItauEntity;
    }
    
    private void persistirTransactionEntity(TransactionItauEntity transactionItauEntity) {
        transactionItauRepository.persistAndFlush(transactionItauEntity);
    }
}
```

**Padrão:** Converte entre Domain Objects e JPA Entities

### 5.2 Adapter de Memória: `TransactionItauMemoryAdapter`

**Localização:** `adapter/out/database/TransactionItauMemoryAdapter.java`

```java
@Singleton
@RequiredArgsConstructor
@Slf4j
public class TransactionItauMemoryAdapter implements TransactionItauMemoryPort {
    
    private final List<TransactionItau> transactionItauList = new ArrayList<>();
    
    @Override
    public List<TransactionItau> getAllTransactions() {
        log.info("Buscando todas as transações existentes.");
        return this.transactionItauList;
    }
    
    @Override
    public TransactionItau createTransaction(TransactionItau transactionItau) {
        log.info("Criando uma nova transação. Payload: {}", transactionItau);
        final var transactionId = UUID.randomUUID().toString();
        transactionItau.setId(RandomUtils.secureStrong().randomLong(1, 1000000));
        transactionItau.setTransactionId(transactionId);
        this.transactionItauList.add(transactionItau);
        
        return getById(transactionId)
                .orElseThrow(
                    () -> new TransactionItauCreateFailedException("Falha ao criar a transação.")
                );
    }
    
    @Override
    public Optional<TransactionItau> getById(final String transactionId) {
        log.info("Buscando transação por meio do id da transação {}.", transactionId);
        return this.transactionItauList.stream()
                .filter(transactionItau -> transactionId.equals(transactionItau.getTransactionId()))
                .findFirst();
    }
    
    @Override
    public void deleteById(String transactionId) {
        log.info("Excluindo transação por meio do id da transação {}.", transactionId);
        getById(transactionId)
                .orElseThrow(() ->
                    new TransactionItauNotFoundException(
                        "Nenhuma transação encontrada por meio do id da transação %s."
                            .formatted(transactionId)
                    )
                );
        this.transactionItauList.removeIf(
            transactionItau -> transactionId.equals(transactionItau.getTransactionId())
        );
    }
    
    @Override
    public void deleteAll() {
        log.info("Excluindo todas as transações existentes.");
        if (CollectionUtils.isEmpty(this.transactionItauList)) {
            throw new TransactionItauNotFoundException("Nenhuma transação encontrada para ser deletada.");
        }
        this.transactionItauList.clear();
    }
}
```

**Padrão:** Cache em memória com lista sincronizada

---

## 6. **Use Cases (Lógica de Negócio)**

### 6.1 Create Use Case: `TransactionItauCreateUseCaseImpl`

**Localização:** `core/usecase/TransactionItauCreateUseCaseImpl.java`

```java
@Singleton
@RequiredArgsConstructor
@Slf4j
public class TransactionItauCreateUseCaseImpl implements TransactionItauCreateUseCase {
    
    @Inject
    TransactionItauMemoryPort transactionItauMemoryPort;
    
    @Inject
    TransactionItauPort transactionItauPort;
    
    @Override
    public TransactionItauOutput createTransaction(TransactionItauInput input) {
        log.info("Inicializando processamento de criação de transação. Payload: {}", input);
        validate(input);
        
        try {
            return TransactionItauOutput.from(
                transactionItauMemoryPort.createTransaction(new TransactionItau(input))
            );
        } catch (Exception e) {
            log.error("Falha ao criar a transação. Payload: {}. Erro: {}", input, e.getMessage());
            throw new EntityCreateFailedException(
                "Falha ao criar a transação. Payload: %s. Erro: %s"
                    .formatted(input, e.getMessage()), 
                e
            );
        }
    }
    
    @Override
    public TransactionItauOutput createNewTransaction(TransactionItauInput input) {
        log.info("Inicializando processamento de criação de uma nova transação. Payload: {}", input);
        validate(input);
        
        try {
            return TransactionItauOutput.from(
                transactionItauPort.createTransaction(new TransactionItau(input))
            );
        } catch (Exception e) {
            log.error("Falha ao criar a transação. Payload: {}. Erro: {}", input, e.getMessage());
            throw new EntityCreateFailedException(
                "Falha ao criar a transação. Payload: %s. Erro: %s"
                    .formatted(input, e.getMessage()), 
                e
            );
        }
    }
    
    @Override
    public boolean isCreatedAtInvalid(TransactionItauInput input) {
        return input.createdAt() == null || input.createdAt().isAfter(OffsetDateTime.now());
    }
    
    private void validate(TransactionItauInput input) {
        // Valida amount > 0
        if (input.amount().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("O valor da transação deve ser maior que zero. Payload: {}", input);
            throw new EntityCreateFailedException("O valor da transação deve ser maior que zero.");
        }
        
        // Valida data/hora
        if (isCreatedAtInvalid(input)) {
            log.error("A data de criação da transação é obrigatória. Payload: {}", input);
            throw new EntityCreateFailedException(
                "A data/hora de criação da transação é obrigatória. Data e hora maiores que data atual não são permitidos."
            );
        }
    }
}
```

**Fluxo:**
1. Validação de regras de negócio
2. Conversão de Input → Domain
3. Persistência via Port
4. Conversão de Domain → Output
5. Tratamento de exceções

### 6.2 Gets Use Case: `TransactionItauGetsUseCaseImpl`

```java
@Singleton
@Slf4j
public class TransactionItauGetsUseCaseImpl implements TransactionItauGetsUseCase {
    
    @Inject
    TransactionItauMemoryPort transactionItauMemoryPort;
    
    @Override
    public List<TransactionItauOutput> getAll() {
        log.info("Buscando todas as transações em cache de memória");
        return transactionItauMemoryPort.getAllTransactions()
                .stream()
                .map(TransactionItauOutput::from)
                .toList();
    }
    
    @Override
    public TransactionItauOutput getById(String transactionId) {
        log.info("Buscando transação por ID: {}", transactionId);
        return transactionItauMemoryPort.getById(transactionId)
                .map(TransactionItauOutput::from)
                .orElseThrow(() -> 
                    new TransactionItauNotFoundException(
                        "Transação não encontrada com ID: " + transactionId
                    )
                );
    }
}
```

### 6.3 Remove Use Case: `TransactionItauRemoveUseCaseImpl`

```java
@Singleton
@Slf4j
public class TransactionItauRemoveUseCaseImpl implements TransactionItauRemoveUseCase {
    
    @Inject
    TransactionItauMemoryPort transactionItauMemoryPort;
    
    @Override
    public void deleteById(String transactionId) {
        log.info("Deletando transação com ID: {}", transactionId);
        transactionItauMemoryPort.deleteById(transactionId);
    }
    
    @Override
    public void deleteAll() {
        log.info("Deletando todas as transações");
        transactionItauMemoryPort.deleteAll();
    }
}
```

### 6.4 Statistics Use Case: `StatiticsTransactionItauUseCaseImpl`

```java
@Singleton
@Slf4j
public class StatiticsTransactionItauUseCaseImpl implements StatiticsTransactionItauUseCase {
    
    @Inject
    TransactionItauMemoryPort transactionItauMemoryPort;
    
    @Override
    public StatisticsItauOutput calculateStatistics(Integer intervaloBusca) {
        log.info("Calculando estatísticas de transações com intervalo de busca: {}", intervaloBusca);
        
        List<TransactionItau> transactions = transactionItauMemoryPort.getAllTransactions();
        
        BigDecimal totalAmount = transactions.stream()
                .map(TransactionItau::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return StatisticsItauOutput.builder()
                .totalTransactions((long) transactions.size())
                .totalAmount(totalAmount)
                .averageAmount(transactions.isEmpty() ? BigDecimal.ZERO : 
                    totalAmount.divide(new BigDecimal(transactions.size()), RoundingMode.HALF_UP))
                .build();
    }
}
```

---

## 7. **Adapters de Entrada (REST Controllers)**

### 7.1 Controller de Transações: `TransactionItauController`

**Localização:** `adapter/in/http/controllers/itau_challenge/TransactionItauController.java`

```java
@Path("/itau/transactions")
@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
@Slf4j
public class TransactionItauController {
    
    @Inject
    TransactionItauCreateUseCase transactionCreateUseCase;
    
    @Inject
    TransactionItauGetsUseCase transactionGetsUseCase;
    
    @Inject
    TransactionItauRemoveUseCase transactionRemoveUseCase;
    
    /**
     * POST /itau/transactions/v1
     * Cria uma transação em memória
     */
    @POST
    @Path(value = "/v1")
    @Operation(summary = "Cria uma nova transação em cache")
    @APIResponses(value = {
        @APIResponse(responseCode = "201", description = "Transação criada com sucesso"),
        @APIResponse(responseCode = "422", description = "Validação falhou"),
        @APIResponse(responseCode = "500", description = "Erro interno")
    })
    public Response create(@Valid TransactionItauInput input) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("Inicializando rota de criação de transação");
            
            return Response.status(Response.Status.CREATED)
                    .entity(transactionCreateUseCase.createTransaction(input))
                    .build();
        } finally {
            MdcUtils.clear();
        }
    }
    
    /**
     * POST /itau/transactions/v2
     * Cria uma transação em banco de dados
     */
    @POST
    @Path(value = "/v2")
    @Operation(summary = "Cria uma transação persistida no banco")
    public Response createTransaction(@Valid TransactionItauInput input) {
        try {
            MdcUtils.putTransactionIdRandom();
            return Response.status(Response.Status.CREATED)
                    .entity(transactionCreateUseCase.createNewTransaction(input))
                    .build();
        } finally {
            MdcUtils.clear();
        }
    }
    
    /**
     * GET /itau/transactions/v1
     * Retorna todas as transações
     */
    @GET
    @Path(value = "/v1")
    public Response getAll() {
        try {
            MdcUtils.putTransactionIdRandom();
            return Response.ok(transactionGetsUseCase.getAll()).build();
        } finally {
            MdcUtils.clear();
        }
    }
    
    /**
     * GET /itau/transactions/v1/{id}
     * Retorna uma transação por ID
     */
    @GET
    @Path(value = "/v1/{id}")
    public Response getById(@PathParam("id") String id) {
        try {
            MdcUtils.putTransactionIdRandom();
            return Response.ok(transactionGetsUseCase.getById(id)).build();
        } finally {
            MdcUtils.clear();
        }
    }
    
    /**
     * DELETE /itau/transactions/v1/{id}
     * Deleta uma transação por ID
     */
    @DELETE
    @Path(value = "/v1/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            MdcUtils.putTransactionIdRandom();
            transactionRemoveUseCase.deleteById(id);
            return Response.noContent().build();
        } finally {
            MdcUtils.clear();
        }
    }
    
    /**
     * DELETE /itau/transactions/v1/
     * Deleta todas as transações
     */
    @DELETE
    @Path(value = "/v1/")
    public Response deleteAll() {
        try {
            MdcUtils.putTransactionIdRandom();
            transactionRemoveUseCase.deleteAll();
            return Response.noContent().build();
        } finally {
            MdcUtils.clear();
        }
    }
}
```

#### Endpoints Detalhados:

| Método | Path | Descrição | Status | Corpo |
|--------|------|-----------|--------|-------|
| POST | `/itau/transactions/v1` | Cria transação em memória | 201 | TransactionItauInput |
| POST | `/itau/transactions/v2` | Cria transação em DB | 201 | TransactionItauInput |
| GET | `/itau/transactions/v1` | Lista todas (memória) | 200 | - |
| GET | `/itau/transactions/v1/{id}` | Busca por ID | 200 | - |
| DELETE | `/itau/transactions/v1/{id}` | Deleta por ID | 204 | - |
| DELETE | `/itau/transactions/v1/` | Deleta todas | 204 | - |

### 7.2 Controller de Estatísticas: `StatiticsItauController`

```java
@Path("/itau/statistics")
@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
@Slf4j
public class StatiticsItauController {
    
    @Inject
    StatiticsTransactionItauUseCase statiticsTransactionItauUseCase;
    
    /**
     * GET /itau/statistics/v1/?intervaloBusca=30
     * Retorna estatísticas de transações
     */
    @GET
    @Path("/v1/")
    @APIResponse(description = "Estatísticas de transações")
    public Response getStatisticsSummary(@QueryParam("intervaloBusca") Integer intervaloBusca) {
        try {
            MdcUtils.putTransactionIdRandom();
            log.info("Inicializando rota de busca de estatísticas");
            
            return Response
                    .ok(statiticsTransactionItauUseCase.calculateStatistics(intervaloBusca))
                    .build();
        } finally {
            MdcUtils.clear();
        }
    }
}
```

---

## 8. **Tratamento de Exceções**

### 8.1 Global Exception Handler: `HandlerAdviceController`

**Localização:** `config/handler/HandlerAdviceController.java`

```java
@Slf4j
@Provider
public class HandlerAdviceController implements ExceptionMapper<Exception> {
    
    @Context
    HttpServerRequest request;
    
    @Inject
    Logger logger;
    
    @ConfigProperty(name = "apiquarkusgeneral.application.log-complete", defaultValue = "NULO") 
    boolean showLogComplete;
    
    @Override
    public Response toResponse(Exception exception) {
        return mapExceptionToResponse(exception);
    }
    
    private Response mapExceptionToResponse(Exception exception) {
        return switch (exception) {
            case WebApplicationException webAppEx -> {
                var originalErrorResponse = webAppEx.getResponse();
                yield Response.fromResponse(originalErrorResponse)
                        .entity(originalErrorResponse.getStatusInfo().getReasonPhrase())
                        .build();
            }
            case IllegalArgumentException e -> Response.status(HttpResponseStatus.NOT_FOUND.code())
                    .entity(e.getMessage())
                    .build();
            case HttpException httpEx -> toResponseFromCustomizeApplication(httpEx);
            default -> toResponseInternalServerError(exception);
        };
    }
    
    private Response toResponseInternalServerError(final Exception throwable) {
        exposeLogErrorFrom(throwable);
        logger.fatalf(throwable, "Failed to process request to: {}", getUriComplete());
        
        final var httpStatus = getHttpStatusOrDefault(null);
        final var response = buildApiErrorResponse(
            throwable.getMessage(),
            throwable.getLocalizedMessage(),
            httpStatus
        );
        
        return buildResponseFrom(httpStatus, response);
    }
    
    private Response toResponseFromCustomizeApplication(final HttpException throwable) {
        exposeLogErrorFrom(throwable);
        final var httpStatus = getHttpStatusOrDefault(throwable.getHttpStatus());
        
        final var response = buildApiErrorResponse(
            throwable.getMessage(),
            throwable.getLocalizedMessage(),
            throwable.getHttpStatus()
        );
        
        return buildResponseFrom(httpStatus, response);
    }
    
    private Response buildResponseFrom(final HttpResponseStatus httpStatus,
                                       final ApiErrorResponse response) {
        return Response.status(httpStatus.code())
                .entity(new ResponseDataError(response))
                .build();
    }
}
```

### 8.2 Exceções Customizadas

**Hierarquia:**

```
HttpException (base customizada)
├── EntityCreateFailedException
├── ParseEntityFailedException
├── EntityNotFoundException
└── api/
    ├── TransactionItauCreateFailedException
    ├── TransactionItauNotFoundException
    └── TransactionItauParseException
```

---

## 9. **Configuração e Propriedades**

### 9.1 Application Properties: `application.properties`

```properties
quarkus.profile=itau

# Servidor
quarkus.http.port=8095
quarkus.application.name=API-QUARKUS-GENERAL

# Logging
quarkus.log.console.json.additional-field."microservice".value=apiquarkusgeneral
quarkus.log.console.json.additional-field."microservice".type=string

# CORS
quarkus.http.cors.enabled=true
quarkus.http.cors.origins=*
quarkus.http.cors.methods=*
quarkus.http.cors.headers=*

# Timeouts
quarkus.http.read-timeout=30S

# Aplicação
apiquarkusgeneral.application.log-complete=true
```

### 9.2 Profile Itau: `application-itau.properties`

```properties
# Banco de dados H2
quarkus.datasource.db-kind=h2
quarkus.datasource.jdbc.url=jdbc:h2:mem:quarkusdb
quarkus.datasource.username=sa
quarkus.datasource.password=

# Hibernate
quarkus.hibernate-orm.dialect=org.hibernate.dialect.H2Dialect
quarkus.hibernate-orm.ddl-generation=create
quarkus.jpa.properties."hibernate.format_sql"=true

# Criptografia
APP_KEY_PWD_ENCRYPT=minha-chave-super-secreta
```

### 9.3 Profile Test: `application-test.properties`

```properties
quarkus.datasource.db-kind=h2
quarkus.datasource.jdbc.url=jdbc:h2:mem:test
quarkus.datasource.username=sa
quarkus.datasource.password=

quarkus.hibernate-orm.dialect=org.hibernate.dialect.H2Dialect
quarkus.hibernate-orm.ddl-generation=drop-and-create

# Random UUID para teste
APP_KEY_PWD_ENCRYPT=random-key-for-test
```

---

## 10. **Criptografia: EncryptoManagerConfig**

**Localização:** `config/EncryptoManagerConfig.java`

```java
public class EncryptoManagerConfig {
    
    private static final StrongTextEncryptor encryptor;
    private static final String PROFILE_NAME_TEST = "test";
    
    static {
        encryptor = new StrongTextEncryptor();
        encryptor.setPassword(getPassword());
    }
    
    private static String getPassword() {
        if (isProfileTest()) {
            return UUID.randomUUID().toString();
        }
        return generateValueConfigProviderPasswor("APP_KEY_PWD_ENCRYPT");
    }
    
    private static String generateValueConfigProviderPasswor(String key) {
        return ConfigProvider.getConfig().getValue(key, String.class);
    }
    
    private static boolean isProfileTest() {
        return FunctionalUtils.getActiveProfiles()
            .stream()
            .anyMatch(s -> s.equals(PROFILE_NAME_TEST));
    }
    
    public static String encrypt(String rawText) {
        return encryptor.encrypt(rawText);
    }
    
    public static String decrypt(String encryptedText) {
        return encryptor.decrypt(encryptedText);
    }
}
```

**Características:**
- Usa Jasypt para criptografia forte (PBKDF2)
- Chave configurável via variável de ambiente
- UUID aleatório em ambiente de teste

---

## 11. **Fluxos de Requisição Completos**

### 11.1 Fluxo: Criar Transação (POST v1 - Memória)

```
1. Cliente HTTP
   ├─> POST /itau/transactions/v1
   └─> JSON: TransactionItauInput

2. TransactionItauController.create()
   ├─> MdcUtils.putTransactionIdRandom()
   └─> transactionCreateUseCase.createTransaction(input)

3. TransactionItauCreateUseCaseImpl
   ├─> validate(input) [amount > 0, createdAt válido]
   ├─> new TransactionItau(input)
   │   ├─> BeanUtils.copyProperties()
   │   ├─> Criptografa CPF e Token do Cartão
   │   └─> Sincroniza amount e transactionValue
   └─> transactionItauMemoryPort.createTransaction()

4. TransactionItauMemoryAdapter
   ├─> Gera UUID para transactionId
   ├─> Gera ID aleatório (1-1000000)
   ├─> Adiciona à lista em memória
   └─> Retorna TransactionItau

5. TransactionItauCreateUseCaseImpl
   ├─> TransactionItauOutput.from(domain)
   └─> Retorna Output

6. TransactionItauController
   ├─> Response.status(201)
   ├─> .entity(output)
   └─> .build()

7. Cliente HTTP
   └─> Recebe JSON: TransactionItauOutput (201 Created)
```

### 11.2 Fluxo: Buscar Transações (GET v1)

```
1. Cliente HTTP
   └─> GET /itau/transactions/v1

2. TransactionItauController.getAll()
   └─> transactionGetsUseCase.getAll()

3. TransactionItauGetsUseCaseImpl
   ├─> transactionItauMemoryPort.getAllTransactions()
   └─> .map(TransactionItauOutput::from).toList()

4. TransactionItauMemoryAdapter
   └─> Retorna this.transactionItauList

5. Cliente HTTP
   └─> Recebe JSON: List<TransactionItauOutput> (200 OK)
```

### 11.3 Fluxo: Criptografia (Create + Encrypt)

```
TransactionItau(TransactionItauInput input)
├─> rawUserDocument = input.documentNumber() // "12345678900"
├─> rawCreditCardToken = input.creditCardToken() // "4111111111111111"
│
├─> EncryptoManagerConfig.encrypt(rawUserDocument)
│   ├─> StrongTextEncryptor.encrypt("12345678900")
│   └─> "U3RyaW5nPTEyMzQ1Njc4OTAw" (base64 encriptado)
│
└─> EncryptoManagerConfig.encrypt(rawCreditCardToken)
    ├─> StrongTextEncryptor.encrypt("4111111111111111")
    └─> "U3RyaW5nPTQxMTExMTExMTExMTExMTE=" (base64 encriptado)
```

---

## 12. **Estrutura de Diretórios Completa**

```
src/main/java/br/com/daniel/java/quarkus/general/
├── Application.java
│   └─ QuarkusMain - Ponto de entrada personalizado
│
├── adapter/
│   ├── in/
│   │   └── http/
│   │       └── controllers/
│   │           └── itau_challenge/
│   │               ├── TransactionItauController.java
│   │               └── StatiticsItauController.java
│   │
│   └── out/
│       ├── database/
│       │   ├── TransactionItauAdapter.java (JPA/Hibernate)
│       │   ├── TransactionItauMemoryAdapter.java (In-Memory)
│       │   └── repository/
│       │       └── TransactionItauRepository.java (Panache)
│       │
│       └── entities/
│           └── TransactionItauEntity.java (@Entity JPA)
│
├── config/
│   ├── EncryptoManagerConfig.java (Criptografia)
│   └── handler/
│       ├── GlobalExceptionHandler.java
│       ├── HandlerAdviceController.java (@Provider)
│       └── errors/
│           ├── ApiErrorResponse.java
│           └── ResponseDataError.java
│
├── core/
│   ├── domain/
│   │   └── TransactionItau.java (Domain Object)
│   │
│   ├── port/
│   │   ├── TransactionItauPort.java (Interface DB)
│   │   └── TransactionItauMemoryPort.java (Interface Memória)
│   │
│   └── usecase/
│       ├── TransactionItauCreateUseCase.java (Interface)
│       ├── TransactionItauCreateUseCaseImpl.java (Implementação)
│       ├── TransactionItauGetsUseCase.java
│       ├── TransactionItauGetsUseCaseImpl.java
│       ├── TransactionItauRemoveUseCase.java
│       ├── TransactionItauRemoveUseCaseImpl.java
│       ├── StatiticsTransactionItauUseCase.java
│       ├── StatiticsTransactionItauUseCaseImpl.java
│       ├── input/
│       │   └── TransactionItauInput.java (DTO Input - Record)
│       └── output/
│           ├── TransactionItauOutput.java (DTO Output)
│           └── StatisticsItauOutput.java
│
├── exceptions/
│   ├── HttpException.java (Base customizada)
│   ├── EntityCreateFailedException.java
│   ├── ParseEntityFailedException.java
│   ├── EntityNotFoundException.java
│   └── api/
│       ├── TransactionItauNotFoundException.java
│       ├── TransactionItauCreateFailedException.java
│       └── TransactionItauParseException.java
│
└── utils/
    ├── FunctionalUtils.java
    └── logs/
        └── MdcUtils.java (MDC para rastreamento)

src/main/resources/
├── application.properties (Principal)
├── application-itau.properties (Profile Itau - H2)
├── application-test.properties (Profile Test)
├── application-h2db.properties (Profile H2DB)
└── import.sql (Dados iniciais)
```

---

## 13. **Padrões de Design Utilizados**

### 13.1 Arquitetura Hexagonal (Ports & Adapters)
- Isolamento de lógica de negócio
- Múltiplas implementações de persistência (DB + Memory)
- Facilita testes

### 13.2 SOLID

| Princípio | Aplicação |
|-----------|-----------|
| **S**ingle Responsibility | Cada classe tem uma única razão de mudança |
| **O**pen/Closed | Aberto para extensão (novos adapters), fechado para modificação |
| **L**iskov Substitution | Adapters são intercambiáveis via Ports |
| **I**nterface Segregation | Ports específicos (TransactionItauPort, TransactionItauMemoryPort) |
| **D**ependency Inversion | Dependências injetadas via @Inject |

### 13.3 DTOs (Data Transfer Objects)
- `TransactionItauInput` - Validação na entrada
- `TransactionItauOutput` - Formato de resposta
- Records Java 14+ para imutabilidade

### 13.4 Factory Pattern
- `TransactionItau(input)` - Factory constructor
- `TransactionItauOutput.from(domain)` - Factory method

### 13.5 Adapter Pattern
- `TransactionItauAdapter` (JPA) vs `TransactionItauMemoryAdapter`
- Mesmo Port, múltiplas implementações

---

## 14. **Exemplo Completo de Uso**

### Requisição 1: Criar Transação

```bash
curl -X POST http://localhost:8095/itau/transactions/v1 \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 250.75,
    "createdAt": "2026-08-20T19:49:56Z",
    "documentNumber": "12345678901",
    "creditCardToken": "4532015112830366"
  }'
```

**Resposta (201 Created):**
```json
{
  "id": 123456,
  "transactionId": "a1b2c3d4-e5f6-4789-0abc-def123456789",
  "amount": 250.75,
  "encryptedUserDocument": "U3RyaW5nPTEyMzQ1Njc4OTAx",
  "encryptedCreditCardToken": "U3RyaW5nPTQ1MzIwMTUxMTI4MzAzNjY=",
  "createdAt": "2026-08-20T19:49:56"
}
```

### Requisição 2: Listar Transações

```bash
curl -X GET http://localhost:8095/itau/transactions/v1 \
  -H "Content-Type: application/json"
```

**Resposta (200 OK):**
```json
[
  {
    "id": 123456,
    "transactionId": "a1b2c3d4-e5f6-4789-0abc-def123456789",
    "amount": 250.75,
    "encryptedUserDocument": "U3RyaW5nPTEyMzQ1Njc4OTAx",
    "encryptedCreditCardToken": "U3RyaW5nPTQ1MzIwMTUxMTI4MzAzNjY=",
    "createdAt": "2026-08-20T19:49:56"
  }
]
```

### Requisição 3: Buscar por ID

```bash
curl -X GET http://localhost:8095/itau/transactions/v1/a1b2c3d4-e5f6-4789-0abc-def123456789 \
  -H "Content-Type: application/json"
```

### Requisição 4: Deletar Transação

```bash
curl -X DELETE http://localhost:8095/itau/transactions/v1/a1b2c3d4-e5f6-4789-0abc-def123456789
```

**Resposta (204 No Content)**

### Requisição 5: Estatísticas

```bash
curl -X GET "http://localhost:8095/itau/statistics/v1/?intervaloBusca=30" \
  -H "Content-Type: application/json"
```

**Resposta:**
```json
{
  "totalTransactions": 5,
  "totalAmount": 1250.75,
  "averageAmount": 250.15
}
```

---

## 15. **Executando a Aplicação**

### Desenvolvimento

```bash
# Com live reload
./mvnw compile quarkus:dev

# Acesso
# API REST: http://localhost:8095
# Dev UI:   http://localhost:8095/q/dev
# OpenAPI:  http://localhost:8095/q/openapi
# Swagger:  http://localhost:8095/q/swagger-ui
```

### Testes

```bash
./mvnw test
./mvnw test -Dtest=TransactionItauControllerTest
```

### Build Produção

```bash
# JAR regular
./mvnw package

# Executar
java -jar target/quarkus-app/quarkus-run.jar

# Nativo (GraalVM)
./mvnw package -Pnative
./target/quarkus-overview-chapter-general-1.0.0-SNAPSHOT-runner
```

---

## 16. **Conclusão**

Esta arquitetura demonstra:

✅ **Clean Architecture** - Separação de responsabilidades clara
✅ **Hexagonal Architecture** - Desacoplamento de camadas
✅ **SOLID Principles** - Código extensível e testável
✅ **Security** - Criptografia de dados sensíveis
✅ **REST Best Practices** - Endpoints bem documentados
✅ **Logging & Monitoring** - Rastreamento completo
✅ **Multiple Adapters** - Database e Memory (cache)
✅ **Exception Handling** - Tratamento centralizado

**Padrão ideal para aplicações empresariais Quarkus!**
