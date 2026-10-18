# Arquitetura Técnica do Projeto

## 1. Visão geral

Este projeto é uma aplicação Quarkus com foco em arquitetura em camadas e em port/interface-driven design. A base técnica é composta por:

- Java 21;
- Quarkus 3.33.3;
- Maven;
- Hibernate ORM Panache;
- MongoDB Panache;
- RabbitMQ;
- RESTEasy / Quarkus REST;
- Jackson;
- Validation API;
- OpenAPI / SmallRye Health.

## 2. Escopo tecnológico

A solução foi projetada para apoiar múltiplos contextos de negócio em uma mesma base tecnológica, sem misturar regras do domínio com detalhes de infraestrutura. A aplicação combina endpoints REST, integrações HTTP, processamento assíncrono e persistência relacional/documental.

## 3. Estrutura funcional

### 3.1 Entrada

Os pontos de entrada do sistema ficam em:

- `src/main/java/br/com/daniel/java/quarkus/general/adapter/in/http/controllers/...`
- `src/main/java/br/com/daniel/java/quarkus/general/adapter/in/rabbitmq/...`

Esses componentes se responsabilizam por:

- receber requisições HTTP;
- validar payloads;
- invocar casos de uso;
- converter respostas para payloads externos;
- registrar contexto de execução com MDC.

### 3.2 Domínio

O domínio está em `core` e reúne:

- `domain`: entidades de negócio e objetos de valor;
- `usecase`: lógicas específicas para cada contexto;
- `port`: interfaces que abstraem acessos a dados e integrações;
- `mapper`: conversão entre tipos do domínio e adaptadores externos;
- `input` / `output`: contratos de entrada e saída.

### 3.3 Infraestrutura

A infraestrutura fica em `adapter/out`:

- `database`: repositories, adapters e entidades JPA;
- `files`: leitura de arquivos JSON/XML;
- `apis`: clientes HTTP para dados externos;
- `client`: abstrações de client-side HTTP;
- `entities`: modelos persistidos no banco;

## 4. Padrões arquiteturais aplicados

### 4.1 Camadas

A aplicação separa claremente:

- camada de apresentação / entrada;
- camada de aplicação / casos de uso;
- camada de domínio;
- camada de infraestrutura.

### 4.2 Portas e adaptadores

Os casos de uso dependem de interfaces (`Port`) em vez de implementações concretas. Isso define o contrato de persistência/integridade e reduz o acoplamento entre as regras de negócio e a tecnologia.

### 4.3 Inversão de dependência

Os controllers e consumidores não conhecem diretamente detalhes de acesso a banco, API ou fila; eles apenas invocam casos de uso, que delegam a implementações disponíveis por CDI/Quarkus.

## 5. Contextos de negócio

### 5.1 Itaú

Módulo focado em operações de transação:

- `TransactionItauController`
- `TransactionItauCreateUseCase`
- `TransactionItauGetsUseCase`
- `TransactionItauRemoveUseCase`
- `TransactionItauAdapter`
- `TransactionItauRepository`

As transações são processadas com validações e persistência relacional. O código também traz suporte a diferentes versões de criação (`v1` e `v2`), demonstrando evolução do contrato sem quebrar a API em todos os pontos.

### 5.2 UOL

Módulo focado em jogadores e grupos de heróis:

- `GamePlayerUolController`
- `GamePlayerUolCreateUseCase`
- `GamePlayerUolGetUseCase`
- `HeroGroupUolApiAdapter`
- `GamePlayerUolFileAdapter`

O módulo combina:

- persistência relacional;
- arquivo local com dados de personagens;
- cliente HTTP para integração externa;
- regras de negócio para montar o “grupo de heróis” e o relatório final.

### 5.3 BTG Pactual

Módulo com foco em pedidos e mensageria:

- `OrderBtgPactualController`
- `OrderBtgPactualCreateUseCase`
- `OrderBtgPactualGetsUseCase`
- `OrderBtgPactualAdapter`
- `BtgPactualOrderConsumer`

A persistência é documental em MongoDB e há processamento assíncrono por fila RabbitMQ. O fluxo também suporta paginação e sumarização por cliente.

## 6. Persistência e bancos

### 6.1 Relacional

O módulo relacional usa Hibernate Panache e pode operar com:

- H2 em memória;
- MySQL;
- SQLite.

Esse suporte é configurado por profiles e arquivos `application-*.properties`.

### 6.2 Documental

O módulo BTG usa MongoDB Panache e persiste pedidos em coleção específica, com objetos embutidos de cliente e itens, permitindo consultas agregadas e paginação.

### 6.3 Arquivos e integrações

O módulo UOL lê arquivos locais em JSON/XML para montar o contexto de heróis. Esse padrão ajuda a isolar a dependência externa de dados estáticos do domínio principal.

## 7. Tratamento de erros e validações

A aplicação usa:

- `@Valid` nos inputs;
- classes específicas de exceção por domínio;
- `GlobalExceptionHandler` e tratamento de erros estruturados;
- logging com contexto via `MdcUtils`.

Isso ajuda a manter respostas previsíveis e diagnósticas para operações de API.

## 8. Observabilidade

Os principais elementos observáveis incluem:

- logs em JSON;
- endpoints de health check;
- documentação OpenAPI;
- identificadores de requisição em MDC.

## 9. Testabilidade

A estrutura favorece testes em vários níveis:

- unitários em casos de uso;
- de integração em controladores;
- de adaptadores e clientes HTTP;
- de consumidor de fila;
- testes de domínio e utilitários.

Isso permite validar tanto a orquestração do fluxo quanto as regras de negócio isoladas.

## 10. Conclusão

A arquitetura técnica do projeto reflete um desenho que combina simplicidade e extensibilidade. A separação de responsabilidades e o uso de interfaces para infraestrutura deixam a aplicação preparada para evoluir com novos módulos, diferentes fontes de dados e novos desafios de negócio sem comprometer a coerência do domínio.
