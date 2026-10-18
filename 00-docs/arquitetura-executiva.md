# Arquitetura Executiva do Projeto

## Resumo executivo

- A solução é uma API Quarkus com múltiplos contextos de negócio em uma mesma base tecnológica.
- O desenho arquitetural prioriza separação de responsabilidades, baixo acoplamento e facilidade de evolução.
- O projeto integra REST, bancos relacionais, MongoDB, mensageria, arquivos e serviços externos.
- A arquitetura é adequada para aprendizado, extensão e demonstração de padrões de software em produção.

## 1. Visão geral

Este projeto é uma API em Quarkus que reúne três contextos de negócio em uma mesma base tecnológica: Itaú, UOL e BTG Pactual. A solução foi concebida para demonstrar como uma aplicação moderna pode combinar REST, integração com sistemas externos, processamento assíncrono e persistência em múltiplos modelos.

## 2. Objetivo estratégico

O objetivo principal não é apenas entregar endpoints funcionais, mas também demonstrar um desenho arquitetural que favorece:

- separação de responsabilidades;
- isolamento da lógica de negócio;
- flexibilidade para trocar adaptadores de banco ou integração;
- suporte a múltiplos desafios de negócio em uma mesma aplicação;
- redução do acoplamento com infraestrutura externa.

## 3. Organização da solução

A arquitetura segue um modelo de camadas e portas/adaptadores:

- entrada: controllers REST e consumidor RabbitMQ;
- núcleo: casos de uso e regras de negócio;
- infraestrutura: banco de dados, arquivos, clientes HTTP e mensageria.

Essa divisão permite que o domínio permaneça independente da forma como os dados são persistidos ou consumidos.

## 4. Contextos atendidos

### 4.1 Itaú
- CRUD de transações;
- busca por identificador;
- estatísticas e agregações;
- persistência relacional.

### 4.2 UOL
- cadastro e consulta de jogadores;
- leitura de grupos de heróis por arquivo ou API externa.

### 4.3 BTG Pactual
- criação e consulta de pedidos;
- consulta paginada;
- agregação por cliente;
- processamento assíncrono via RabbitMQ.

## 5. Benefícios da arquitetura

- manutenção mais simples por módulo de negócio;
- menor impacto ao trocar tecnologia de persistência;
- clareza na evolução das regras de negócio;
- suporte a múltiplos tipos de integração sem misturar responsabilidades;
- boa base para testes automatizados.

## 6. Tecnologias centrais

- Java 21;
- Quarkus 3.33.3;
- Hibernate ORM + Panache;
- MongoDB Panache;
- RabbitMQ;
- REST API;
- OpenAPI e health checks.

## 7. Resumo executivo

A aplicação funciona como uma referência prática de arquitetura para APIs Java com Quarkus, especialmente em cenários em que uma mesma solução precisa atender a diferentes domínios, integrações e tipos de armazenamento. O desenho atual demonstra maturidade estrutural e capacidade de evolução sem depender de um único padrão tecnológico.
