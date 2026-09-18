package br.com.daniel.java.quarkus.general.integration;

import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.OrderBtgPactualCreateUseCase;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.mockito.MockitoConfig;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@QuarkusTest
class BtgOrderRabbitIntegrationTest {

    private static final String CHANNEL = "btg-pactual-orderbtgpactual-created-in";

    @Inject
    @Any
    InMemoryConnector inMemoryConnector;

    @InjectMock
    @MockitoConfig(convertScopes = true)
    OrderBtgPactualCreateUseCase orderCreateUseCase;

    @Test
    void consumesRabbitMessageAndDelegatesDeserializedOrder() {
        var orderId = "3c2b66c2-9b3e-4f6a-a0ae-2dc4723a58f0";
        var customerId = "c7e8fb1a-daa6-48fd-bee2-2653601e2893";
        var payload = """
                {
                  "codigoPedido": "%s",
                  "codigoCliente": "%s",
                  "itens": [
                    {
                      "produto": "PROD-001",
                      "quantidade": 2,
                      "preco": 100.00
                    }
                  ]
                }
                """.formatted(orderId, customerId);

        inMemoryConnector.source(CHANNEL).send(payload);

        verify(orderCreateUseCase, timeout(Duration.ofSeconds(5).toMillis()))
                .createOrderFrom(argThat(input ->
                        input.orderId().toString().equals(orderId)
                                && input.customerId().toString().equals(customerId)
                                && input.items().size() == 1
                                && input.items().getFirst().quantity() == 2
                ));
    }
}
