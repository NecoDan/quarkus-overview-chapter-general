# Guia de ambiente, publicação na fila RabbitMQ e consulta via API

Este documento descreve como subir o ambiente local do projeto, publicar mensagens na fila RabbitMQ e consultar os dados processados pela API.

## 1) Pré-requisitos

- JDK 21
- Maven Wrapper (`./mvnw`)
- Docker e Docker Compose (ou Docker Engine)
- `curl` para testar as rotas HTTP

## 2) Subir o ambiente local

### 2.1 Iniciar MongoDB e RabbitMQ

No terminal, execute:

```bash
docker run -d --name btg-mongo \
  -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=admin \
  mongo:7

docker run -d --name btg-rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  --hostname rabbitmq \
  rabbitmq:3-management
```

Confirmação:

```bash
docker ps
```

Você deve ver os containers `btg-mongo` e `btg-rabbitmq` em execução.

### 2.2 Iniciar a aplicação Quarkus

Na raiz do projeto, execute:

```bash
./mvnw quarkus:dev
```

A aplicação usa o perfil `btg` por padrão (conforme `src/main/resources/application.properties`), então ela carregará a configuração do arquivo `application-btg.properties`.

A API ficará disponível em:

- Aplicação: `http://localhost:8095`
- Swagger UI: `http://localhost:8095/q/swagger-ui`
- Health check: `http://localhost:8095/q/health`

## 3) Configuração da fila RabbitMQ

A fila e o exchange usados pelo projeto são:

- Exchange: `orders-exchange`
- Routing key: `order-created-key`
- Queue: `btg-pactual-orderbtgpactual-created`

Esses valores estão configurados em `src/main/resources/application-btg.properties`.

## 4) Publicar mensagem na fila RabbitMQ

### Opção A: usar o rabbitmqadmin dentro do container

Se o plugin de management estiver ativo no RabbitMQ, execute:

```bash
docker exec -it btg-rabbitmq rabbitmqadmin \
  --host=localhost \
  --port=15672 \
  --username=guest \
  --password=guest \
  publish \
  exchange=orders-exchange \
  routing_key=order-created-key \
  payload='{"codigoPedido":"123e4567-e89b-12d3-a456-426614174000","codigoCliente":"550e8400-e29b-41d4-a716-446655440000","itens":[{"produto":"lápis","quantidade":100,"preco":1.10},{"produto":"caderno","quantidade":10,"preco":1.00}]}'
```

### Opção B: publicar via HTTP da API de management do RabbitMQ

```bash
curl -u guest:guest \
  -H 'content-type:application/json' \
  -X POST http://localhost:15672/api/exchanges/%2F/orders-exchange/publish \
  -d '{
    "properties": {},
    "routing_key": "order-created-key",
    "payload": "{\"codigoPedido\":\"123e4567-e89b-12d3-a456-426614174000\",\"codigoCliente\":\"550e8400-e29b-41d4-a716-446655440000\",\"itens\":[{\"produto\":\"lápis\",\"quantidade\":100,\"preco\":1.10},{\"produto\":\"caderno\",\"quantidade\":10,\"preco\":1.00}]}",
    "payload_encoding": "string"
  }'
```

Observações:

- `codigoPedido` e `codigoCliente` devem ser UUID válidos.
- A estrutura `itens` deve seguir o formato esperado pelo consumidor da fila.
- O consumidor da aplicação processa a mensagem e salva o pedido no banco MongoDB.

## 5) Consultar os dados via API

Depois que a mensagem for publicada, a aplicação processa o evento e grava o pedido. Acesse as APIs abaixo:

### Listar todos os pedidos

```bash
curl "http://localhost:8095/btgpactual/orders/v1?page=0&size=10&expand_items=true"
```

### Buscar pedido por ID

```bash
curl "http://localhost:8095/btgpactual/orders/v1/{id-do-pedido}"
```

### Total do pedido por ID

```bash
curl "http://localhost:8095/btgpactual/orders/v1/{id-do-pedido}/totalAmount"
```

### Listar pedidos por cliente

```bash
curl "http://localhost:8095/btgpactual/orders/v1/consumers/listAll?customerId=550e8400-e29b-41d4-a716-446655440000&page=0&size=10&expand_items=true"
```

### Resumo por cliente

```bash
curl "http://localhost:8095/btgpactual/orders/v1/consumers/summarize?customerId=550e8400-e29b-41d4-a716-446655440000"
```

## 6) Verificar o processamento

Você pode validar o console da aplicação Quarkus para verificar logs como:

- mensagem recebida da fila RabbitMQ
- processamento do payload
- gravação do pedido no MongoDB

Além disso, pode verificar no RabbitMQ Management em:

```text
http://localhost:15672
```

Usando usuário/senha:

```text
guest / guest
```

## 7) Dicas de uso

- Para testar várias mensagens, repita o comando de publicação com diferentes UUIDs e itens.
- Para reiniciar do zero, remova os containers e reexecute os passos anteriores:

```bash
docker rm -f btg-rabbitmq btg-mongo
```

- Se a aplicação não conectar ao MongoDB ou RabbitMQ, confirme se os containers estão rodando e se as portas 27017, 5672 e 15672 estão livres.

## 8) Resumo rápido

1. Subir MongoDB e RabbitMQ com Docker.
2. Iniciar a aplicação com `./mvnw quarkus:dev`.
3. Publicar uma mensagem no exchange `orders-exchange` com routing key `order-created-key`.
4. Consultar a API em `http://localhost:8095/btgpactual/orders/...`.

Se quiser, eu também posso criar um arquivo `docker-compose.yml` para automatizar esse ambiente em um único comando.