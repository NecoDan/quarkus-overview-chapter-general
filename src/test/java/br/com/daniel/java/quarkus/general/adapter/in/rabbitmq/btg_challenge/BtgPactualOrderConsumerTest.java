package br.com.daniel.java.quarkus.general.adapter.in.rabbitmq.btg_challenge;

import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.OrderBtgPactualCreateUseCase;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input.OrderCreatedEventBtgPactualInput;
import br.com.daniel.java.quarkus.general.core.usecase.btg_challenge.input.OrderItemCreatedEventBtgPactualInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BtgPactualOrderConsumerTest {

    @Mock
    private OrderBtgPactualCreateUseCase orderBtgPactualCreateUseCase;

    @Mock(name = "objectMapper")
    private ObjectMapper objectMapper;

    @InjectMocks
    private BtgPactualOrderConsumer consumer;

    private String validPayload;
    private OrderCreatedEventBtgPactualInput validInput;

    @BeforeEach
    void setUp() {
        validPayload = """
                {
                  "codigoPedido": "PED-001",
                  "codigoCliente": "CLI-001",
                  "itens": [
                    {
                      "produto": "PROD-001",
                      "quantidade": 2,
                      "preco": 100.00
                    }
                  ]
                }
                """;

        validInput = new OrderCreatedEventBtgPactualInput(
                "PED-001",
                "CLI-001",
                List.of(
                        new OrderItemCreatedEventBtgPactualInput(
                                "PROD-001",
                                2,
                                BigDecimal.valueOf(100.00)
                        )
                )
        );

        consumer.queeNameConsumer = "btg-pactual-orderbtgpactual-created";
    }

    @Test
    void successfullyProcessesValidPayload() throws Exception {
        when(objectMapper.readValue(validPayload, OrderCreatedEventBtgPactualInput.class))
                .thenReturn(validInput);

        consumer.consumerProcessOrders(validPayload);

        verify(objectMapper).readValue(validPayload, OrderCreatedEventBtgPactualInput.class);
        verify(orderBtgPactualCreateUseCase).createOrderFrom(validInput);
    }

    @Test
    void clearsTransactionIdAfterSuccessfulProcessing() throws Exception {
        when(objectMapper.readValue(validPayload, OrderCreatedEventBtgPactualInput.class))
                .thenReturn(validInput);

        MDC.put("transactionId", "test-123");

        consumer.consumerProcessOrders(validPayload);

        assertNull(MDC.get("transactionId"));
    }

    @Test
    void clearsTransactionIdOnJsonProcessingException() throws Exception {
        when(objectMapper.readValue(validPayload, OrderCreatedEventBtgPactualInput.class))
                .thenThrow(new com.fasterxml.jackson.core.JsonProcessingException("Invalid JSON") {});

        MDC.put("transactionId", "test-123");

        assertThrows(RuntimeException.class, () -> consumer.consumerProcessOrders(validPayload));

        assertNull(MDC.get("transactionId"));
    }

    @Test
    void clearsTransactionIdOnValidationException() throws Exception {
        when(objectMapper.readValue(validPayload, OrderCreatedEventBtgPactualInput.class))
                .thenReturn(validInput);
        doThrow(new IllegalArgumentException("Invalid order data"))
                .when(orderBtgPactualCreateUseCase).createOrderFrom(any());

        MDC.put("transactionId", "test-123");

        assertThrows(RuntimeException.class, () -> consumer.consumerProcessOrders(validPayload));

        assertNull(MDC.get("transactionId"));
    }

    @Test
    void throwsRuntimeExceptionOnJsonProcessingException() throws Exception {
        com.fasterxml.jackson.core.JsonProcessingException jsonException =
                new com.fasterxml.jackson.core.JsonProcessingException("Invalid JSON") {};

        when(objectMapper.readValue(validPayload, OrderCreatedEventBtgPactualInput.class))
                .thenThrow(jsonException);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> consumer.consumerProcessOrders(validPayload)
        );

        assertTrue(exception.getMessage().contains("Erro ao desserializar JSON"));
        assertSame(jsonException, exception.getCause());
    }

    @Test
    void throwsRuntimeExceptionOnIllegalArgumentException() throws Exception {
        IllegalArgumentException validationException = new IllegalArgumentException("Invalid order");

        when(objectMapper.readValue(validPayload, OrderCreatedEventBtgPactualInput.class))
                .thenReturn(validInput);
        doThrow(validationException)
                .when(orderBtgPactualCreateUseCase).createOrderFrom(any());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> consumer.consumerProcessOrders(validPayload)
        );

        assertTrue(exception.getMessage().contains("Erro ao processar pedido"));
        assertSame(validationException, exception.getCause());
    }

    @Test
    void throwsRuntimeExceptionOnIllegalStateException() throws Exception {
        IllegalStateException stateException = new IllegalStateException("Order already exists");

        when(objectMapper.readValue(validPayload, OrderCreatedEventBtgPactualInput.class))
                .thenReturn(validInput);
        doThrow(stateException)
                .when(orderBtgPactualCreateUseCase).createOrderFrom(any());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> consumer.consumerProcessOrders(validPayload)
        );

        assertTrue(exception.getMessage().contains("Erro ao processar pedido"));
        assertSame(stateException, exception.getCause());
    }

    @Test
    void throwsRuntimeExceptionOnUnexpectedException() throws Exception {
        RuntimeException unexpectedException = new RuntimeException("Database connection failed");

        when(objectMapper.readValue(validPayload, OrderCreatedEventBtgPactualInput.class))
                .thenReturn(validInput);
        doThrow(unexpectedException)
                .when(orderBtgPactualCreateUseCase).createOrderFrom(any());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> consumer.consumerProcessOrders(validPayload)
        );

        assertTrue(exception.getMessage().contains("Erro inesperado ao processar pedido"));
        assertSame(unexpectedException, exception.getCause());
    }

    @Test
    void handlesEmptyItemsPayload() throws Exception {
        String emptyItemsPayload = """
                {
                  "codigoPedido": "PED-002",
                  "codigoCliente": "CLI-002",
                  "itens": []
                }
                """;

        OrderCreatedEventBtgPactualInput emptyInput = new OrderCreatedEventBtgPactualInput(
                "PED-002",
                "CLI-002",
                List.of()
        );

        when(objectMapper.readValue(emptyItemsPayload, OrderCreatedEventBtgPactualInput.class))
                .thenReturn(emptyInput);

        consumer.consumerProcessOrders(emptyItemsPayload);

        verify(objectMapper).readValue(emptyItemsPayload, OrderCreatedEventBtgPactualInput.class);
        verify(orderBtgPactualCreateUseCase).createOrderFrom(emptyInput);
    }

    @Test
    void processesMultipleItemsPayload() throws Exception {
        String multipleItemsPayload = """
                {
                  "codigoPedido": "PED-003",
                  "codigoCliente": "CLI-003",
                  "itens": [
                    {
                      "produto": "PROD-001",
                      "quantidade": 2,
                      "preco": 100.00
                    },
                    {
                      "produto": "PROD-002",
                      "quantidade": 1,
                      "preco": 50.00
                    }
                  ]
                }
                """;

        OrderCreatedEventBtgPactualInput multipleItemsInput = new OrderCreatedEventBtgPactualInput(
                "PED-003",
                "CLI-003",
                List.of(
                        new OrderItemCreatedEventBtgPactualInput("PROD-001", 2, BigDecimal.valueOf(100.00)),
                        new OrderItemCreatedEventBtgPactualInput("PROD-002", 1, BigDecimal.valueOf(50.00))
                )
        );

        when(objectMapper.readValue(multipleItemsPayload, OrderCreatedEventBtgPactualInput.class))
                .thenReturn(multipleItemsInput);

        consumer.consumerProcessOrders(multipleItemsPayload);

        verify(objectMapper).readValue(multipleItemsPayload, OrderCreatedEventBtgPactualInput.class);
        verify(orderBtgPactualCreateUseCase).createOrderFrom(multipleItemsInput);
    }

    @Test
    void doesNotCallUseCaseWhenDeserializationFails() throws Exception {
        when(objectMapper.readValue(validPayload, OrderCreatedEventBtgPactualInput.class))
                .thenThrow(new com.fasterxml.jackson.core.JsonProcessingException("Invalid JSON") {});

        assertThrows(RuntimeException.class, () -> consumer.consumerProcessOrders(validPayload));

        verify(orderBtgPactualCreateUseCase, never()).createOrderFrom(any());
    }

    @Test
    void verifyObjectMapperIsCalledWithCorrectParameters() throws Exception {
        when(objectMapper.readValue(validPayload, OrderCreatedEventBtgPactualInput.class))
                .thenReturn(validInput);

        consumer.consumerProcessOrders(validPayload);

        verify(objectMapper, times(1)).readValue(validPayload, OrderCreatedEventBtgPactualInput.class);
        verifyNoMoreInteractions(objectMapper);
    }

    @Test
    void verifyUseCaseIsCalledWithCorrectInput() throws Exception {
        when(objectMapper.readValue(validPayload, OrderCreatedEventBtgPactualInput.class))
                .thenReturn(validInput);

        consumer.consumerProcessOrders(validPayload);

        verify(orderBtgPactualCreateUseCase, times(1)).createOrderFrom(validInput);
        verifyNoMoreInteractions(orderBtgPactualCreateUseCase);
    }

    @Test
    void useCaseNotCalledMultipleTimesOnSinglePayload() throws Exception {
        when(objectMapper.readValue(validPayload, OrderCreatedEventBtgPactualInput.class))
                .thenReturn(validInput);

        consumer.consumerProcessOrders(validPayload);

        verify(orderBtgPactualCreateUseCase).createOrderFrom(validInput);
        verifyNoMoreInteractions(orderBtgPactualCreateUseCase);
    }
}
