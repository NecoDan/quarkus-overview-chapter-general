# 📋 Documentação de Modelagem Completa
## Repositório: quarkus-overview-chapter-general

### 1. **Visão Geral do Projeto**

Este é um projeto de demonstração/aprendizado baseado em **Quarkus** (Java framework supersônico e subatômico) que implementa uma API RESTful geral com suporte a múltiplos padrões de serialização e persistência de dados.

**Tecnologias Principais:**
- **Linguagem:** Java 21
- **Framework:** Quarkus 3.33.3
- **Build:** Maven (mvnw)
- **Serialização:** JSON-B, Jackson, JAXB
- **ORM:** Hibernate com Panache
- **Bancos de Dados:** H2 (em memória), MySQL
- **Validação:** Hibernate Validator

---

### 2. **Estrutura de Organização**

```
quarkus-overview-chapter-general/
├── pom.xml                          # Configuração Maven (dependências e plugins)
├── mvnw / mvnw.cmd                  # Maven Wrapper (Linux/Windows)
├── README.md                        # Documentação básica
├── .gitignore                       # Exclusões Git
├── .dockerignore                    # Exclusões Docker
├── .github/                         # Configurações GitHub (workflows, etc)
│
├── src/main/
│   ├── java/br/com/daniel/java/     # Código-fonte principal (estrutura de pacotes)
│   │   └── [módulos de negócio]     # A definir conforme desenvolvimento
│   │
│   └── resources/                   # Recursos da aplicação
│       └── [arquivos de config]     # application.properties, arquivos estáticos
│
├── src/test/
│   ├── java/                        # Testes unitários e integração
│   └── resources/                   # Recursos para testes
│
└── target/                          # Build output (gerado)
```

---

### 3. **Arquitetura de Camadas**

A aplicação segue uma arquitetura em camadas padrão para APIs Quarkus:

```
┌─────────────────────────────────────┐
│   REST Endpoints (JAX-RS)           │  ← Controllers / Resources
├─────────────────────────────────────┤
│   Business Logic (Services)         │  ← Lógica de negócio
├─────────────────────────────────────┤
│   Data Access (Repository Pattern)  │  ← Hibernate Panache
├─────────────────────────────────────┤
│   Database Layer                    │  ← H2 / MySQL
└─────────────────────────────────────┘
```

---

### 4. **Stack Tecnológico Detalhado**

#### **Dependências Core:**

| Componente | Versão | Propósito |
|-----------|--------|----------|
| **quarkus-resteasy** | 3.33.3 | Framework REST (JAX-RS) |
| **quarkus-resteasy-jsonb** | 3.33.3 | Serialização JSON-B |
| **quarkus-resteasy-jackson** | 3.33.3 | Serialização Jackson |
| **quarkus-resteasy-jaxb** | 3.33.3 | Serialização JAXB (XML) |
| **quarkus-resteasy-multipart** | 3.33.3 | Upload de arquivos multipart |
| **quarkus-arc** | 3.33.3 | Injeção de dependência (CDI) |
| **quarkus-smallrye-openapi** | 3.33.3 | Documentação OpenAPI/Swagger |
| **quarkus-smallrye-health** | 3.33.3 | Health checks |
| **quarkus-logging-json** | 3.33.3 | Logging estruturado em JSON |

#### **Persistência de Dados:**

| Componente | Versão | Propósito |
|-----------|--------|----------|
| **quarkus-hibernate-orm-panache** | 3.33.3 | ORM com abstração Panache |
| **quarkus-jdbc-h2** | 3.33.3 | Banco H2 em memória (testes/dev) |
| **quarkus-jdbc-mysql** | 3.33.3 | Conector MySQL |
| **quarkus-hibernate-validator** | 3.33.3 | Validação de entities |

#### **Ferramentas e Utilitários:**

| Biblioteca | Versão | Propósito |
|-----------|--------|----------|
| **Lombok** | 1.18.34 | Redução de boilerplate (getters, setters) |
| **MapStruct** | 1.5.5.Final | Mapeamento entre DTOs e entities |
| **Google Guava** | 33.4.0-jre | Utilitários Java (collections, cache) |
| **Apache Commons Lang** | 3.18.0 | Utilitários de strings e arrays |
| **Apache Commons Collections** | 4.5.0-M2 | Coleções utilitárias |
| **Jasypt** | 1.9.3 | Criptografia de strings |
| **REST Assured** | (test scope) | Testes de APIs REST |
| **JUnit 5 + Mockito** | (test scope) | Testes unitários e mocks |
| **JavaFaker** | 1.0.2 | Geração de dados fake (testes) |
| **SnakeYAML** | 2.2 | Parsing de YAML |

---

### 5. **Fluxo de Requisições**

```
Cliente HTTP
     ↓
[REST Endpoint - @Path / @GET / @POST...]
     ↓
[Service Layer - Lógica de negócio]
     ↓
[Repository / Panache Query]
     ↓
[Hibernate ORM]
     ↓
[JDBC Driver - MySQL/H2]
     ↓
[Banco de Dados]
```

**Padrão de Resposta:**
- Serialização em JSON (Jackson ou JSON-B)
- OpenAPI/Swagger documentação automática

---

### 6. **Convenções de Pacotes**

```
br.com.daniel.java.quarkus.general
├── entities/           # @Entity classes (JPA)
├── dto/               # Data Transfer Objects
├── resources/        # REST Endpoints (@Path)
├── services/         # Lógica de negócio (@ApplicationScoped)
├── repositories/     # Acesso a dados (extends PanacheRepository)
├── mappers/          # MapStruct mappers
├── validators/       # Validadores customizados
├── exceptions/       # Exceções da aplicação
├── config/           # Configurações (properties, beans)
├── utils/            # Utilitários e helpers
└── constants/        # Constantes globais
```

---

### 7. **Configuração e Build**

#### **Compilação e Execução:**

```bash
# Modo desenvolvimento com live coding
./mvnw compile quarkus:dev
# Acesso: http://localhost:8080/q/dev (Dev UI)

# Empacotamento JAR
./mvnw package

# Executar JAR
java -jar target/quarkus-app/quarkus-run.jar

# Build Über-JAR (todas as deps incluídas)
./mvnw package -Dquarkus.package.type=uber-jar

# Build nativo (GraalVM)
./mvnw package -Pnative

# Build nativo em container
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

#### **Propriedades Java:**
- **Versão Source/Target:** Java 21
- **Encoding:** UTF-8
- **Compiler Parameters:** Habilitado (para reflection)

---

### 8. **Endpoints e Recursos REST**

A aplicação utiliza **RESTEasy Classic** com suporte a:

- ✅ Multipart file upload
- ✅ JSON-B serialization
- ✅ Jackson serialization
- ✅ JAXB (XML) serialization
- ✅ OpenAPI/Swagger documentation

**Acesso à documentação:**
```
http://localhost:8080/q/openapi
http://localhost:8080/q/swagger-ui
```

---

### 9. **Saúde e Monitoramento**

Integrado **SmallRye Health** para health checks:

```
GET /q/health           # Health geral
GET /q/health/live      # Liveness probe (Kubernetes)
GET /q/health/ready     # Readiness probe (Kubernetes)
```

Logging estruturado em JSON através de `quarkus-logging-json`.

---

### 10. **Banco de Dados**

#### **Desenvolvimento/Testes:**
- **H2 In-Memory:** Sem configuração adicional, perfeito para testes

#### **Produção:**
- **MySQL:** Requer configuração via `application.properties`

#### **ORM:**
- **Hibernate + Panache:** Abstração sobre JPA com métodos helpers
- **Validação:** Hibernate Validator (@NotNull, @NotBlank, etc)

---

### 11. **Testes**

#### **Dependências:**
- **REST Assured:** Testes de APIs HTTP
- **JUnit 5:** Framework de testes
- **Mockito:** Mocking de dependências
- **Quarkus Test Common:** Utilitários de teste Quarkus

#### **Estrutura:**
```
src/test/java/br/com/daniel/java/
├── resources/           # Dados de teste
└── [test classes]       # *Test.java
```

---

### 12. **Nota sobre Recursos Comentados**

No `pom.xml`, algumas dependências estão comentadas e podem ser ativadas conforme necessário:

- `quarkus-rest-client-reactive` - Cliente HTTP reativo
- `quarkus-resteasy-reactive-jackson` - REST reativo
- `quarkus-smallrye-opentracing` - Distributed tracing
- `quarkus-smallrye-metrics` - Métricas Prometheus

---

### 13. **Próximos Passos para Desenvolvimento**

Para expandir a modelagem, você precisará:

1. **Definir Entities:** Criar classes em `br.com.daniel.java.quarkus.general.entities`
2. **Criar DTOs:** Para transferência de dados nas APIs
3. **Implementar Repositories:** Estendendo `PanacheRepository<Entity, ID>`
4. **Desenvolver Services:** Lógica de negócio transacional
5. **Criar Resources (Controllers):** Endpoints REST com validação
6. **Configurar application.properties:** Banco de dados, porta, profiles (dev/test/prod)
7. **Implementar Testes:** Cobertura com REST Assured + JUnit 5

---

Esta documentação fornece a base completa da modelagem do repositório. Para mais detalhes sobre implementação específica de entidades e endpoints, será necessário explorar o código-fonte conforme for desenvolvido.
